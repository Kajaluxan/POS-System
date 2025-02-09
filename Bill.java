import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

class GloceryItem implements Serializable {
    private int item_code;
    private String itemName;
    private double price;
    private LocalDate Manufacturing_Date;
    private LocalDate Expiry_Date;
    private double Discount;

    public GloceryItem(String itemName, int item_code, double price, LocalDate Manufacturing_Date, LocalDate Expiry_Date, double Discount) {
        this.itemName = itemName;
        this.item_code = item_code;
        this.price = price;
        this.Manufacturing_Date = Manufacturing_Date;
        this.Expiry_Date = Expiry_Date;
        this.Discount = Discount;
    }

    public int getItem_code() {
        return item_code;
    }

    public void setItem_code(int item_code) {
        this.item_code = item_code;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getManufacturing_Date() {
        return Manufacturing_Date;
    }

    public void setManufacturing_Date(LocalDate manufacturing_Date) {
        Manufacturing_Date = manufacturing_Date;
    }

    public LocalDate getExpiry_Date() {
        return Expiry_Date;
    }

    public void setExpiry_Date(LocalDate expiry_Date) {
        Expiry_Date = expiry_Date;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    // Getters and setters
    
}

class ItemCodeNotFound extends Exception {
    // Custom exception class for handling item code not found scenario
    public ItemCodeNotFound(String s) {
        super(s);
    }
}

class POS implements Serializable {
    // Attributes of the POS system
    private String Cashier_Name;
    private String Branch;
    private String Customer_Name;
    private double Netprice;
    private double Total_price;
    private double Total_discount;
    private LocalDate Current_date;
    private LocalTime Current_time;
    private List<GloceryItem> itemsList;
    private List<Integer> quantities;
    private List<Double> net;
    private static Map<Integer, GloceryItem> dictionary = new HashMap<>();

    public static Map<Integer, GloceryItem> getDictionary() {
        return dictionary;
    }

    public GloceryItem getItemDetails() {
        GloceryItem item = null;
        try {
            InputStreamReader r = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(r);
            int item_code = Integer.parseInt(br.readLine());
            item = dictionary.get(item_code);
            if (item == null) {
                throw new ItemCodeNotFound("Item code not found: " + item_code);
            }
            br.close();
            r.close();
        } catch (IOException | NumberFormatException | ItemCodeNotFound e) {
            e.printStackTrace();
        }
        return item;
    }

    public void savePendingBill(String filename) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
            outputStream.writeObject(this);
            System.out.println("Pending bill saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving pending bill: " + e.getMessage());
        }
    }

    public static POS loadPendingBill(String filename) {
        POS pos = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            pos = (POS) inputStream.readObject();
            System.out.println("Pending bill loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading pending bill: " + e.getMessage());
        }
        return pos;
    }

    public POS(String Cashier_Name, String Branch, String Customer_Name) {
        this.Cashier_Name = Cashier_Name;
        this.Branch = Branch;
        this.Customer_Name = Customer_Name;
        this.Netprice = 0.0;
        this.Total_price = 0.0;
        this.Total_discount = 0.0;
        this.Current_date = LocalDate.now();
        this.Current_time = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        this.itemsList = new ArrayList<>();
        this.quantities = new ArrayList<>();
        this.net = new ArrayList<>();
    }

    public void addItem(GloceryItem item, int quantity) {
        double netcost = (item.getPrice() - ((item.getDiscount() / 100) * item.getPrice())) * quantity;
        itemsList.add(item);
        net.add((netcost) / quantity);
        Total_discount += ((item.getDiscount() / 100) * (item.getPrice() * quantity));
        Total_price += netcost;
        quantities.add(quantity);
    }

    public void printBill() {
        System.out.println("**************************************************************************************");
        System.out.println("                                YOUR SHOPPING BILL                         ");
        System.out.println("**************************************************************************************");
        System.out.println("Cashier Name: " + Cashier_Name);
        System.out.println("Branch: " + Branch);
        System.out.println("Customer Name: " + Customer_Name);
        System.out.println("Date: " + Current_date);
        System.out.println("Time: " + Current_time);
        System.out.println("**************************************************************************************");
        System.out.printf("%-25s %-15s %-15s %-15s %-10s\n", "Item Name", "Unit Cost", "Discount", "Net Price", "Quantity");
        System.out.println("--------------------------------------------------------------------------------------");
        for (int i = 0; i < itemsList.size(); i++) {
            GloceryItem item = itemsList.get(i);
            int quantity = quantities.get(i);
            double netprice = net.get(i);
            System.out.printf("%-25s %-14.2f %-14s %-14.2f %-10d\n", item.getItemName(), item.getPrice(), item.getDiscount() + "%", netprice, quantity);
        }
        System.out.println("--------------------------------------------------------------------------------------");
        System.out.printf("%-60s %-10.2f\n", "Total Discount:", Total_discount);
        System.out.printf("%-60s %-10.2f\n", "Total Cost:", Total_price);
        System.out.println("**************************************************************************************");
        System.out.println("                              THANK YOU FOR SHOPPING WITH US!                 ");
        System.out.println("**************************************************************************************");
    }
    



    // Method to add items from another POS object
    public void addItemsFrom(POS pendingBill) {
        this.itemsList.addAll(pendingBill.itemsList);
        this.quantities.addAll(pendingBill.quantities);
        this.net.addAll(pendingBill.net);
        this.Total_discount += pendingBill.Total_discount;
        this.Total_price += pendingBill.Total_price;
    }
}

