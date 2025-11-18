import java.util.ArrayList;
import java.util.Scanner;

public class CLInterface {

    private Scanner scanner;
    private OrderingSystemService systemService;

    public CLInterface(Scanner scanner, OrderingSystemService systemService) {
        this.scanner = scanner;
        this.systemService = systemService;
    }

    public void start() {
        System.out.println("""
                
                BuildMyPc
                
                1.View Items
                2.Vouchers
                3.Order History
                4.View Cart
                5.Checkout
                
                """);
        System.out.print("INPUT:");
        int input = Integer.parseInt(scanner.nextLine());

        switch(input) {
            case 1: viewItems();
        }
    }

    public void viewItems() {
        System.out.println("""
                                            
                                            CATEGORIES
                                            
        . CPU                  . GRAPHIC CARDS      . MEMORY              . KEYBOARDS
        . MOTHERBOARDS         . STORAGE            . POWER SUPPLIES      . MOUSE
        . CASES                . COOLERS            . MONITORS            . AUDIO
        """);
        System.out.print("-Type \"b\" to go back\nINPUT: ");
        String input = scanner.nextLine();

        if(input.trim().equalsIgnoreCase("b")) start();

        ArrayList<Product> productList = systemService.getListByCategory(input);
        formatAndPrint(input, productList);

        System.out.println("Input:");
        int product = Integer.parseInt(scanner.nextLine());
    }

    public void productInfo() {

    }


    //method helper for viewItems() method
    //formats the string so that product name and price aligns okay
    private void formatAndPrint(String category, ArrayList<Product> productList) {
        StringBuilder str = new StringBuilder();
        str.append("""
                
                %-25s && %s &&
                
                %-6s %-36s %s
                """.formatted("", category.toUpperCase(),"", "PRODUCT NAME", "PRICE"));
        for(int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            str.append("""
                    %d%-5s %-36s ₱%d
                    """.formatted(i + 1, ".", product.getPRODUCT_NAME(), product.getPRICE()));
        }
        System.out.println(str);
    }
}



