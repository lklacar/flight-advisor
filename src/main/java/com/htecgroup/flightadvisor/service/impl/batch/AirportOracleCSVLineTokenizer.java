package com.htecgroup.flightadvisor.service.impl.batch;

import org.springframework.stereotype.Component;

@Component("airportOracleCSVLineTokenizer")
public class AirportOracleCSVLineTokenizer extends AbstractOracleCSVLineTokenizer {
    private static final String[] FIELD_NAMES = {"airportId", "name", "city", "country", "iata", "icao", "latitude", "longitude", "altitude", "timezone", "dst", "tz", "type", "source"};

    @Override
    public String[] getFieldNames() {
        return FIELD_NAMES;
    }
}
