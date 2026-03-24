import java.util.*;

class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

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

class HotelInventory {
    private Map<String, Integer> roomAvailability;
    private Map<String, Double> roomPrices;

    public HotelInventory() {
        roomAvailability = new HashMap<>();
        roomPrices = new HashMap<>();

        roomAvailability.put("Standard", 2);
        roomAvailability.put("Deluxe", 2);
        roomAvailability.put("Suite", 1);

        roomPrices.put("Standard", 3000.0);
        roomPrices.put("Deluxe", 5000.0);
        roomPrices.put("Suite", 8000.0);
    }

    public void validateRoomType(String roomType) throws InvalidBookingException {
        if (!roomAvailability.containsKey(roomType)) {
            throw new InvalidBookingException("Invalid room type: " + roomType + ". Valid room types are Standard, Deluxe, Suite.");
        }
    }

    public void validateAvailability(String roomType) throws InvalidBookingException {
        int available = roomAvailability.get(roomType);
        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for room type: " + roomType);
        }
    }

    public Reservation confirmBooking(String reservationId, String guestName, String roomType) throws InvalidBookingException {
        validateRoomType(roomType);
        validateAvailability(roomType);

        int currentAvailability = roomAvailability.get(roomType);
        if (currentAvailability - 1 < 0) {
            throw new InvalidBookingException("Inventory error: Cannot reduce " + roomType + " rooms below zero.");
        }

        roomAvailability.put(roomType, currentAvailability - 1);

        return new Reservation(reservationId, guestName, roomType, roomPrices.get(roomType));
    }

    public void displayAvailability() {
        System.out.println("Current Room Availability:");
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        HotelInventory inventory = new HotelInventory();

        System.out.println("===== Book My Stay - Use Case 9: Error Handling & Validation =====\n");

        inventory.displayAvailability();
        System.out.println();

        try {
            Reservation r1 = inventory.confirmBooking("R101", "Christopher", "Deluxe");
            System.out.println("Booking Successful:");
            System.out.println(r1);
        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }

        System.out.println();

        try {
            Reservation r2 = inventory.confirmBooking("R102", "Aarav", "deluxe");
            System.out.println("Booking Successful:");
            System.out.println(r2);
        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }

        System.out.println();

        try {
            Reservation r3 = inventory.confirmBooking("R103", "Priya", "Suite");
            System.out.println("Booking Successful:");
            System.out.println(r3);
        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }

        System.out.println();

        try {
            Reservation r4 = inventory.confirmBooking("R104", "Rahul", "Suite");
            System.out.println("Booking Successful:");
            System.out.println(r4);
        } catch (InvalidBookingException e) {
            System.out.println("Booking Failed: " + e.getMessage());
        }

        System.out.println();
        System.out.println("System remains stable after handling errors.\n");

        inventory.displayAvailability();
    }
}