import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

public class OrderingSystemService {
////  pc path for products.csv
  private final String PRODUCT_CSV_FILE = "C:\\Users\\sadsc\\IdeaProjects\\CLI-Ordering-System-finals-project-\\src\\products.csv";
////  laptop path for products.csv
////    private final String PRODUCT_CSV_FILE = "C:\\Users\\dhan\\IdeaProjects\\CLI Ordering System\\src\\products.csv";

    private final HashMap<String, ArrayList<Product>> productsMap = new HashMap<>();
    private final HashMap<String, String> vouchers = new LinkedHashMap<>();
    private final HashMap<Product, Integer> cart = new LinkedHashMap<>();
    private final List<OrderHistory> orderHistoryList = new ArrayList<>();

    public OrderingSystemService() {
        populateMapOfProducts();
        populateMapOfVouchers();
    }

    private void populateMapOfVouchers() {
        vouchers.put("BUNDLE10", "- 10% off when purchasing CPU + MOTHERBOARD + MEMORY together.");
        vouchers.put("BIGSPENDER", "- ₱1,000 off orders over ₱20,000");
        vouchers.put("FULLBUILD15", "- 15% off when buying all 6 core components (CPU, Motherboard, Memory, Storage, PSU, Case)");
        vouchers.put("GAMEREADY", "- 12% off when buying GPU + Gaming Monitor");
        vouchers.put("DOUBLESTORAGE", "- ₱200 off orders when buying 2 or more storage devices");
    }

