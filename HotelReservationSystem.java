1
import java.io.*;
import java.util.*;

class Room {
    int roomNumber;
    String category;
    double price;
    boolean isBooked;

    Room(int roomNumber, String category, double price, boolean isBooked) {
        this.roomNumber = roomNumber;
        this.category = category;
        this.price = price;
        this.isBooked = isBooked;
    }

    @Override
    public String toString() {
        return roomNumber + "," + category + "," + price + "," + isBooked;
    }
}

class Booking {
    String customerName;
    int roomNumber;
    String category;
    double amount;

    Booking(String customerName, int roomNumber, String category, double amount) {
        this.customerName = customerName;
        this.roomNumber = roomNumber;
        this.category = category;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return customerName + "," + roomNumber + "," + category + "," + amount;
    }
}

public class HotelReservationSystem {

    static ArrayList<Room> rooms = new ArrayList<>();
    static ArrayList<Booking> bookings = new ArrayList<>();

    static final String ROOM_FILE = "rooms.txt";
    static final String BOOKING_FILE = "bookings.txt";

    public static void main(String[] args) {

        loadRooms();
        loadBookings();

        Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.println("\n===== HOTEL RESERVATION SYSTEM =====");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Cancel Booking");
            System.out.println("4. View Booking Details");
            System.out.println("5. Exit");
            System.out.print("Enter Choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    viewAvailableRooms();
                    break;

                case 2:
                    bookRoom(sc);
                    break;

                case 3:
                    cancelBooking(sc);
                    break;

                case 4:
                    viewBookings();
                    break;

                case 5:
                    saveRooms();
                    saveBookings();
                    System.out.println("Thank You!");
                    System.exit(0);

                default:
                    System.out.println("Invalid Choice!");
            }
        }
    }

    static void loadRooms() {

        File file = new File(ROOM_FILE);

        if (!file.exists()) {

            rooms.add(new Room(101, "Standard", 2000, false));
            rooms.add(new Room(102, "Deluxe", 3500, false));
            rooms.add(new Room(103, "Suite", 5000, false));

            saveRooms();
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                rooms.add(new Room(
                        Integer.parseInt(data[0]),
                        data[1],
                        Double.parseDouble(data[2]),
                        Boolean.parseBoolean(data[3])
                ));
            }

        } catch (Exception e) {
            System.out.println("Error loading rooms.");
        }
    }

    static void loadBookings() {

        File file = new File(BOOKING_FILE);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                bookings.add(new Booking(
                        data[0],
                        Integer.parseInt(data[1]),
                        data[2],
                        Double.parseDouble(data[3])
                ));
            }

        } catch (Exception e) {
            System.out.println("Error loading bookings.");
        }
    }

    static void viewAvailableRooms() {

        System.out.println("\nAvailable Rooms:");

        for (Room room : rooms) {

            if (!room.isBooked) {

                System.out.println("Room No: " + room.roomNumber +
                        " | Category: " + room.category +
                        " | Price: ₹" + room.price);
            }
        }
    }

    static void bookRoom(Scanner sc) {

        System.out.print("Enter Customer Name: ");
        sc.nextLine();
        String name = sc.nextLine();

        viewAvailableRooms();

        System.out.print("Enter Room Number to Book: ");
        int roomNo = sc.nextInt();

        for (Room room : rooms) {

            if (room.roomNumber == roomNo && !room.isBooked) {

                room.isBooked = true;

                System.out.println("Payment Successful!");

                bookings.add(new Booking(
                        name,
                        room.roomNumber,
                        room.category,
                        room.price
                ));

                saveRooms();
                saveBookings();

                System.out.println("Room Booked Successfully!");
                return;
            }
        }

        System.out.println("Room not available!");
    }

    static void cancelBooking(Scanner sc) {

        System.out.print("Enter Room Number to Cancel Booking: ");
        int roomNo = sc.nextInt();

        Iterator<Booking> iterator = bookings.iterator();

        while (iterator.hasNext()) {

            Booking booking = iterator.next();

            if (booking.roomNumber == roomNo) {

                iterator.remove();

                for (Room room : rooms) {

                    if (room.roomNumber == roomNo) {
                        room.isBooked = false;
                    }
                }

                saveRooms();
                saveBookings();

                System.out.println("Booking Cancelled Successfully!");
                return;
            }
        }

        System.out.println("Booking not found!");
    }

    static void viewBookings() {

        System.out.println("\nBooking Details:");

        for (Booking booking : bookings) {

            System.out.println("Customer: " + booking.customerName +
                    " | Room No: " + booking.roomNumber +
                    " | Category: " + booking.category +
                    " | Amount: ₹" + booking.amount);
        }
    }

    static void saveRooms() {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ROOM_FILE))) {

            for (Room room : rooms) {

                bw.write(room.toString());
                bw.newLine();
            }

        } catch (Exception e) {
            System.out.println("Error saving rooms.");
        }
    }

    static void saveBookings() {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(BOOKING_FILE))) {

            for (Booking booking : bookings) {

                bw.write(booking.toString());
                bw.newLine();
            }

        } catch (Exception e) {
            System.out.println("Error saving bookings.");
        }
    }
}