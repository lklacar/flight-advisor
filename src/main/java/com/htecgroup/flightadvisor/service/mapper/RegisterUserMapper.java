package com.htecgroup.flightadvisor.service.mapper;


import com.htecgroup.flightadvisor.domain.ApplicationUser;
import com.htecgroup.flightadvisor.service.dto.RegisterUserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegisterUserMapper {

    ApplicationUser toUser(RegisterUserRequest registerUserRequest);
}
