import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {

        OrderingSystemService systemService = new OrderingSystemService();
        systemService.populateMapOfProducts();
        CLInterface cli = new CLInterface(new Scanner(System.in), systemService);
        cli.start();
    }
}
