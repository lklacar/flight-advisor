package com.htecgroup.flightadvisor.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {

    @NotEmpty
    @Size(min = 4, max = 20)
    private String username;

    @NotEmpty
    @Size(min = 4, max = 20)
    private String password;

    @NotEmpty
    @Size(min = 1, max = 100)
    private String firstName;

    @NotEmpty
    @Size(min = 1, max = 100)
    private String lastName;
}
