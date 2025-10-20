package com.devsuperior.bds04.service;


import com.devsuperior.bds04.dto.CityDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CityService {

    @Transactional(readOnly = true)
    List<CityDTO> findAllSortedByName();

    @Transactional
    CityDTO insert(CityDTO dto);
}
