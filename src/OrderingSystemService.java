import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderingSystemService {
    private final String PRODUCT_CSV_FILE = "products.csv";
    private HashMap<String, ArrayList<Product>> productsMap;

    public OrderingSystemService() {
        productsMap = new HashMap<>();
    }


    public void populateMapOfProducts() {
        try(BufferedReader br = new BufferedReader(new FileReader(PRODUCT_CSV_FILE))) {
          String line;
          while((line = br.readLine()) != null) {
              //format of csv - productName, category, specs, reviews, price
              String[] split = line.split(",");
              String category = split[1];

              //if map do not have key, put category as key then value as arraylist of product
              if(!productsMap.containsKey(category)) {
                  ArrayList<Product> productList = new ArrayList<>();
                  productsMap.put(category, productList);
                  productsMap.get(category).add(new Product(split[0], split[2], split[3], Integer.parseInt(split[4])));
                  continue;
              }
              productsMap.get(category).add(new Product(split[0], split[2], split[3], Integer.parseInt(split[4])));
          }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void printDemo() {
        for(Product)
    }

}
