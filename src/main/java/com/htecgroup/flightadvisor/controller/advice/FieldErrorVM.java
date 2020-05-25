package com.htecgroup.flightadvisor.controller.advice;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class FieldErrorVM implements Serializable {

    private final String objectName;
    private final String field;
    private final String message;
}

