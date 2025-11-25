import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        OrderingSystemService systemService = new OrderingSystemService();
        systemService.populateMapOfProducts();
        ArrayList<Product> list = systemService.getListByCategory("cpu");
        ArrayList<Product> sorted = systemService.getAscSortedListByPrice(list);

        for(Product product: sorted) {
            System.out.println(product.getPRICE());
        }


////        systemService.printDemo();
//        CLInterface cli = new CLInterface(new Scanner(System.in), systemService);
//        cli.start();
    }
}
