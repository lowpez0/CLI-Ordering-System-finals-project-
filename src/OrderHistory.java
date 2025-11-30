import java.time.LocalDateTime;
import java.util.HashMap;

public class OrderHistory {

    private String STATUS = "DELIVERED";
    private double total;
    private LocalDateTime date;
    private HashMap<Product, Integer> items;

    public OrderHistory(String STATUS, double total, LocalDateTime date, HashMap<Product, Integer> items) {
        this.STATUS = STATUS;
        this.total = total;
        this.date = date;
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
