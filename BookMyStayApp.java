import java.util.*;

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private double roomCost;

    public Reservation(String reservationId, String guestName, String roomType, double roomCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomCost = roomCost;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getRoomCost() {
        return roomCost;
    }

    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest Name: " + guestName +
                ", Room Type: " + roomType +
                ", Room Cost: Rs." + roomCost;
    }
}

class BookingHistory {
    private List<Reservation> confirmedBookings;

    public BookingHistory() {
        confirmedBookings = new ArrayList<>();
    }

    public void addBooking(Reservation reservation) {
        confirmedBookings.add(reservation);
    }

    public List<Reservation> getAllBookings() {
        return confirmedBookings;
    }
}

class BookingReportService {
    public void displayAllBookings(BookingHistory history) {
        List<Reservation> bookings = history.getAllBookings();

        if (bookings.isEmpty()) {
            System.out.println("No booking history available.");
            return;
        }

        System.out.println("===== Booking History =====");
        for (Reservation reservation : bookings) {
            System.out.println(reservation);
        }
    }

    public void generateSummaryReport(BookingHistory history) {
        List<Reservation> bookings = history.getAllBookings();

        int totalBookings = bookings.size();
        double totalRevenue = 0;

        for (Reservation reservation : bookings) {
            totalRevenue += reservation.getRoomCost();
        }

        System.out.println("\n===== Booking Summary Report =====");
        System.out.println("Total Confirmed Bookings : " + totalBookings);
        System.out.println("Total Revenue            : Rs." + totalRevenue);
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        Reservation r1 = new Reservation("R101", "Christopher", "Deluxe Room", 5000);
        Reservation r2 = new Reservation("R102", "Aarav", "Suite Room", 8000);
        Reservation r3 = new Reservation("R103", "Priya", "Standard Room", 3000);

        BookingHistory bookingHistory = new BookingHistory();

        bookingHistory.addBooking(r1);
        bookingHistory.addBooking(r2);
        bookingHistory.addBooking(r3);

        BookingReportService reportService = new BookingReportService();

        System.out.println("===== Book My Stay - Use Case 8: Booking History & Reporting =====\n");

        reportService.displayAllBookings(bookingHistory);
        reportService.generateSummaryReport(bookingHistory);

        System.out.println("\nStored booking data remains unchanged after reporting.");
    }
}