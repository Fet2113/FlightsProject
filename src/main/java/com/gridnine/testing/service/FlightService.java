package com.gridnine.testing.service;

import com.gridnine.testing.filter.FlightFilter;
import com.gridnine.testing.model.Flight;

import java.util.List;

public class FlightService {
    private List<FlightFilter> filters;

    public FlightService(List<FlightFilter> filters) {
        this.filters = filters;
    }

    public List<Flight> filterFlights(List<Flight> flights) {
        for (FlightFilter filter : filters) {
            flights = filter.filter(flights);
        }
        return flights;
    }
}
