/**
 * Abstract Room class representing common properties of all room types
 */
abstract class Room {

    protected String roomType;
    protected int beds;
    protected double price;

    // Constructor
    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    // Method to display room details
    public void displayRoomDetails() {
        System.out.println("Room Type : " + roomType);
        System.out.println("Beds      : " + beds);
        System.out.println("Price     : $" + price);
    }
}
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 100.0);
    }
}
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 180.0);
    }
}
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 350.0);
    }
}
/**
 * Book My Stay App
 * Hotel Booking Management System
 *
 * @author Christopher Wilson
 * @version 1.0
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=========== Book My Stay ===========");
        System.out.println(" Hotel Booking System v1.0 ");
        System.out.println("====================================");

        // Create room objects (Polymorphism)
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Static availability variables
        int singleAvailable = 10;
        int doubleAvailable = 7;
        int suiteAvailable = 3;

        System.out.println("\n--- Room Details ---");

        single.displayRoomDetails();
        System.out.println("Available : " + singleAvailable);
        System.out.println();

        doubleRoom.displayRoomDetails();
        System.out.println("Available : " + doubleAvailable);
        System.out.println();

        suite.displayRoomDetails();
        System.out.println("Available : " + suiteAvailable);
        System.out.println();

        System.out.println("Thank you for using Book My Stay!");
    }
}