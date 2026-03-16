/**
 * Reservation class representing a guest booking request
 */
import java.util.LinkedList;
import java.util.Queue;

class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void displayReservation() {
        System.out.println("Guest Name : " + guestName);
        System.out.println("Room Type  : " + roomType);
    }
}
class BookingRequestQueue {

    private Queue<Reservation> requestQueue;

    public BookingRequestQueue() {
        requestQueue = new LinkedList<>();
    }

    // Add booking request
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // Display all requests in queue
    public void displayRequests() {

        System.out.println("\n------ Booking Request Queue ------");

        for (Reservation r : requestQueue) {
            r.displayReservation();
            System.out.println("-----------------------------");
        }
    }
}
/**
 * Use Case 5: Booking Request Queue (FIFO)
 *
 * Demonstrates first-come-first-served booking request intake
 * using a Queue data structure.
 *
 * @author Christopher Wilson
 * @version 5.0
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=========== Book My Stay ===========");
        System.out.println(" Hotel Booking System v5.0 ");
        System.out.println(" Booking Request Queue Demo ");
        System.out.println("====================================");

        // Initialize booking queue
        BookingRequestQueue bookingQueue = new BookingRequestQueue();

        // Create booking requests
        Reservation r1 = new Reservation("Alice", "Single Room");
        Reservation r2 = new Reservation("Bob", "Double Room");
        Reservation r3 = new Reservation("Charlie", "Suite Room");

        // Add requests to queue
        bookingQueue.addRequest(r1);
        bookingQueue.addRequest(r2);
        bookingQueue.addRequest(r3);

        // Display queue (FIFO order)
        bookingQueue.displayRequests();

        System.out.println("\nAll requests stored in arrival order.");
        System.out.println("No inventory updates performed at this stage.");
    }
}
