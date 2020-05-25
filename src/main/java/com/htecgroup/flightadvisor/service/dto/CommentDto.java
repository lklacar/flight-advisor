package com.htecgroup.flightadvisor.service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentDto {
    private Long id;
    private String author;
    private String text;
}

