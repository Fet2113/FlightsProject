package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;


class FlightBuilder {
    public static List<Flight> createFlights() {
        LocalDateTime threeDaysFromNow = LocalDateTime.now().plusDays(3);
        return Arrays.asList(
                //A normal flight with two hour duration
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2)),
                //A normal multi segment flight
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                        threeDaysFromNow.plusHours(3), threeDaysFromNow.plusHours(5)),
                //A flight departing in the past
                createFlight(threeDaysFromNow.minusDays(6), threeDaysFromNow),
                //A flight that departs before it arrives
                createFlight(threeDaysFromNow, threeDaysFromNow.minusHours(6)),
                //A flight with more than two hours ground time
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                        threeDaysFromNow.plusHours(5), threeDaysFromNow.plusHours(6)),
                //Another flight with more than two hours ground time
                createFlight(threeDaysFromNow, threeDaysFromNow.plusHours(2),
                        threeDaysFromNow.plusHours(3), threeDaysFromNow.plusHours(4),
                        threeDaysFromNow.plusHours(6), threeDaysFromNow.plusHours(7)));
    }

    private static Flight createFlight(final LocalDateTime... dates) {
        if ((dates.length % 2) != 0) {
            throw new IllegalArgumentException(
                    "you must pass an even number of dates");
        }
        List<Segment> segments = new ArrayList<>(dates.length / 2);
        for (int i = 0; i < (dates.length - 1); i += 2) {
            segments.add(new Segment(dates[i], dates[i + 1]));
        }
        return new Flight(segments);
    }
}

class Flight {
    private final List<Segment> segments;

    Flight(final List<Segment> segs) {
        segments = segs;
    }

    List<Segment> getSegments() {
        return segments;
    }

    @Override
    public String toString() {
        return segments.stream().map(Object::toString)
                .collect(Collectors.joining(" "));
    }
}


class Segment {
    private final LocalDateTime departureDate;

    private final LocalDateTime arrivalDate;

    Segment(final LocalDateTime dep, final LocalDateTime arr) {
        departureDate = Objects.requireNonNull(dep);
        arrivalDate = Objects.requireNonNull(arr);
    }

    LocalDateTime getDepartureDate() {
        return departureDate;
    }

    LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return '[' + departureDate.format(fmt) + '|' + arrivalDate.format(fmt)
                + ']';
    }
}

interface flightFilter {
    Predicate<Flight> filter();
}
// * Сегменты с датой прилёта до текущего момента времени
class DepartureUpToCurrentMoment implements flightFilter {
    @Override
    public Predicate<Flight> filter() {

        return flight -> flight.getSegments()
                .stream()
                .anyMatch(segment -> segment.getDepartureDate().isBefore(LocalDateTime.now()));
    }
}

// * Сегменты с датой прилёта раньше даты вылета
class SegmentsWithArrivalDateBeforeDepartureDate implements flightFilter {
    @Override
    public Predicate<Flight> filter() {

        return flight -> flight.getSegments()
                .stream()
                .anyMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate()));
    }
}

//        * Перелеты где общее время на земле больше 2 часов
class FlightsWhereTheTotalTimeOnTheGroundIsMoreThanTwoHours implements flightFilter {
    @Override
    public Predicate<Flight> filter() {
        return flight -> {
            long timeOnEarth = 0;
            List<Segment> segments = flight.getSegments();
            for (var i = 0; i < flight.getSegments().size() - 1; i++) {

                timeOnEarth += Duration.between(segments.get(i).getArrivalDate(), segments.get(i + 1).getDepartureDate())
                        .toMillis();
            }

            return timeOnEarth < 2 * 60 * 60 * 1000;
        };
    }
}


class FlightMain {
    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();

        System.out.println("Общая выборка полётов");
        flights.forEach(flight -> System.out.println(flight.toString()));

        System.out.println("Вылет до текущего момента времени");
        flights.stream().filter(new DepartureUpToCurrentMoment().filter())
                .forEach(f -> System.out.println(f.toString()));

        System.out.println("Сегменты с датой прилёта раньше даты вылета");
        flights.stream().filter(new SegmentsWithArrivalDateBeforeDepartureDate().filter())
                .forEach(f -> System.out.println(f.toString()));

        System.out.println("Перелеты где общее время на земле больше 2 часов");
        flights.stream().filter(new FlightsWhereTheTotalTimeOnTheGroundIsMoreThanTwoHours().filter())
                .forEach(f -> System.out.println(f.toString()));
    }
}


