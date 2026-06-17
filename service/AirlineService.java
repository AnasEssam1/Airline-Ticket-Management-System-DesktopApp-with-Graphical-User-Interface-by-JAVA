package service;

import models.Booking;
import models.Flight;
import models.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/*
    This class is a temporary backend/mock service.
    Later, the Database Developer can replace the data here with MySQL queries.
*/
public class AirlineService {
    private List<User> users = new ArrayList<>();
    private List<Flight> flights = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();

    private int nextBookingId = 1001;

    public AirlineService() {
        seedUsers();
        seedFlights();
    }

    private void seedUsers() {
    users.add(new User(1, "anas", "1234", "anas awad", "anasessam5707@email.com")); 
    users.add(new User(2, "admin", "admin", "System Admin", "admin@email.com"));
}

    private void seedFlights() {
        flights.add(new Flight(1, "Cairo", "Dubai", "2026-05-20", "10:30", 5000));
        flights.add(new Flight(2, "Cairo", "Dubai", "2026-05-20", "18:00", 6200));
        flights.add(new Flight(3, "Cairo", "Riyadh", "2026-05-21", "12:00", 4500));
        flights.add(new Flight(4, "Alexandria", "Istanbul", "2026-05-22", "09:15", 7200));
        flights.add(new Flight(5, "Cairo", "London", "2026-05-25", "02:00", 14500));
        flights.add(new Flight(6, "Cairo", "Paris", "2026-05-25", "06:45", 13200));
    }

    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) && user.checkPassword(password)) {
                return user;
            }
        }
        return null;
    }

    public List<Flight> searchFlights(String from, String to, String date) {
        List<Flight> result = new ArrayList<>();

        for (Flight flight : flights) {
            boolean matchesFrom = from.equalsIgnoreCase("Any") || flight.getFromCity().equalsIgnoreCase(from);
            boolean matchesTo = to.equalsIgnoreCase("Any") || flight.getToCity().equalsIgnoreCase(to);
            boolean matchesDate = date.trim().isEmpty() || flight.getDate().equals(date.trim());

            if (matchesFrom && matchesTo && matchesDate) {
                result.add(flight);
            }
        }

        return result;
    }

    public List<String> getAvailableSeats(int flightId) {
        String[] allSeats = {
                "A1", "A2", "A3", "A4",
                "B1", "B2", "B3", "B4",
                "C1", "C2", "C3", "C4"
        };

        Set<String> bookedSeats = new HashSet<>();
        for (Booking booking : bookings) {
            if (booking.getFlight().getFlightId() == flightId && booking.getStatus().equals("Confirmed")) {
                bookedSeats.add(booking.getSeatNumber());
            }
        }

        List<String> availableSeats = new ArrayList<>();
        for (String seat : allSeats) {
            if (!bookedSeats.contains(seat)) {
                availableSeats.add(seat);
            }
        }

        return availableSeats;
    }

    public Booking bookSeat(Flight flight, String passengerName, String seatNumber) {
        if (flight == null || passengerName == null || passengerName.trim().isEmpty() || seatNumber == null) {
            return null;
        }

        for (Booking booking : bookings) {
            if (booking.getFlight().getFlightId() == flight.getFlightId()
                    && booking.getSeatNumber().equals(seatNumber)
                    && booking.getStatus().equals("Confirmed")) {
                return null;
            }
        }

        Booking booking = new Booking(nextBookingId++, flight, passengerName.trim(), seatNumber, "Confirmed");
        bookings.add(booking);
        return booking;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public boolean cancelBooking(int bookingId) {
        for (Booking booking : bookings) {
            if (booking.getBookingId() == bookingId && booking.getStatus().equals("Confirmed")) {
                booking.cancel();
                return true;
            }
        }
        return false;
    }
}
