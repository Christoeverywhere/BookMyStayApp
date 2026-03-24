import java.io.*;
import java.util.*;

class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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

    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest Name: " + guestName +
                ", Room Type: " + roomType +
                ", Room ID: " + roomId +
                ", Room Cost: Rs." + roomCost +
                ", Status: " + (active ? "Confirmed" : "Cancelled");
    }
}

class HotelInventory implements Serializable {
    private static final long serialVersionUID = 1L;

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
        standardRooms.offer("S101");
        standardRooms.offer("S102");

        Queue<String> deluxeRooms = new LinkedList<>();
        deluxeRooms.offer("D201");
        deluxeRooms.offer("D202");

        Queue<String> suiteRooms = new LinkedList<>();
        suiteRooms.offer("SU301");

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

    public void displayAvailability() {
        System.out.println("Current Room Availability:");
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

class BookingHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Reservation> bookingList;

    public BookingHistory() {
        bookingList = new ArrayList<>();
    }

    public void addBooking(Reservation reservation) {
        if (reservation != null) {
            bookingList.add(reservation);
        }
    }

    public List<Reservation> getAllBookings() {
        return bookingList;
    }

    public void displayAllBookings() {
        System.out.println("===== Booking History =====");
        if (bookingList.isEmpty()) {
            System.out.println("No bookings available.");
            return;
        }

        for (Reservation reservation : bookingList) {
            System.out.println(reservation);
        }
    }
}

class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    private HotelInventory inventory;
    private BookingHistory bookingHistory;

    public SystemState(HotelInventory inventory, BookingHistory bookingHistory) {
        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
    }

    public HotelInventory getInventory() {
        return inventory;
    }

    public BookingHistory getBookingHistory() {
        return bookingHistory;
    }
}

class PersistenceService {
    private String fileName;

    public PersistenceService(String fileName) {
        this.fileName = fileName;
    }

    public void saveState(SystemState state) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName));
            outputStream.writeObject(state);
            outputStream.close();
            System.out.println("System state saved successfully to file: " + fileName);
        } catch (IOException e) {
            System.out.println("Error while saving system state: " + e.getMessage());
        }
    }

    public SystemState loadState() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName));
            SystemState state = (SystemState) inputStream.readObject();
            inputStream.close();
            System.out.println("System state loaded successfully from file: " + fileName);
            return state;
        } catch (FileNotFoundException e) {
            System.out.println("Persistence file not found. Starting with a fresh system state.");
        } catch (IOException e) {
            System.out.println("Persistence file is corrupted or unreadable. Starting with a fresh system state.");
        } catch (ClassNotFoundException e) {
            System.out.println("Stored data format is invalid. Starting with a fresh system state.");
        }

        return new SystemState(new HotelInventory(), new BookingHistory());
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        String fileName = "hotel_state.dat";
        PersistenceService persistenceService = new PersistenceService(fileName);

        System.out.println("===== Book My Stay - Use Case 12: Data Persistence & System Recovery =====\n");

        HotelInventory inventory = new HotelInventory();
        BookingHistory bookingHistory = new BookingHistory();

        Reservation r1 = inventory.confirmBooking("R101", "Christopher", "Deluxe");
        Reservation r2 = inventory.confirmBooking("R102", "Aarav", "Standard");

        bookingHistory.addBooking(r1);
        bookingHistory.addBooking(r2);

        System.out.println("Before Saving State:\n");
        bookingHistory.displayAllBookings();
        System.out.println();
        inventory.displayAvailability();

        System.out.println("\n--- Saving System State Before Shutdown ---\n");
        SystemState currentState = new SystemState(inventory, bookingHistory);
        persistenceService.saveState(currentState);

        System.out.println("\n--- Simulating System Restart ---\n");

        SystemState recoveredState = persistenceService.loadState();

        HotelInventory recoveredInventory = recoveredState.getInventory();
        BookingHistory recoveredBookingHistory = recoveredState.getBookingHistory();

        System.out.println("\nAfter Recovery:\n");
        recoveredBookingHistory.displayAllBookings();
        System.out.println();
        recoveredInventory.displayAvailability();

        System.out.println("\nSystem resumed successfully with recovered state.");
    }
}