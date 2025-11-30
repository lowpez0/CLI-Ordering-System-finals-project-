import java.time.LocalDateTime;
import java.util.HashMap;

public class OrderHistory {

    private String STATUS = "DELIVERED";
    private double total;
    private LocalDateTime date;
    private HashMap<Product, Integer> items;

    public OrderHistory(double total, HashMap<Product, Integer> items) {
        this.total = total;
        this.date = LocalDateTime.now();
        this.items = items;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public double getTotal() {
        return total;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public HashMap<Product, Integer> getItems() {
        return items;
    }
}
