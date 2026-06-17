
package models;


public class Flight {
    private int flightId;
    private String fromCity;
    private String toCity;
    private String date;
    private String time;
    private double price;

    public Flight(int flightId, String fromCity, String toCity, String date, String time, double price) {
        this.flightId = flightId;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.date = date;
        this.time = time;
        this.price = price;
    }

    public int getFlightId() {
        return flightId;
    }

    public String getFromCity() {
        return fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public double getPrice() {
        return price;
    }
}
