package com.htecgroup.flightadvisor.service.mapper;

import com.htecgroup.flightadvisor.domain.projection.CityCommentProjection;
import com.htecgroup.flightadvisor.service.dto.CityDto;
import com.htecgroup.flightadvisor.service.dto.CommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = CommentMapper.class)
public interface CityCommentProjectionMapper {

    //@formatter:off
    @Mappings(value = {
            @Mapping(source = "cityId", target = "id"),
            @Mapping(source = "cityName", target = "name"),
            @Mapping(source = "cityCountry", target = "country"),
            @Mapping(source = "cityDescription", target = "description")
    })
    //@formatter:on
    CityDto toCity(CityCommentProjection cityCommentProjection);

    //@formatter:off
    @Mappings(value = {
            @Mapping(source = "commentId", target = "id"),
            @Mapping(source = "commentText", target = "text"),
            @Mapping(source = "authorUsername", target = "author")
    })
    //@formatter:on
    CommentDto toComment(CityCommentProjection cityCommentProjection);
}
