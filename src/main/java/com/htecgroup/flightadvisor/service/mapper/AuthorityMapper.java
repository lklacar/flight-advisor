package com.htecgroup.flightadvisor.service.mapper;

import com.htecgroup.flightadvisor.domain.Authority;
import com.htecgroup.flightadvisor.service.dto.AuthorityDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorityMapper {

    AuthorityDto toDto(Authority authority);
}
