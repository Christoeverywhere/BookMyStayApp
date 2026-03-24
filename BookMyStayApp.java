import java.util.*;

class BookingRequest {
    private String requestId;
    private String guestName;
    private String roomType;

    public BookingRequest(String requestId, String guestName, String roomType) {
        this.requestId = requestId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;
    private String roomId;

    public Reservation(String reservationId, String guestName, String roomType, String roomId) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.roomId = roomId;
    }

    public String toString() {
        return "Reservation ID: " + reservationId +
                ", Guest Name: " + guestName +
                ", Room Type: " + roomType +
                ", Room ID: " + roomId;
    }
}

class BookingQueue {
    private Queue<BookingRequest> requestQueue;

    public BookingQueue() {
        requestQueue = new LinkedList<>();
    }

    public synchronized void addRequest(BookingRequest request) {
        requestQueue.offer(request);
        System.out.println("Request Added: " + request.getRequestId() + " by " + request.getGuestName());
    }

    public synchronized BookingRequest getNextRequest() {
        return requestQueue.poll();
    }

    public synchronized boolean isEmpty() {
        return requestQueue.isEmpty();
    }
}

class HotelInventory {
    private Map<String, Integer> roomAvailability;
    private Map<String, Queue<String>> availableRooms;

    public HotelInventory() {
        roomAvailability = new LinkedHashMap<>();
        availableRooms = new LinkedHashMap<>();

        roomAvailability.put("Standard", 2);
        roomAvailability.put("Deluxe", 2);
        roomAvailability.put("Suite", 1);

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

    public synchronized Reservation allocateRoom(BookingRequest request) {
        String roomType = request.getRoomType();

        if (!roomAvailability.containsKey(roomType)) {
            System.out.println(Thread.currentThread().getName() + " -> Booking Failed for " + request.getGuestName() + ": Invalid room type.");
            return null;
        }

        if (roomAvailability.get(roomType) <= 0) {
            System.out.println(Thread.currentThread().getName() + " -> Booking Failed for " + request.getGuestName() + ": No " + roomType + " rooms available.");
            return null;
        }

        String roomId = availableRooms.get(roomType).poll();
        roomAvailability.put(roomType, roomAvailability.get(roomType) - 1);

        Reservation reservation = new Reservation(request.getRequestId(), request.getGuestName(), roomType, roomId);

        System.out.println(Thread.currentThread().getName() + " -> Booking Confirmed: " + reservation);

        return reservation;
    }

    public synchronized void displayAvailability() {
        System.out.println("\nFinal Room Availability:");
        for (Map.Entry<String, Integer> entry : roomAvailability.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

class ConcurrentBookingProcessor extends Thread {
    private BookingQueue bookingQueue;
    private HotelInventory inventory;

    public ConcurrentBookingProcessor(String threadName, BookingQueue bookingQueue, HotelInventory inventory) {
        super(threadName);
        this.bookingQueue = bookingQueue;
        this.inventory = inventory;
    }

    public void run() {
        while (true) {
            BookingRequest request;

            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                request = bookingQueue.getNextRequest();
            }

            if (request != null) {
                inventory.allocateRoom(request);
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println(getName() + " interrupted.");
            }
        }
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        BookingQueue bookingQueue = new BookingQueue();
        HotelInventory inventory = new HotelInventory();

        System.out.println("===== Book My Stay - Use Case 11: Concurrent Booking Simulation =====\n");

        bookingQueue.addRequest(new BookingRequest("R101", "Christopher", "Deluxe"));
        bookingQueue.addRequest(new BookingRequest("R102", "Aarav", "Standard"));
        bookingQueue.addRequest(new BookingRequest("R103", "Priya", "Suite"));
        bookingQueue.addRequest(new BookingRequest("R104", "Rahul", "Suite"));
        bookingQueue.addRequest(new BookingRequest("R105", "Sneha", "Deluxe"));
        bookingQueue.addRequest(new BookingRequest("R106", "Kiran", "Standard"));

        System.out.println();

        ConcurrentBookingProcessor processor1 = new ConcurrentBookingProcessor("Processor-1", bookingQueue, inventory);
        ConcurrentBookingProcessor processor2 = new ConcurrentBookingProcessor("Processor-2", bookingQueue, inventory);
        ConcurrentBookingProcessor processor3 = new ConcurrentBookingProcessor("Processor-3", bookingQueue, inventory);

        processor1.start();
        processor2.start();
        processor3.start();

        try {
            processor1.join();
            processor2.join();
            processor3.join();
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted.");
        }

        inventory.displayAvailability();

        System.out.println("\nSystem completed concurrent allocations without conflicts or double-booking.");
    }
}