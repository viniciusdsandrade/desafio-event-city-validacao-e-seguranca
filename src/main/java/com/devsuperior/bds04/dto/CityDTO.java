package com.devsuperior.bds04.dto;

import java.io.Serializable;

import com.devsuperior.bds04.entities.City;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CityDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "Campo requerido")
    private String name;

    public CityDTO(City entity) {
        id = entity.getId();
        name = entity.getName();
    }
}
