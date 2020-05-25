package com.htecgroup.flightadvisor.service.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDto {

    private String username;
    private String email;
    private List<AuthorityDto> authorities;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String imageUrl;
}
