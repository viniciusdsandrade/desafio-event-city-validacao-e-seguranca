package com.devsuperior.bds04.service;

import com.devsuperior.bds04.dto.EventDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface EventService {

    @Transactional(readOnly = true)
    Page<EventDTO> findAllPaged(Pageable pageable);

    @Transactional
    EventDTO insert(EventDTO dto);
}