public class Bill {
    public static void main(String[] args) {
        GloceryItem set1 = new GloceryItem("Milo", 10, 150.0, LocalDate.now(), LocalDate.now().plusDays(7), 5.0);
        GloceryItem set2 = new GloceryItem("Anchor", 11, 1000.0, LocalDate.now(), LocalDate.now().plusDays(7), 10.0);
        GloceryItem set3 = new GloceryItem("Rice", 12, 250.0, LocalDate.now(), LocalDate.now().plusDays(7), 2.0);
        GloceryItem set4 = new GloceryItem("Oil", 13, 800.0, LocalDate.now(), LocalDate.now().plusDays(300), 1.0);
        GloceryItem set5 = new GloceryItem("Noodles", 14, 130.0, LocalDate.now(), LocalDate.now().plusDays(45), 0.5);

        // Set key-value pairs for items
        POS.getDictionary().put(10, set1);
        POS.getDictionary().put(11, set2);
        POS.getDictionary().put(12, set3);
        POS.getDictionary().put(13, set4);
        POS.getDictionary().put(14, set5);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Cashier Name: ");
        String cashier_Name = scanner.nextLine();
        System.out.print("Enter Branch: ");
        String branch = scanner.nextLine();
        System.out.print("Enter Customer Name: ");
        String customerName = scanner.nextLine();

        POS pos = new POS(cashier_Name, branch, customerName);
        boolean exit = false;
        boolean print = true;
        while (!exit) {
           
            System.out.print("Enter Item Code and No of Items Customer Buys (enter 'p' to save and exit, 'r' to load pending bill, 'e' to print the bill): ");
            String input = scanner.next();
            
            switch (input.toLowerCase()) {
                case "p":
                    pos.savePendingBill("pending_bill.txt");
                    System.out.println("Pending bill saved. Exiting...");
                    print=false;
                    exit = true;
                    break;
                case "r":
                    POS pendingBill = POS.loadPendingBill("pending_bill.txt");
                    if (pendingBill != null) {
                        pos.addItemsFrom(pendingBill); // Add items from the pending bill
                        System.out.println("Pending bill loaded successfully.");
                    } else {
                        System.out.println("Error loading pending bill.");
                    }
                    break;
                case "e":
                    exit = true;
                    break;
                default:
                    int userInput1 = Integer.parseInt(input);
                    int userInput2 = scanner.nextInt();

                    GloceryItem item = POS.getDictionary().get(userInput1);

                    if (item != null) {
                        pos.addItem(item, userInput2);
                    } else {
                        System.out.println("Invalid item code entered: " + userInput1);
                    }
                    break;

            }
        }

        // Close the scanner
        scanner.close();

        // Print the bil
        if(print==true){pos.printBill();}
    }
}
