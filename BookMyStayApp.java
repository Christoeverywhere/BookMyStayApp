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
}

class AddOnService {
    private String serviceName;
    private double serviceCost;

    public AddOnService(String serviceName, double serviceCost) {
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
    }

    public String getServiceName() {
        return serviceName;
    }

    public double getServiceCost() {
        return serviceCost;
    }

    public String toString() {
        return serviceName + " (Rs." + serviceCost + ")";
    }
}

class AddOnServiceManager {
    private Map<String, List<AddOnService>> reservationServices;

    public AddOnServiceManager() {
        reservationServices = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {
        if (!reservationServices.containsKey(reservationId)) {
            reservationServices.put(reservationId, new ArrayList<>());
        }
        reservationServices.get(reservationId).add(service);
    }

    public List<AddOnService> getServices(String reservationId) {
        if (reservationServices.containsKey(reservationId)) {
            return reservationServices.get(reservationId);
        } else {
            return new ArrayList<>();
        }
    }

    public double calculateAdditionalCost(String reservationId) {
        double total = 0;
        List<AddOnService> services = getServices(reservationId);
        for (AddOnService service : services) {
            total += service.getServiceCost();
        }
        return total;
    }

    public double calculateFinalCost(Reservation reservation) {
        return reservation.getRoomCost() + calculateAdditionalCost(reservation.getReservationId());
    }
}

public class BookMyStayApp {
    public static void main(String[] args) {
        Reservation reservation = new Reservation("R101", "Christopher", "Deluxe Room", 5000);

        AddOnService breakfast = new AddOnService("Breakfast", 500);
        AddOnService airportPickup = new AddOnService("Airport Pickup", 1200);
        AddOnService spaAccess = new AddOnService("Spa Access", 1500);

        AddOnServiceManager manager = new AddOnServiceManager();

        manager.addService(reservation.getReservationId(), breakfast);
        manager.addService(reservation.getReservationId(), airportPickup);
        manager.addService(reservation.getReservationId(), spaAccess);

        System.out.println("===== Book My Stay - Use Case 7: Add-On Service Selection =====");
        System.out.println("Reservation ID : " + reservation.getReservationId());
        System.out.println("Guest Name     : " + reservation.getGuestName());
        System.out.println("Room Type      : " + reservation.getRoomType());
        System.out.println("Base Room Cost : Rs." + reservation.getRoomCost());

        System.out.println("\nSelected Add-On Services:");
        List<AddOnService> selectedServices = manager.getServices(reservation.getReservationId());
        for (AddOnService service : selectedServices) {
            System.out.println("- " + service.getServiceName() + " : Rs." + service.getServiceCost());
        }

        double additionalCost = manager.calculateAdditionalCost(reservation.getReservationId());
        double finalCost = manager.calculateFinalCost(reservation);

        System.out.println("\nAdditional Cost: Rs." + additionalCost);
        System.out.println("Final Total Cost: Rs." + finalCost);
        System.out.println("\nCore booking and inventory remain unchanged.");
    }
}