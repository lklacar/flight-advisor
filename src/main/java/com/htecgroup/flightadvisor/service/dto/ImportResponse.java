package com.htecgroup.flightadvisor.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportResponse {
    private Boolean success;
    private Integer totalRecords;
    private Integer writeCount;
}
