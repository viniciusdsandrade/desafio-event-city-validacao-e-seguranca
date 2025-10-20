package com.devsuperior.bds04.service;



import com.devsuperior.bds04.dto.user.UserDTO;
import com.devsuperior.bds04.dto.user.UserInsertDTO;
import com.devsuperior.bds04.dto.user.UserUpdateDTO;
import com.devsuperior.bds04.entities.Users;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDTO> findAllPaged(Pageable pageable);

    @Transactional
    UserDTO insert(@Valid UserInsertDTO userInsertDTO);

    UserDTO findById(Long id);

    @Transactional
    UserDTO update(Long id, @Valid UserUpdateDTO userInsertDTO);

    Users authenticated();

    UserDTO getMe();
}
