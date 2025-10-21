package com.devsuperior.bds04.dto.user;
import com.devsuperior.bds04.dto.RoleDTO;
import com.devsuperior.bds04.entities.Users;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

import static lombok.AccessLevel.NONE;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    @Setter(NONE)
    private Set<RoleDTO> roles = new HashSet<>();

    public UserDTO(Users users) {
        this.id = users.getId();
        this.firstName = users.getFirstName();
        this.lastName  = users.getLastName();
        this.email     = users.getEmail();
        users.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
    }
}