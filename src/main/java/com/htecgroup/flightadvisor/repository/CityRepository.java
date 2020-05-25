package com.htecgroup.flightadvisor.repository;

import com.htecgroup.flightadvisor.domain.City;
import com.htecgroup.flightadvisor.domain.projection.CityCommentProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByNameAndCountry(String name, String country);

    //@formatter:off
    @Query(value = "select com.ID        as commentId,\n" +
            "       com.TEXT      as commentText,\n" +
            "       c.ID          as cityId,\n" +
            "       c.COUNTRY     as cityCountry,\n" +
            "       c.DESCRIPTION as cityDescription,\n" +
            "       c.NAME        as cityName,\n" +
            "       AU.USERNAME   as authorUsername\n" +
            "from COMMENT com\n" +
            "         right join city c on com.id in\n" +
            "                              (SELECT com1.id\n" +
            "                               FROM comment AS com1\n" +
            "                               WHERE c.id = com1.CITY_ID\n" +
            "                               ORDER BY created_date DESC\n" +
            "                               LIMIT :numberOfComments)\n" +
            "    and c.NAME ilike CONCAT('%', :cityName, '%')\n" +
            "         left join APPLICATION_USER AU on com.USER_ID = AU.ID\n" +
            "where c.ID is not null;\n", nativeQuery = true)
    //@formatter:on
    List<CityCommentProjection> getCitiesWithComments(@Param("numberOfComments") Integer numberOfComments, @Param("cityName") String cityName);
}

