import com.jjjwelectronics.scanner.*;
import java.util.*;

// Represents an item in the store with details
class Item {
    private String name;
    private double price;

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

}


    // Represents a user's shopping session
    class ShoppingSession {
        private List<Item> items = new ArrayList<>();

        public void addItem(Item item) {
            items.add(item);
        }

        // Get the total price of all items in the session
        public double getTotalPrice() {
            double total = 0.0;
            for (Item item : items) {
                total += item.getPrice();
            }
            return total;
        }

        // Get the count of items added to the session
        public int getItemsCount() {
            return items.size();
        }

        // Get a list of all items in the session
        public List<Item> getItems() {
            return new ArrayList<>(items);  // Return a copy to prevent external modifications
        }

        // Optional: Get a summary of the session (useful for displaying in a console or GUI)
        public String getSessionSummary() {
            StringBuilder summary = new StringBuilder();
            for (Item item : items) {
                summary.append(item.getName()).append(" - $").append(item.getPrice()).append("\n");
            }
            summary.append("Total: $").append(getTotalPrice());
            return summary.toString();
        }
    }

// Simple data store using a HashMap
class DataStore {
    private Map<String, Item> items = new HashMap<>();

    public DataStore() {
        // Sample data: In a real-world scenario, this data would come from a database or other data sources
        items.put("123456", new Item("Apple", 0.50));
        items.put("789012", new Item("Banana", 0.30));
        // ... add more items as needed ...
    }

    public Item lookupItemByBarcode(String barcode) {
        return items.get(barcode);
    }
}

// Implementation of BarcodeScannerListener
class BarcodeScannerListenerImpl implements BarcodeScannerListener {
    private DataStore dataStore;
    private ShoppingSession shoppingSession;

    public BarcodeScannerListenerImpl(DataStore dataStore, ShoppingSession shoppingSession) {
        this.dataStore = dataStore;
        this.shoppingSession = shoppingSession;
    }

    @Override
    public void aBarcodeHasBeenScanned(IBarcodeScanner barcodeScanner, Barcode barcode) {
        Item item = dataStore.lookupItemByBarcode(barcode.toString());
        if (item != null) {
            shoppingSession.addItem(item);
            System.out.println(item.getName() + " added to the cart!");
        } else {
            System.out.println("Item not found for barcode: " + barcode);
        }
    }
}

public class Main {
    public static void main(String[] args) {
        // Initialize components
        DataStore dataStore = new DataStore();
        ShoppingSession shoppingSession = new ShoppingSession();
        BarcodeScanner barcodeScanner = new BarcodeScanner();
        BarcodeScannerListenerImpl listener = new BarcodeScannerListenerImpl(dataStore, shoppingSession);

        // Simulate scanning items
        barcodeScanner.scan(new BarcodedItem(new Barcode(new Numeral[]{Numeral.ONE, Numeral.TWO, Numeral.THREE, Numeral.FOUR, Numeral.FIVE, Numeral.SIX}))); // Should add "Apple"
        barcodeScanner.scan(new BarcodedItem(new Barcode(new Numeral[]{Numeral.SEVEN, Numeral.EIGHT, Numeral.NINE, Numeral.ZERO, Numeral.ONE, Numeral.TWO}))); // Should add "Banana"

        // ... continue with other operations like calculating total, etc. ...
    }
}
