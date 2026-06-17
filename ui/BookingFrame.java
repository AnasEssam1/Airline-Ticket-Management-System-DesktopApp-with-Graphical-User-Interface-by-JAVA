package ui;

import models.Booking;
import models.Flight;
import service.AirlineService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class BookingFrame extends JFrame {
    private AirlineService service;
    private Flight selectedFlight;
    private JFrame parentFrame;

    private JTextField passengerNameField;
    private JComboBox<String> seatComboBox;

    public BookingFrame(AirlineService service, Flight selectedFlight, JFrame parentFrame) {
        this.service = service;
        this.selectedFlight = selectedFlight;
        this.parentFrame = parentFrame;

        setTitle("Airline Ticket Management System - Booking");
        setSize(550, 450);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("Confirm Booking", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 55, 90));

        JPanel flightPanel = new JPanel(new GridLayout(5, 2, 8, 8));
        flightPanel.setBorder(BorderFactory.createTitledBorder("Selected Flight Details"));
        flightPanel.setBackground(Color.WHITE);

        flightPanel.add(new JLabel("Flight ID:"));
        flightPanel.add(new JLabel(String.valueOf(selectedFlight.getFlightId())));

        flightPanel.add(new JLabel("Route:"));
        flightPanel.add(new JLabel(selectedFlight.getFromCity() + " → " + selectedFlight.getToCity()));

        flightPanel.add(new JLabel("Date:"));
        flightPanel.add(new JLabel(selectedFlight.getDate()));

        flightPanel.add(new JLabel("Time:"));
        flightPanel.add(new JLabel(selectedFlight.getTime()));

        flightPanel.add(new JLabel("Price:"));
        flightPanel.add(new JLabel(selectedFlight.getPrice() + " EGP"));

        JPanel bookingPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        bookingPanel.setBorder(BorderFactory.createTitledBorder("Passenger Information"));
        bookingPanel.setBackground(Color.WHITE);

        passengerNameField = new JTextField();

        seatComboBox = new JComboBox<>();
        loadAvailableSeats();

        bookingPanel.add(new JLabel("Passenger Name:"));
        bookingPanel.add(passengerNameField);

        bookingPanel.add(new JLabel("Seat Number:"));
        bookingPanel.add(seatComboBox);

        JButton confirmButton = new JButton("Confirm Booking");
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setBackground(new Color(0, 130, 90));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.addActionListener(e -> confirmBooking());

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(new Color(120, 120, 120));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 247, 250));
        buttonPanel.add(confirmButton);
        buttonPanel.add(closeButton);

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 15, 15));
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.add(flightPanel);
        centerPanel.add(bookingPanel);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadAvailableSeats() {
        List<String> seats = service.getAvailableSeats(selectedFlight.getFlightId());
        seatComboBox.removeAllItems();

        for (String seat : seats) {
            seatComboBox.addItem(seat);
        }
    }

    private void confirmBooking() {
        String passengerName = passengerNameField.getText().trim();

        if (passengerName.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter passenger name.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (seatComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "No available seats for this flight.",
                    "Seat Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String seatNumber = seatComboBox.getSelectedItem().toString();

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to confirm this booking?",
                "Confirm Booking",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        Booking booking = service.bookSeat(selectedFlight, passengerName, seatNumber);

        if (booking != null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Booking Confirmed Successfully!\n\n"
                            + "Booking ID: " + booking.getBookingId() + "\n"
                            + "Passenger: " + booking.getPassengerName() + "\n"
                            + "Seat: " + booking.getSeatNumber() + "\n"
                            + "Flight: " + selectedFlight.getFromCity() + " → " + selectedFlight.getToCity(),
                    "Booking Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "This seat is already booked. Please choose another seat.",
                    "Booking Failed",
                    JOptionPane.ERROR_MESSAGE
            );

            loadAvailableSeats();
        }
    }
}
