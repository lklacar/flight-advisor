package com.htecgroup.flightadvisor.domain.projection;

@SuppressWarnings("unused")
public interface CityCommentProjection {
    Long getCommentId();

    Long getCityId();

    String getCityCountry();

    String getCityDescription();

    String getCityName();

    String getCommentText();

    String getAuthorUsername();
}
