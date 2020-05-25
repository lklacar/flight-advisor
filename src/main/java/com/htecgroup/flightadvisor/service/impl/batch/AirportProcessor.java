package com.htecgroup.flightadvisor.service.impl.batch;

import com.htecgroup.flightadvisor.domain.Airport;
import com.htecgroup.flightadvisor.domain.City;
import org.springframework.batch.item.ItemProcessor;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

class AirportProcessor implements ItemProcessor<Airport, Airport> {

    private final List<City> cities;
    private final List<Airport> airports;

    public AirportProcessor(List<City> cities, List<Airport> airports) {
        this.cities = cities;
        this.airports = airports;
    }

    @Override
    public Airport process(@Nonnull Airport airport) {
        var updatedAirport = cities.stream()
                .filter(filterCityByAirportPredicate(airport))
                .findFirst()
                .map(city -> {
                    airport.setCityId(city.getId());
                    return airport;
                })
                .orElse(null);

        return updatedAirport != null && airports.stream()
                .anyMatch(airport1 -> airport1.getAirportId()
                        .equals(updatedAirport.getAirportId())) ? null : updatedAirport;

    }

    private Predicate<City> filterCityByAirportPredicate(@Nonnull Airport airport) {
        return city -> Objects.equals(city.getName(), airport.getCity()) && Objects.equals(city.getCountry(),
                airport.getCountry()
        );
    }

}
