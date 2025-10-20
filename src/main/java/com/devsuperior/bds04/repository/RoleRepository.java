package com.devsuperior.bds04.repository;

import com.devsuperior.bds04.entities.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("roleRepository")
public interface RoleRepository extends JpaRepository<Roles, Long> {
}
