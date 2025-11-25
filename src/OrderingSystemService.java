import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class OrderingSystemService {
//  pc path for products.csv
  private final String PRODUCT_CSV_FILE = "C:\\Users\\sadsc\\IdeaProjects\\CLI-Ordering-System-finals-project-\\src\\products.csv";
//  laptop path for products.csv
//  private final String PRODUCT_CSV_FILE = "C:\\Users\\dhan\\IdeaProjects\\CLI Ordering System\\src\\products.csv";

    private final HashMap<String, ArrayList<Product>> productsMap = new HashMap<>();

    //populates productsMap with data from products.csv
    public void populateMapOfProducts()  {
        try(BufferedReader br = new BufferedReader(new FileReader(PRODUCT_CSV_FILE))) {
          String line;
          while((line = br.readLine()) != null) {
              //format of csv - productName, category, specs, reviews, price
              String[] split = line.split(",");
              String category = split[1].trim();

              //if map do not have key, put category as key then value as arraylist of product
              if(!productsMap.containsKey(category)) {
                  ArrayList<Product> productList = new ArrayList<>();
                  productsMap.put(category, productList);
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
        //throws an exception if category is not a key in productsMap
        if(!productsMap.containsKey(category.toLowerCase())) {
            throw new Exception();
        } else{ return productsMap.get(category.toLowerCase()); }
    }

    public ArrayList<Product> getDescListByPrice(ArrayList<Product> productsList) {
        productsList.sort((p1, p2) -> Integer.compare(p2.getPRICE(), p1.getPRICE()));
        return productsList;
    }

    public ArrayList<Product> getAsceListByPrice(ArrayList<Product> productsList) {
        productsList.sort((p1, p2) -> Integer.compare(p1.getPRICE(), p2.getPRICE()));
        return productsList;
    }

    //testing purpose
    public void printDemo() {
        for(Product product: productsMap.get("cpu")) {
            System.out.println(product);
        }
    }

}
