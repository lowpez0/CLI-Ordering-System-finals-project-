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
        System.out.print("INPUT: ");
        String category = scanner.nextLine();
        ArrayList<Product> productList = systemService.getListByCategory(category);
        StringBuilder str = getStringBuilder(category, productList);
        System.out.println(str);
    }



    private StringBuilder getStringBuilder(String category, ArrayList<Product> productList) {
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
        return str;
    }
}

