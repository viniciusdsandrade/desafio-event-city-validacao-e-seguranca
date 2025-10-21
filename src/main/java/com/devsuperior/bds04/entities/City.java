package com.devsuperior.bds04.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.NONE;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_city")
public class City {
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String name;
	
	@OneToMany(mappedBy = "city")
    @Setter(NONE)
	private List<Event> events = new ArrayList<>();
}
