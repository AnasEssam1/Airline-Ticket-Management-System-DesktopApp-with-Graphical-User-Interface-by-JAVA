package models;

public class Seat {
    private String seatNumber;
    private boolean isbooked;

    public Seat(String seatNumber) {
        this.seatNumber=seatNumber;
        this.isbooked=false;
    }
    public String getseatNumber(){
        return seatNumber;
    }
    public boolean isBooked() {
        return isbooked;
    }
    public void bookSeat(){
        if (isbooked) {
            throw new RuntimeException("Seat already booked!") ;
        }
        isbooked=true;

    }

    public void cancel(){
        isbooked=false;
    }
}
