package com.htecgroup.flightadvisor.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.*;

import java.math.BigDecimal;

@RelationshipEntity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Route {
    @Id
    @GeneratedValue
    private Long id;

    private String airline;
    private String airlineId;
    private String sourceAirport;
    private String sourceAirportId;
    private String destinationAirport;
    private String destinationAirportId;
    private String codeshare;
    private Integer stops;
    private String equipment;
    private BigDecimal price;

    @StartNode
    private Airport source;

    @EndNode
    private Airport destination;

}
