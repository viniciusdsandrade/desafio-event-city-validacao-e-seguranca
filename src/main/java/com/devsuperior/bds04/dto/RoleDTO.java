package com.devsuperior.bds04.dto;

import com.devsuperior.bds04.entities.Roles;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    @JsonProperty(access = WRITE_ONLY)
    private Long id;
    private String authority;

    public RoleDTO(Roles roles) {
        this.id = roles.getId();
        this.authority = roles.getAuthority();
    }
}
