package ui;

import models.Booking;
import service.AirlineService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MyBookingsFrame extends JFrame {
    private AirlineService service;
    private JFrame parentFrame;

    private JTable bookingsTable;
    private DefaultTableModel tableModel;
    private List<Booking> bookings;

    public MyBookingsFrame(AirlineService service, JFrame parentFrame) {
        this.service = service;
        this.parentFrame = parentFrame;

        setTitle("Airline Ticket Management System - My Bookings");
        setSize(850, 450);
        setLocationRelativeTo(parentFrame);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        loadBookings();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("My Bookings");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 55, 90));

        String[] columns = {"Booking ID", "Passenger", "Flight", "Date", "Time", "Seat", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        bookingsTable = new JTable(tableModel);
        bookingsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        bookingsTable.setRowHeight(28);
        bookingsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        bookingsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(bookingsTable);

        JButton cancelBookingButton = new JButton("Cancel Selected Booking");
        cancelBookingButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelBookingButton.setBackground(new Color(170, 60, 40));
        cancelBookingButton.setForeground(Color.WHITE);
        cancelBookingButton.setFocusPainted(false);
        cancelBookingButton.addActionListener(e -> cancelSelectedBooking());

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.setBackground(new Color(120, 120, 120));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 247, 250));
        buttonPanel.add(cancelBookingButton);
        buttonPanel.add(closeButton);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadBookings() {
        bookings = service.getBookings();
        tableModel.setRowCount(0);

        for (Booking booking : bookings) {
            tableModel.addRow(new Object[]{
                    booking.getBookingId(),
                    booking.getPassengerName(),
                    booking.getFlight().getFromCity() + " → " + booking.getFlight().getToCity(),
                    booking.getFlight().getDate(),
                    booking.getFlight().getTime(),
                    booking.getSeatNumber(),
                    booking.getStatus()
            });
        }
    }

    private void cancelSelectedBooking() {
        int selectedRow = bookingsTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a booking first.",
                    "No Booking Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
        String status = tableModel.getValueAt(selectedRow, 6).toString();

        if (status.equals("Cancelled")) {
            JOptionPane.showMessageDialog(
                    this,
                    "This booking is already cancelled.",
                    "Cancel Booking",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to cancel this booking?",
                "Cancel Booking",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        boolean cancelled = service.cancelBooking(bookingId);

        if (cancelled) {
            JOptionPane.showMessageDialog(
                    this,
                    "Booking cancelled successfully.",
                    "Cancellation Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            loadBookings();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Could not cancel booking.",
                    "Cancellation Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
