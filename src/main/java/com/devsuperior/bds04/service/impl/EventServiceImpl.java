package com.devsuperior.bds04.service.impl;

import com.devsuperior.bds04.dto.EventDTO;
import com.devsuperior.bds04.entities.City;
import com.devsuperior.bds04.entities.Event;
import com.devsuperior.bds04.repository.CityRepository;
import com.devsuperior.bds04.repository.EventRepository;
import com.devsuperior.bds04.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CityRepository cityRepository;

    public EventServiceImpl(EventRepository eventRepository, CityRepository cityRepository) {
        this.eventRepository = eventRepository;
        this.cityRepository = cityRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EventDTO> findAllPaged(Pageable pageable) {
        return eventRepository.findAll(pageable).map(EventDTO::new);
    }

    @Transactional
    @Override
    public EventDTO insert(EventDTO dto) {
        City city = cityRepository.getReferenceById(dto.getCityId());
        Event entity = new Event(
                null,
                dto.getName(),
                dto.getDate(),
                dto.getUrl(),
                city
        );
        eventRepository.save(entity);
        return new EventDTO(entity);
    }
}
