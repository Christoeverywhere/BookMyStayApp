import java.util.*;

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;
    private double roomCost;
    private boolean active;

    public Reservation(String reservationId, String guestName, String roomType, String roomId, double roomCost) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
        this.roomCost = roomCost;
        this.active = true;
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

    public String getRoomId() {
        return roomId;
    }

    public double getRoomCost() {
        return roomCost;
    }

    public boolean isActive() {
        return active;
    }

    public void cancel() {
        active = false;
    }

    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest Name: " + guestName +
                ", Room Type: " + roomType +
                ", Room ID: " + roomId +
                ", Room Cost: Rs." + roomCost +
                ", Status: " + (active ? "Confirmed" : "Cancelled");
    }
}

class HotelInventory {
    private Map<String, Integer> roomAvailability;
    private Map<String, Double> roomPrices;
    private Map<String, Queue<String>> availableRooms;

    public HotelInventory() {
        roomAvailability = new LinkedHashMap<>();
        roomPrices = new LinkedHashMap<>();
        availableRooms = new LinkedHashMap<>();

        roomAvailability.put("Standard", 2);
        roomAvailability.put("Deluxe", 2);
        roomAvailability.put("Suite", 1);

        roomPrices.put("Standard", 3000.0);
        roomPrices.put("Deluxe", 5000.0);
        roomPrices.put("Suite", 8000.0);

        Queue<String> standardRooms = new LinkedList<>();
        standardRooms.add("S101");
        standardRooms.add("S102");

        Queue<String> deluxeRooms = new LinkedList<>();
        deluxeRooms.add("D201");
        deluxeRooms.add("D202");

        Queue<String> suiteRooms = new LinkedList<>();
        suiteRooms.add("SU301");

        availableRooms.put("Standard", standardRooms);
        availableRooms.put("Deluxe", deluxeRooms);
        availableRooms.put("Suite", suiteRooms);
    }

    public Reservation confirmBooking(String reservationId, String guestName, String roomType) {
        if (!roomAvailability.containsKey(roomType)) {
            System.out.println("Booking Failed: Invalid room type.");
            return null;
        }

        if (roomAvailability.get(roomType) <= 0) {
            System.out.println("Booking Failed: No rooms available for " + roomType);
            return null;
        }

        String allocatedRoomId = availableRooms.get(roomType).poll();
        roomAvailability.put(roomType, roomAvailability.get(roomType) - 1);

        return new Reservation(reservationId, guestName, roomType, allocatedRoomId, roomPrices.get(roomType));
    }

    public void restoreRoom(String roomType, String roomId) {
        availableRooms.get(roomType).offer(roomId);
        roomAvailability.put(roomType, roomAvailability.get(roomType) + 1);
    }

    public void displayAvailability() {
        System.out.println("Current Room Availability:");
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

class BookingHistory {
    private List<Reservation> bookingList;
    private Map<String, Reservation> reservationMap;

    public BookingHistory() {
        bookingList = new ArrayList<>();
        reservationMap = new LinkedHashMap<>();
    }

    public void addBooking(Reservation reservation) {
        if (reservation != null) {
            bookingList.add(reservation);
            reservationMap.put(reservation.getReservationId(), reservation);
        }
    }

    public Reservation getReservation(String reservationId) {
        return reservationMap.get(reservationId);
    }

    public void displayAllBookings() {
        System.out.println("===== Booking History =====");
        for (Reservation reservation : bookingList) {
            System.out.println(reservation);
        }
    }
}

class CancellationService {
    private Stack<String> rollbackStack;

    public CancellationService() {
        rollbackStack = new Stack<>();
    }

    public void cancelBooking(String reservationId, BookingHistory history, HotelInventory inventory) {
        Reservation reservation = history.getReservation(reservationId);

        if (reservation == null) {
            System.out.println("Cancellation Failed: Reservation ID " + reservationId + " does not exist.");
            return;
        }

        if (!reservation.isActive()) {
            System.out.println("Cancellation Failed: Reservation ID " + reservationId + " is already cancelled.");
            return;
        }

        rollbackStack.push(reservation.getRoomId());

        inventory.restoreRoom(reservation.getRoomType(), reservation.getRoomId());

        reservation.cancel();

        System.out.println("Cancellation Successful for Reservation ID: " + reservationId);
        System.out.println("Released Room ID: " + rollbackStack.pop());
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        HotelInventory inventory = new HotelInventory();
        BookingHistory history = new BookingHistory();
        CancellationService cancellationService = new CancellationService();

        System.out.println("===== Book My Stay - Use Case 10: Booking Cancellation & Inventory Rollback =====\n");

        Reservation r1 = inventory.confirmBooking("R101", "Christopher", "Deluxe");
        Reservation r2 = inventory.confirmBooking("R102", "Aarav", "Standard");

        history.addBooking(r1);
        history.addBooking(r2);

        System.out.println("Bookings Confirmed:\n");
        history.displayAllBookings();

        System.out.println();
        inventory.displayAvailability();

        System.out.println("\n--- Processing Cancellation ---\n");
        cancellationService.cancelBooking("R101", history, inventory);

        System.out.println();
        inventory.displayAvailability();

        System.out.println("\nUpdated Booking History:\n");
        history.displayAllBookings();

        System.out.println("\n--- Invalid Cancellation Attempts ---\n");
        cancellationService.cancelBooking("R101", history, inventory);
        cancellationService.cancelBooking("R999", history, inventory);

        System.out.println("\nSystem state restored consistently after cancellation.");
    }
}