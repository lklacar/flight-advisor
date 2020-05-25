package com.htecgroup.flightadvisor.service;

import com.htecgroup.flightadvisor.service.dto.RegisterUserRequest;

public interface UserService {

    /**
     * Register user
     *
     * @param request {@link RegisterUserRequest} object describing the user that needs to register.
     */
    void registerUser(RegisterUserRequest request);

}