    ////populates productsMap with data from products.csv
    private void populateMapOfProducts()  {
        try(BufferedReader br = new BufferedReader(new FileReader(PRODUCT_CSV_FILE))) {
          String line;
          while((line = br.readLine()) != null) {
              /////format of csv - productName, category, specs, reviews, price
              String[] split = line.split(",");
              String category = split[1].trim().toLowerCase();

              ////if map do not have key, put category as key then value as arraylist of product
              if(!productsMap.containsKey(category)) {
                  ArrayList<Product> productList = new ArrayList<>();
                  productsMap.put(category, productList);
                  //// split[0] = product name, split[2] = specifications, split[3] = review, split[4] = price,
                  productsMap.get(category).add(new Product(split[0], split[2], split[3], Integer.parseInt(split[4].trim()), category));
                  continue;
              }
              productsMap.get(category).add(new Product(split[0], split[2], split[3], Integer.parseInt(split[4].trim()), category));
          }
        } catch (IOException e) {
            System.out.println("ERROR in populateMapOfProducts()");
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Product> getListByCategory(String category) throws Exception {
        ////throws an exception if category is not a key in productsMap
        if(!productsMap.containsKey(category)) {
            throw new Exception();
        } else { return productsMap.get(category); }
    }

    public ArrayList<Product> getDescListByPrice(ArrayList<Product> productsList) {
        productsList.sort((p1, p2) -> Integer.compare(p2.getPRICE(), p1.getPRICE()));
        return productsList;
    }

    public ArrayList<Product> getAsceListByPrice(ArrayList<Product> productsList) {
        productsList.sort((p1, p2) -> Integer.compare(p1.getPRICE(), p2.getPRICE()));
        return productsList;
    }

    public void addToCart(Product product, int quantity) throws Exception {
        if(quantity <= 0) { throw new Exception(); }
        ////if product is in cart already, just add the quantity
        else if(cart.containsKey(product)) {
            cart.put(product, cart.get(product) + quantity);
        } else {
            cart.put(product, quantity);
        }
    }

    public HashMap<Product, Integer> getCart() {
        return this.cart;
    }

    public void changeCartItemQuantity(int index, int newQuantity) {
        List<Product> indexedKeyOfMap = new ArrayList<>(cart.keySet());
        ////delete from cart if new quantity is equal or less than 0
        if(newQuantity <= 0) {
            cart.remove(indexedKeyOfMap.get(index));
            return;
        }
        cart.put(indexedKeyOfMap.get(index), newQuantity);
    }

    //// will throw an error if index is outOfBounds
    public void checkIfItemExists(int index)  {
        List<Product> indexedKeyOfMap = new ArrayList<>(cart.keySet());
        indexedKeyOfMap.get(index);
    }

    public void deleteCartItem(int index) {
        List<Product> indexedKeyOfMap = new ArrayList<>(cart.keySet());
        cart.remove(indexedKeyOfMap.get(index));
    }

    public HashMap<String, String> getVouchers() {
        return this.vouchers;
    }

    ////returns 0 if wrong input and -1 if voucher is not applicable
    public double applyVoucherIfApplicable(String voucher, double totalPrice) {
        return switch (voucher) {
            case "BUNDLE10" -> bundle10(totalPrice);
            case "BIGSPENDER" -> bigSPENDER(totalPrice);
            case "FULLBUILD15" -> fullBuild15(totalPrice);
            case "GAMEREADY" -> gameReady(totalPrice);
            case "DOUBLESTORAGE" -> doubleStorage(totalPrice);
            default -> 0;
        };
    }

    ///===============================================
    /// private method helpers for applyVoucherIfApplicable()
    ///================================================

    //// returns 200 off totalPrice if bought 2 or more storage
    private double doubleStorage(double totalPrice) {
        int storageCount = 0;
        for(Product product: this.cart.keySet()) {
            if(product.getCATEGORY().equals("memory")) {
                storageCount += this.cart.get(product);
            }
        }
        return storageCount >= 2 ? totalPrice - 200: -1;
    }

    ///returns 12% off totalPrice if bought gpu and monitor
    private double gameReady(double totalPrice) {
        boolean gpu = false, monitor = false;
        for(Product product: this.cart.keySet()) {
            switch (product.getCATEGORY()) {
                case "graphic cards" -> gpu = true;
                case "monitors" -> monitor = true;
            }
        }
        return gpu && monitor ? totalPrice - (totalPrice * 0.12): -1;
    }

    ///returns 15% off totalPrice if bought 6 core components
    private double fullBuild15(double totalPrice) {
        boolean cpu = false, motherboard = false, memory = false, storage = false, psu = false, pcCase = false;
        for(Product product: this.cart.keySet()) {
            switch (product.getCATEGORY()) {
                case "cpu" -> cpu = true;
                case "motherboards" -> motherboard = true;
                case "memory" -> memory = true;
                case "storage" -> storage = true;
                case "power supplies" -> psu = true;
                case "cases" -> pcCase = true;
            }
        }
        return cpu && motherboard && memory && storage && psu && pcCase ?
                totalPrice - (totalPrice * 0.15): -1;
    }

    ///return 1000 pesos off totalPrice if totalPrice is over 20000
    private double bigSPENDER(double totalPrice) {
        return totalPrice > 20000 ? totalPrice - 1000: -1;
    }

    ///return 10% off totalPrice if bought cpu, motherboard, and memory
    private double bundle10(double totalPrice) {
        boolean cpu = false, motherboard = false, memory = false;
        for(Product product: this.cart.keySet()) {
            switch (product.getCATEGORY()) {
                case "cpu" -> cpu = true;
                case "motherboards" -> motherboard = true;
                case "memory" -> memory = true;
            }
        }
        return cpu && motherboard && memory ? totalPrice - (totalPrice * 0.10): -1;
    }

    ///================================================================
    ///================================================================

    ///testing purpose
    public void printDemo() {
        for(Product product: productsMap.get("cpu")) {
            System.out.println(product);
        }
    }

    public void clearCart() {
        this.cart.clear();
    }

    public void addOrderToHistory(HashMap<Product, Integer> order, double total) {
        //shallow copy of order, so that when we clear cart, it does not delete field items in OrderHistory.java
        HashMap<Product, Integer> orderCopy = new HashMap<>(order);
        this.orderHistoryList.add(new OrderHistory(total, orderCopy));
    }

    public List<OrderHistory> getOrderHistory() {
        return this.orderHistoryList;
    }
}
