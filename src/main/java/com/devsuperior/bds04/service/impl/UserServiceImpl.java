package com.devsuperior.bds04.service.impl;


import com.devsuperior.bds04.dto.user.UserDTO;
import com.devsuperior.bds04.dto.user.UserInsertDTO;
import com.devsuperior.bds04.dto.user.UserUpdateDTO;
import com.devsuperior.bds04.entities.Roles;
import com.devsuperior.bds04.entities.Users;
import com.devsuperior.bds04.exception.DuplicateEntryException;
import com.devsuperior.bds04.exception.ResourceNotFoundException;
import com.devsuperior.bds04.projections.UserDetailsProjection;
import com.devsuperior.bds04.repository.RoleRepository;
import com.devsuperior.bds04.repository.UserRepository;
import com.devsuperior.bds04.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Locale.ROOT;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Long ROLE_CLIENT_ID = 2L;

    private final PasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(
            PasswordEncoder bcryptpasswordencoder,
            UserRepository userRepository,
            RoleRepository roleRepository
    ) {
        this.bCryptPasswordEncoder = bcryptpasswordencoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> results = userRepository.searchUserAndRolesByEmail(username);
        if (results.isEmpty())
            throw new UsernameNotFoundException("Email not found: " + username);
        Users users = new Users();
        users.setEmail(results.getFirst().getUsername());
        users.setPassword(results.getFirst().getPassword());
        results.forEach(projection -> users.addRole(
                new Roles(projection.getRoleId(), projection.getAuthority())
        ));
        return users;
    }

    @Override
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        Page<Users> list = userRepository.findAll(pageable);
        return list.map(UserDTO::new);
    }

    @Override
    public UserDTO findById(Long id) {
        Users users = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserDTO(users);
    }

    @Override
    @Transactional
    public UserDTO insert(@Valid UserInsertDTO dto) {
        final String normalizedEmail = dto.email().trim().toLowerCase();

        if (userRepository.findByEmail(normalizedEmail).isPresent())
            throw new DuplicateEntryException("Email already exists: " + normalizedEmail);

        try {
            Users entity = new Users();
            entity.initializeProfile(
                    dto.firstName(),
                    dto.lastName(),
                    normalizedEmail,
                    bCryptPasswordEncoder.encode(dto.password())
            );

            entity.getRoles().clear();
            entity.getRoles().add(roleRepository.getReferenceById(ROLE_CLIENT_ID));

            userRepository.saveAndFlush(entity);
            return new UserDTO(entity);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEntryException("Email already exists: " + normalizedEmail);
        }
    }

    @Override
    @Transactional
    public UserDTO update(final Long id, final UserUpdateDTO dto) {
        final Users entity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        final Authentication auth = requireAuthenticated();
        final String requester = resolveRequesterIdentity(auth);

        assertOwner(entity, requester);

        final String normalizedEmail = normalizeEmail(dto.email());

        validateEmailChange(entity, normalizedEmail, id);

        entity.updateProfile(dto.firstName(), dto.lastName(), normalizedEmail);
        userRepository.save(entity);

        return new UserDTO(entity);
    }

    @Override
    public Users authenticated() {
        final Authentication auth = requireAuthenticated();
        final String requester = resolveRequesterIdentity(auth);
        return userRepository.findByEmail(requester)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public UserDTO getMe() {
        final Authentication auth = requireAuthenticated();
        final String requester = resolveRequesterIdentity(auth);

        final Users entity = userRepository.findByEmail(requester)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new UserDTO(entity);
    }

    private Authentication requireAuthenticated() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated())
            throw new AuthenticationCredentialsNotFoundException("No authentication");
        return auth;
    }

    private String resolveRequesterIdentity(Authentication auth) {
        if (auth instanceof JwtAuthenticationToken jwt) {
            Object username = jwt.getTokenAttributes().get("username");
            return (username != null) ? username.toString() : jwt.getName();
        }
        return auth.getName();
    }

    private void assertOwner(Users entity, String requesterIdentity) {
        String ownerEmail = entity.getEmail();
        boolean isOwner = ownerEmail != null
                && ownerEmail.equalsIgnoreCase(requesterIdentity);

        if (!isOwner) throw new AccessDeniedException("You are not allowed to update this user");
    }

    private String normalizeEmail(String raw) {
        if (raw == null) throw new ValidationException("Email must not be null");
        return raw.trim().toLowerCase(ROOT);
    }

    private void validateEmailChange(Users entity, String newEmail, Long id) {
        if (entity.getEmail() != null && entity.getEmail().equalsIgnoreCase(newEmail))
            throw new DuplicateEntryException("New email must be different from current");
        if (userRepository.existsByEmailIgnoreCaseAndIdNot(newEmail, id))
            throw new DuplicateEntryException("Email already exists: " + newEmail);
    }
}
