package com.htecgroup.flightadvisor.service.impl.batch;

import org.springframework.stereotype.Component;

@Component("routeOracleCSVLineTokenizer")
public class RouteOracleCSVLineTokenizer extends AbstractOracleCSVLineTokenizer {
    private static final String[] FIELD_NAMES = {"airline", "airlineId", "sourceAirport", "sourceAirportId", "destinationAirport", "destinationAirportId", "codeshare", "stops", "equipment", "price",};

    @Override
    public String[] getFieldNames() {
        return FIELD_NAMES;
    }
}
