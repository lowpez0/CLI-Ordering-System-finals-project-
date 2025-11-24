import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        OrderingSystemService systemService = new OrderingSystemService();
        systemService.populateMapOfProducts();
//        systemService.printDemo();
        CLInterface cli = new CLInterface(new Scanner(System.in), systemService);
        cli.start();
    }
}
