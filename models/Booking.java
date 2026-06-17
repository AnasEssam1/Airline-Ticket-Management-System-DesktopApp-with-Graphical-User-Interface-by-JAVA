package models;

public class Booking {
    private int bookingId;
    private Flight flight;
    private String passengerName;
    private String seatNumber;
    private String status;

    public Booking(int bookingId, Flight flight, String passengerName, String seatNumber, String status) {
        this.bookingId = bookingId;
        this.flight = flight;
        this.passengerName = passengerName;
        this.seatNumber = seatNumber;
        this.status = status;
    }

    public int getBookingId() {
        return bookingId;
    }

    public Flight getFlight() {
        return flight;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getStatus() {
        return status;
    }

    public void cancel() {
        this.status = "Cancelled";
    }
}
