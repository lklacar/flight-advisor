package com.htecgroup.flightadvisor.service.mapper;

import com.htecgroup.flightadvisor.domain.Comment;
import com.htecgroup.flightadvisor.service.dto.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(source = "author.username", target = "author")
    CommentDto toDto(Comment comment);

    @Mapping(target = "author", ignore = true)
    Comment fromDto(CommentDto commentDto);
}
