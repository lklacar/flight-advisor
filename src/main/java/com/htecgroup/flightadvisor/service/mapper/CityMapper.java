package com.htecgroup.flightadvisor.service.mapper;

import com.htecgroup.flightadvisor.domain.City;
import com.htecgroup.flightadvisor.service.dto.CityDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = CommentMapper.class)
public interface CityMapper {
    CityDto toDto(City city);

    City fromDto(CityDto dto);
}
