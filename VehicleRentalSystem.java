import java.util.*;
import java.util.Objects;

// Abstract Base Class
abstract class Vehicle {
    private String vehicleId;
    private String model;
    private double baseRentalRate;
    private boolean available;

    public Vehicle(String vehicleId, String model, double baseRentalRate) {
        if (vehicleId == null || vehicleId.isEmpty() || baseRentalRate <= 0) {
            throw new IllegalArgumentException("Invalid vehicle details");
        }
        this.vehicleId = vehicleId;
        this.model = model;
        this.baseRentalRate = baseRentalRate;
        this.available = true;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getModel() {
        return model;
    }

    public double getBaseRentalRate() {
        return baseRentalRate;
    }

    public boolean isAvailableForRental() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public abstract double calculateRentalCost(int days);

    @Override
    public String toString() {
        return "Vehicle ID: " + vehicleId + ", Model: " + model + ", Base Rate: " + baseRentalRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return vehicleId.equals(vehicle.vehicleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vehicleId);
    }
}

// Concrete Vehicle Classes
class Car extends Vehicle {
    private int seatingCapacity;
    private boolean hasGPS;

    public Car(String vehicleId, String model, double baseRentalRate, int seatingCapacity, boolean hasGPS) {
        super(vehicleId, model, baseRentalRate);
        if (seatingCapacity <= 0) {
            throw new IllegalArgumentException("Invalid seating capacity");
        }
        this.seatingCapacity = seatingCapacity;
        this.hasGPS = hasGPS;
    }

    @Override
    public double calculateRentalCost(int days) {
        double cost = getBaseRentalRate() * days;
        if (hasGPS) {
            cost += 10 * days;
        }
        return cost;
    }

    @Override
    public String toString() {
        return super.toString() + ", Seating Capacity: " + seatingCapacity + ", GPS: " + (hasGPS ? "Yes" : "No");
    }
}

class Motorcycle extends Vehicle {
    private boolean hasCarrier;

    public Motorcycle(String vehicleId, String model, double baseRentalRate, boolean hasCarrier) {
        super(vehicleId, model, baseRentalRate);
        this.hasCarrier = hasCarrier;
    }

    @Override
    public double calculateRentalCost(int days) {
        double cost = getBaseRentalRate() * days;
        if (!hasCarrier) {
            cost -= 5 * days;
        }
        return cost;
    }

    @Override
    public String toString() {
        return super.toString() + ", Carrier: " + (hasCarrier ? "Yes" : "No");
    }
}

class Truck extends Vehicle {
    private double loadCapacity;

    public Truck(String vehicleId, String model, double baseRentalRate, double loadCapacity) {
        super(vehicleId, model, baseRentalRate);
        if (loadCapacity <= 0) {
            throw new IllegalArgumentException("Invalid load capacity");
        }
        this.loadCapacity = loadCapacity;
    }

    @Override
    public double calculateRentalCost(int days) {
        return getBaseRentalRate() * days + loadCapacity * 2 * days;
    }

    @Override
    public String toString() {
        return super.toString() + ", Load Capacity: " + loadCapacity + " tons";
    }
}

// Supporting Classes
class Customer {
    private String customerId;
    private String name;

    public Customer(String customerId, String name) {
        if (customerId == null || customerId.isEmpty() || name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Invalid customer details");
        }
        this.customerId = customerId;
        this.name = name;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Customer ID: " + customerId + ", Name: " + name;
    }
}

class RentalTransaction {
    private Vehicle vehicle;
    private Customer customer;
    private int days;
    private double cost;

    public RentalTransaction(Vehicle vehicle, Customer customer, int days) {
        if (vehicle == null || customer == null || days <= 0) {
            throw new IllegalArgumentException("Invalid transaction details");
        }
        this.vehicle = vehicle;
        this.customer = customer;
        this.days = days;
        this.cost = vehicle.calculateRentalCost(days);
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getDays() {
        return days;
    }

    public double getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Transaction: [Vehicle: " + vehicle + ", Customer: " + customer + ", Days: " + days + ", Cost: " + cost + "]";
    }
}

class RentalAgency {
    private List<Vehicle> vehicles;
    private List<RentalTransaction> transactions;

    public RentalAgency() {
        vehicles = new ArrayList<>();
        transactions = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public boolean rentVehicle(String vehicleId, Customer customer, int days) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleId().equals(vehicleId) && vehicle.isAvailableForRental()) {
                vehicle.setAvailable(false);
                transactions.add(new RentalTransaction(vehicle, customer, days));
                return true;
            }
        }
        return false;
    }

    public boolean returnVehicle(String vehicleId) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getVehicleId().equals(vehicleId) && !vehicle.isAvailableForRental()) {
                vehicle.setAvailable(true);
                return true;
            }
        }
        return false;
    }

    public List<RentalTransaction> getTransactions() {
        return transactions;
    }
}

// Testing the System
public class VehicleRentalSystem {
    public static void main(String[] args) {
        RentalAgency agency = new RentalAgency();

        // Adding vehicles
        Vehicle car = new Car("C001", "Toyota Camry", 50, 5, true);
        Vehicle bike = new Motorcycle("M001", "Yamaha R15", 20, true);
        Vehicle truck = new Truck("T001", "Volvo FH", 100, 10);

        agency.addVehicle(car);
        agency.addVehicle(bike);
        agency.addVehicle(truck);

        // Adding customers
        Customer customer1 = new Customer("CU001", "John Doe");
        Customer customer2 = new Customer("CU002", "Jane Smith");

        // Renting vehicles
        System.out.println("Renting Car: " + agency.rentVehicle("C001", customer1, 3));
        System.out.println("Renting Bike: " + agency.rentVehicle("M001", customer2, 5));

        // Returning vehicles
        System.out.println("Returning Car: " + agency.returnVehicle("C001"));

        // Printing transactions
        for (RentalTransaction transaction : agency.getTransactions()) {
            System.out.println(transaction);
        }
    }
}

