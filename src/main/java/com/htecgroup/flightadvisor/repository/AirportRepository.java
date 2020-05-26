package com.htecgroup.flightadvisor.repository;

import com.htecgroup.flightadvisor.domain.Airport;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface AirportRepository extends CrudRepository<Airport, String> {

    // @formatter:off
    @Query("MATCH (from:Airport{cityId: $fromCityId}), (end:Airport{cityId: $toCityId}) " +
           "CALL apoc.algo.dijkstra(from, end, 'ROUTE>', 'price') " +
           "YIELD path, weight as totalPrice RETURN nodes(path) AS airports, totalPrice")
    List<Map<String, Object>> findCheapestFlights(@Param("fromCityId") Long fromCityId, @Param("toCityId") Long toCityId);
    // @formatter:on
}
