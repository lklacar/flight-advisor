package com.htecgroup.flightadvisor.service.mapper;

import com.htecgroup.flightadvisor.domain.ApplicationUser;
import com.htecgroup.flightadvisor.service.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AuthorityMapper.class)
public interface UserMapper {

    UserDto toDto(ApplicationUser applicationUser);
}
