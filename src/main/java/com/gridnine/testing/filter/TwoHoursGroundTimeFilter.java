package com.gridnine.testing.filter;

import com.gridnine.testing.model.Flight;
import com.gridnine.testing.model.Segment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TwoHoursGroundTimeFilter implements FlightFilter {
    @Override
    public List<Flight> filter(List<Flight> flights) {
        List<Flight> resultFlights = new ArrayList<>();

        for (Flight flightItem : flights) {
            LocalDateTime prevArrival = null;
            boolean flag = true;
            for (Segment segmentItem : flightItem.getSegments()) {
                if (!Objects.isNull(prevArrival)) {
                    Duration duration = Duration.between(prevArrival, segmentItem.getDepartureDate());
                    if (duration.toHoursPart() > 2 || (duration.toHoursPart() == 2 & duration.toMinutesPart() > 0)) {
                        flag = false;
                        break;
                    }
                }
                prevArrival = segmentItem.getArrivalDate();
            }
            if (flag) {
                resultFlights.add(flightItem);
            }
        }
        return resultFlights;
    }

}
