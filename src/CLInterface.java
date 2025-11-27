import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class CLInterface {

    private final Scanner scanner;
    private final OrderingSystemService systemService;

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
                """);
        int input = 0;
        //loops so that it only prompts for another input when invalid instead of displaying start again
        while (true) {
            System.out.print("Input: ");
            try {
                input = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("INVALID!\n");
                continue;
            }
            if(input == 1) {
                viewCategories();
            } else if(input == 4) {
                viewCartItems();
            } else {
                System.out.println("INVALID!\n");
                continue;
            }
            break;
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
                - Type "b" to go back        - Type category to view items
                """);

        String input;
        ArrayList<Product> productList;
        ////loops so that it only prompts for another input when invalid instead of displaying categories again
        while (true) {
            System.out.print("Input: ");
            input = scanner.nextLine().toLowerCase().trim();
            ////go back feature
            if (input.equalsIgnoreCase("b")) start();
            try {
                productList = systemService.getListByCategory(input);
            } catch (Exception e) {
                System.out.println("INVALID!\n");
                continue;
            }
            break;
        }
        viewItems(input, productList);

    }

    public void viewItems(String category, ArrayList<Product> productList) {
        ////prints items based on inputted category
        formatAndPrint(category, productList);
        ////loops so that it only prompts for another input when invalid instead of displaying items again
        while (true) {
            System.out.print("""
                - Type "b" to go back        - Type number of product for additional information        - Type "asc" to ascending order based on price        - Type "des" to descending order based on price
                """);
            System.out.print("Input: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("b")) { break; }
            else if (input.equalsIgnoreCase("asc")) {
                formatAndPrint(category, systemService.getAsceListByPrice(productList));
                continue;
            }
              else if (input.equalsIgnoreCase("des")) {
                  formatAndPrint(category, systemService.getDescListByPrice(productList));
                  continue;
            }
            //// checks if input is valid, if not catch the error and continue.
            try {
                viewProductInfo(productList.get(Integer.parseInt(input) - 1));
            } catch (Exception e) {
                System.out.println("INVALID!\n"); }
        }
        viewCategories();
    }

    public void viewProductInfo(Product product) {
        System.out.printf("""
                        
                        %-30s %s
                        %-29s %s
                        %-29s %s
                        %-30s %s
                        %n""",
                "Product Name:", product.getPRODUCT_NAME(),
                "Review:", product.getREVIEW(),
                "Specifications:", product.getSPECIFICATIONS(),
                "Price:", "₱" + product.getPRICE());
        while(true) {
            System.out.print("""
                    - Type "b" to go back        - Type "add" to place in cart
                    Input:\s""");
            String input = scanner.nextLine().trim().toLowerCase();
            if(input.equals("b")) return;
            else if (input.equals("add")) {
                System.out.print("Quantity: ");
                int quantity = Integer.parseInt(scanner.nextLine().trim());
                try {
                    systemService.addToCart(product, quantity);
                    System.out.println("Added to cart!\n");
                    return;
                } catch (Exception e) { System.out.println("INVALID!"); }
            } else { System.out.println("INVALID!"); }
        }
    }

    public void viewCartItems() {
        HashMap<Product, Integer> cart = systemService.getCart();
        StringBuilder str = new StringBuilder();
        str.append("""
            
            %-25s && %s &&
            
            %-7s %-36s %s
            """.formatted("", "CART", "", "CART ITEMS", " QUANTITY"));
        for(Product product: cart.keySet()) {
            str.append("""
                    %-7s %-37s %d
                    """.formatted("", product.getPRODUCT_NAME() + " " + product.getREVIEW(), cart.get(product)));
        }
        System.out.println(str);
    }

    ////method helper for viewItems() method
    ////formats the string so that product name and price aligns okay
    private void formatAndPrint(String category, ArrayList<Product> productList) {
        StringBuilder str = new StringBuilder();
        str.append("""
                
                %-25s && %s &&
                
                %-7s %-36s %s
                """.formatted("", category.toUpperCase(), "", "PRODUCT NAME", "PRICE"));
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            str.append("""
                    %-7s %-36s ₱%d
                    """.formatted(i + 1 + ".", product.getPRODUCT_NAME(), product.getPRICE()));
        }
        System.out.println(str);
    }
}



