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
        System.out.print("""
                -Type number to navigate
                INPUT:\s""");
        int input = 0;
        try {
            input = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid!");
            start();
        }
        switch(input) {
            case 1: viewCategories();
            default:
                System.out.println("INVALID!");
                start();
        }
    }

    public void viewCategories() {
        System.out.println("""
                                            
                                            CATEGORIES
                                            
        . CPU                  . GRAPHIC CARDS      . MEMORY              . KEYBOARDS
        . MOTHERBOARDS         . STORAGE            . POWER SUPPLIES      . MOUSE
        . CASES                . COOLERS            . MONITORS            . AUDIO
        """);
        System.out.print("""
                -Type "b" to go back
                -Type category to view items
                Input:\s""");
        String input = scanner.nextLine().trim();

        //go back feature
        if(input.equalsIgnoreCase("b")) start();
        viewItems(input);
    }

    public void viewItems(String category) {
        ArrayList<Product> productList = systemService.getListByCategory(category);
        while(true) {
            //prints items based on inputted category
            formatAndPrint(category, productList);
            System.out.print("""
                    -Type "b" to go back
                    -Type number of product for additional information
                    Input:\s""");
            String input = scanner.nextLine().trim();
            if(input.equalsIgnoreCase("b")) break;
            //prints product info
            try {
                viewProductInfo(productList.get(Integer.parseInt(input) - 1));
            } catch(Exception e){
                System.out.println("Product does not exists!");
                continue;
            }
        }
        viewCategories();
    }

    public void viewProductInfo(Product product) {
        System.out.printf("""
                        
                        %-30s %s
                        %-29s %s
                        %-29s %s
                        %-30s %s
                        %n""", "Product Name:", product.getPRODUCT_NAME(),
        "Review:", product.getREVIEW(),
        "Specifications:", product.getSPECIFICATIONS(),
        "Price:", "₱" + product.getPRICE());
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



