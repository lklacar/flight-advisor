package com.htecgroup.flightadvisor.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;

@Data
@NodeEntity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Airport {
    @Id
    @GeneratedValue
    private Long id;

    @Index(unique = true)
    private String airportId;
    private String name;
    private String city;
    private String country;
    @Index(unique = true)
    private String iata;
    @Index(unique = true)
    private String icao;
    private String latitude;
    private String longitude;
    private String altitude;
    private String timezone;
    private String dst;
    private String tz;
    private String type;
    private String source;
    private Long cityId;
}
