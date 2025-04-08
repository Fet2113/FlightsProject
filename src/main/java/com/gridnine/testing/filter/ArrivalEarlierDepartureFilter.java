package com.gridnine.testing.filter;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ArrivalEarlierDepartureFilter implements FlightFilter {
    @Override
    public List<Flight> filter(List<Flight> flights) {
        List<Flight> resultFlights = new ArrayList<>();

        for (Flight flightItem : flights) {
            boolean flag = true;
            for (Segment segmentItem : flightItem.getSegments()) {
                if (segmentItem.getArrivalDate().isBefore(segmentItem.getDepartureDate())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                resultFlights.add(flightItem);
            }
        }
        return resultFlights;
    }
}
