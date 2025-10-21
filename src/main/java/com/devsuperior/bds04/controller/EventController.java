package com.devsuperior.bds04.controller;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.service.EventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.http.ResponseEntity.created;

@RestController
@RequestMapping(value = "/events")
public class EventController {

    private final EventService service;

    public EventController(EventService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<Page<EventDTO>> findAll(Pageable pageable) {
        Page<EventDTO> page = service.findAllPaged(pageable);
        return ok(page);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('CLIENT','ADMIN')")
    public ResponseEntity<EventDTO> insert(@Valid @RequestBody EventDTO dto) {
        EventDTO saved = service.insert(dto);
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return created(uri).body(saved);
    }
}