package com.htecgroup.flightadvisor.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticateRequest {

    @NotNull
    private String username;
    @NotNull
    private String password;
}
