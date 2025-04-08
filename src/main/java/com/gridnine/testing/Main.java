package com.gridnine.testing;


import com.gridnine.testing.factory.FlightBuilder;
import com.gridnine.testing.filter.ArrivalEarlierDepartureFilter;
import com.gridnine.testing.filter.DepartureBeforeNowFilter;
import com.gridnine.testing.filter.TwoHoursGroundTimeFilter;
import com.gridnine.testing.model.Flight;
import com.gridnine.testing.service.FlightService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Flight> flightsArray = FlightBuilder.createFlights();

        System.out.println(flightsArray);

        FlightService fService1 = new FlightService(List.of(new DepartureBeforeNowFilter()));
        System.out.println(fService1.filterFlights(flightsArray));

        FlightService fService2 = new FlightService(List.of(new ArrivalEarlierDepartureFilter()));
        System.out.println(fService2.filterFlights(flightsArray));

        FlightService fService3 = new FlightService(List.of(new TwoHoursGroundTimeFilter()));
        System.out.println(fService3.filterFlights(flightsArray));

        FlightService fService4 = new FlightService(List.of(new TwoHoursGroundTimeFilter(), new DepartureBeforeNowFilter(), new ArrivalEarlierDepartureFilter()));
        System.out.println(fService4.filterFlights(flightsArray));
    }
}