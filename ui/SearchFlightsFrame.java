package ui;

import models.Flight;
import models.User;
import service.AirlineService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SearchFlightsFrame extends JFrame {
    private AirlineService service;
    private User currentUser;

    private JComboBox<String> fromComboBox;
    private JComboBox<String> toComboBox;
    private JTextField dateField;
    private JTable flightsTable;
    private DefaultTableModel tableModel;

    private List<Flight> currentFlights;

    public SearchFlightsFrame(AirlineService service, User currentUser) {
        this.service = service;
        this.currentUser = currentUser;

        setTitle("Airline Ticket Management System - Search Flights");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        mainPanel.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("Search Available Flights");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(30, 55, 90));

        JLabel userLabel = new JLabel("Logged in as: " + currentUser.getFullName());
        userLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        userLabel.setForeground(Color.GRAY);

        JPanel topTitlePanel = new JPanel(new BorderLayout());
        topTitlePanel.setBackground(new Color(245, 247, 250));
        topTitlePanel.add(titleLabel, BorderLayout.WEST);
        topTitlePanel.add(userLabel, BorderLayout.EAST);

        JPanel searchPanel = new JPanel(new GridLayout(2, 4, 10, 8));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Flight Search"));
        searchPanel.setBackground(Color.WHITE);

        fromComboBox = new JComboBox<>(new String[]{"Any", "Cairo", "Alexandria"});
        toComboBox = new JComboBox<>(new String[]{"Any", "Dubai", "Riyadh", "Istanbul", "London", "Paris"});
        dateField = new JTextField();
        dateField.setToolTipText("Example: 2026-05-20");

        JButton searchButton = new JButton("Search");
        searchButton.setBackground(new Color(30, 90, 160));
        searchButton.setForeground(Color.WHITE);
        searchButton.setFocusPainted(false);
        searchButton.addActionListener(e -> searchFlights());

        searchPanel.add(new JLabel("From:"));
        searchPanel.add(new JLabel("To:"));
        searchPanel.add(new JLabel("Date:"));
        searchPanel.add(new JLabel(""));

        searchPanel.add(fromComboBox);
        searchPanel.add(toComboBox);
        searchPanel.add(dateField);
        searchPanel.add(searchButton);

        String[] columns = {"Flight ID", "From", "To", "Date", "Time", "Price"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        flightsTable = new JTable(tableModel);
        flightsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        flightsTable.setRowHeight(28);
        flightsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        flightsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(flightsTable);

        JButton bookButton = new JButton("Book Selected Flight");
        bookButton.setFont(new Font("Arial", Font.BOLD, 14));
        bookButton.setBackground(new Color(0, 130, 90));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFocusPainted(false);
        bookButton.addActionListener(e -> openBookingScreen());

        JButton myBookingsButton = new JButton("My Bookings");
        myBookingsButton.setFont(new Font("Arial", Font.BOLD, 14));
        myBookingsButton.setBackground(new Color(90, 90, 90));
        myBookingsButton.setForeground(Color.WHITE);
        myBookingsButton.setFocusPainted(false);
        myBookingsButton.addActionListener(e -> new MyBookingsFrame(service, this).setVisible(true));

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(150, 40, 40));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.addActionListener(e -> {
            new LoginFrame(service).setVisible(true);
            dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 247, 250));
        buttonPanel.add(myBookingsButton);
        buttonPanel.add(bookButton);
        buttonPanel.add(logoutButton);

        JPanel northPanel = new JPanel(new BorderLayout(10, 10));
        northPanel.setBackground(new Color(245, 247, 250));
        northPanel.add(topTitlePanel, BorderLayout.NORTH);
        northPanel.add(searchPanel, BorderLayout.CENTER);

        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        searchFlights();
    }

    private void searchFlights() {
        String from = fromComboBox.getSelectedItem().toString();
        String to = toComboBox.getSelectedItem().toString();
        String date = dateField.getText().trim();

        currentFlights = service.searchFlights(from, to, date);

        tableModel.setRowCount(0);

        for (Flight flight : currentFlights) {
            tableModel.addRow(new Object[]{
                    flight.getFlightId(),
                    flight.getFromCity(),
                    flight.getToCity(),
                    flight.getDate(),
                    flight.getTime(),
                    flight.getPrice()
            });
        }

        if (currentFlights.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "No flights found for your search.",
                    "Search Result",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void openBookingScreen() {
        int selectedRow = flightsTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a flight first.",
                    "No Flight Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Flight selectedFlight = currentFlights.get(selectedRow);
        new BookingFrame(service, selectedFlight, this).setVisible(true);
    }
}
