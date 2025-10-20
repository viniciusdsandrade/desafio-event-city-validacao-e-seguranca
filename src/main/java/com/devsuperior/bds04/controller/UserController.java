package com.devsuperior.bds04.controller;

import com.devsuperior.bds04.dto.user.UserDTO;
import com.devsuperior.bds04.dto.user.UserInsertDTO;
import com.devsuperior.bds04.dto.user.UserUpdateDTO;
import com.devsuperior.bds04.service.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping({"/api/v1/userss", "/users"})
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PermitAll
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        Page<UserDTO> users = userService.findAllPaged(pageable);
        return ok(users);
    }

    @PermitAll
    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        UserDTO user = userService.findById(id);
        return ok(user);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/me")
    public ResponseEntity<UserDTO> getMe() {
        UserDTO me = userService.getMe();
        return ok(me);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PermitAll
    public ResponseEntity<UserDTO> createUser(
            @Valid @RequestBody UserInsertDTO userInsertDTO,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        UserDTO createdUser = userService.insert(userInsertDTO);
        URI uri = uriComponentsBuilder
                .path("/api/v1/users/{id}")
                .buildAndExpand(createdUser.getId())
                .toUri();
        return created(uri).body(createdUser);
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @PermitAll
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO userInsertDTO
    ) {
        UserDTO updatedUser = userService.update(id, userInsertDTO);
        return ok(updatedUser);
    }
}
