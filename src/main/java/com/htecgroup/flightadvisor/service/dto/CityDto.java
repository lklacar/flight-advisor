package com.htecgroup.flightadvisor.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityDto {
    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Size(min = 1, max = 100)
    private String country;

    @NotNull
    @Size(min = 1, max = 2000)
    private String description;

    private Set<CommentDto> comments;
}
