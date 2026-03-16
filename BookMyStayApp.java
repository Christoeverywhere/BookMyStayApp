import java.util.HashMap;
import java.util.Map;

/**
 * RoomInventory class manages centralized room availability
 * using a HashMap.
 *
 * @author Christopher Wilson
 * @version 3.0
 */
class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor to initialize inventory
    public RoomInventory() {
        inventory = new HashMap<>();

        // Register room types with availability
        inventory.put("Single Room", 10);
        inventory.put("Double Room", 7);
        inventory.put("Suite Room", 3);
    }

    // Method to get availability
    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Method to update availability
    public void updateAvailability(String roomType, int newCount) {
        inventory.put(roomType, newCount);
    }

    // Display full inventory
    public void displayInventory() {

        System.out.println("\n------ Current Room Inventory ------");

        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}
/**
 * Use Case 3: Centralized Room Inventory Management
 *
 * Demonstrates how HashMap provides a single source of truth
 * for managing room availability.
 *
 * @author Christopher Wilson
 * @version 3.1
 */

public class BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("=========== Book My Stay ===========");
        System.out.println(" Hotel Booking System v3.1 ");
        System.out.println(" Centralized Inventory Demo ");
        System.out.println("====================================");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display current inventory
        inventory.displayInventory();

        // Example availability lookup
        System.out.println("\nChecking availability for Single Room...");
        int available = inventory.getAvailability("Single Room");
        System.out.println("Available Single Rooms: " + available);

        // Example update
        System.out.println("\nUpdating inventory...");
        inventory.updateAvailability("Single Room", 8);

        // Display updated inventory
        inventory.displayInventory();

        System.out.println("\nInventory operations completed successfully.");
    }
}