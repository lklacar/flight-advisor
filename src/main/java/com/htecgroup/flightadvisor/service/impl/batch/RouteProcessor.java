package com.htecgroup.flightadvisor.service.impl.batch;

import com.htecgroup.flightadvisor.domain.Airport;
import com.htecgroup.flightadvisor.domain.Route;
import org.springframework.batch.item.ItemProcessor;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RouteProcessor implements ItemProcessor<Route, Route> {

    private final List<Airport> airports;

    public RouteProcessor(List<Airport> airports) {
        this.airports = airports;
    }

    @Override
    public Route process(Route route) {
        var sourceAirport = filterAirportByAirportIdOrIataOrIcao(route.getSourceAirportId(),
                route.getSourceAirport(),
                route.getSourceAirport()
        ).orElse(null);

        var destinationAirport = filterAirportByAirportIdOrIataOrIcao(route.getDestinationAirportId(),
                route.getDestinationAirport(),
                route.getDestinationAirport()
        ).orElse(null);

        if (sourceAirport == null || destinationAirport == null) {
            return null;
        }

        route.setSource(sourceAirport);
        route.setDestination(destinationAirport);

        return route;
    }

    private Optional<Airport> filterAirportByAirportIdOrIataOrIcao(String airportId, String iata, String icao) {
        return airports.stream()
                .filter(airport -> Objects.equals(airport.getAirportId(),
                        airportId
                ) || Objects.equals(airport.getIata(), iata) || Objects.equals(
                        airport.getIcao(),
                        icao
                ))
                .findFirst();
    }
}
