import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class OrderingSystemService {
////  pc path for products.csv
  private final String PRODUCT_CSV_FILE = "C:\\Users\\sadsc\\IdeaProjects\\CLI-Ordering-System-finals-project-\\src\\products.csv";
////  laptop path for products.csv
////    private final String PRODUCT_CSV_FILE = "C:\\Users\\dhan\\IdeaProjects\\CLI Ordering System\\src\\products.csv";

    private final HashMap<String, ArrayList<Product>> productsMap = new HashMap<>();
    private final HashMap<String, Double> vouchers = new HashMap<>();
    private final LinkedHashMap<Product, Integer> cart = new LinkedHashMap<>();

    public OrderingSystemService() {
        populateMapOfProducts();
        populateMapOfVouchers();
    }

    private void populateMapOfVouchers() {
        vouchers.put("\"BUNDLE10\" - 10% off when purchasing CPU + MOTHERBOARD + RAM together.", 0.10);
        vouchers.put("\"BIGSPENDER\" - ₱1,000 off orders over ₱20,000", 1000.0);
        vouchers.put("\"FULLBUILD15\" - 15% off when buying all 6 core components", 0.15);
        vouchers.put("\"GAMEREADY\" - 12% off when buying GPU + Gaming Monitor", 0.12);
        vouchers.put("\"DOUBLESTORAGE\" - 20% off SSDs when buying 2 or more storage devices", 0.20);
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
                  //// split[0] = product name, split[2] = specifications, split[3] = review, split[4] = price
                  productsMap.get(category).add(new Product(split[0], split[2], split[3], Integer.parseInt(split[4].trim())));
                  continue;
              }
              productsMap.get(category).add(new Product(split[0], split[2], split[3], Integer.parseInt(split[4].trim())));
          }
        } catch (IOException e) {
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

    public LinkedHashMap<Product, Integer> getCart() {
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

    public HashMap<String, Double> getVouchers() {
        return this.vouchers;
    }

    ////testing purpose
    public void printDemo() {
        for(Product product: productsMap.get("cpu")) {
            System.out.println(product);
        }
    }

}
