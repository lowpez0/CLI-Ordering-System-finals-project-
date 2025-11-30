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
        int input = 0;
        ////loops so that it only prompts for another input when invalid instead of displaying start again
        boolean loop = true;
        while (loop) {
            printStartUI();
            try {
                input = Integer.parseInt(scanner.nextLine().trim());
            } catch (Exception e) {
                System.out.println("INVALID!\n");
                continue;
            }
            switch (input) {
                case 1:
                    viewCategories();
                    break;
                case 2:
                    viewVouchers();
                    break;
                case 4:
                    viewCartItems();
                    break;
                case 5:
                    viewCheckout();
                    break;
                case 6:
                    loop = false;
                    break;
                default:
                    System.out.println("INVALID");
            }
        }
        System.out.println("END PROGRAM!");
    }

    private void viewCategories() {
        printCategories();
        String input;
        ArrayList<Product> productList;
        ////loops so that it only prompts for another input when invalid instead of displaying categories again
        while (true) {
            System.out.print("Input: ");
            input = scanner.nextLine().toLowerCase().trim();
            ////go back feature
            if (input.equalsIgnoreCase("b")) return;
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

    private void viewItems(String category, ArrayList<Product> productList) {
        printItems(category, productList);
        ///loops so that it only prompts for another input when invalid instead of displaying items again
        while (true) {
            System.out.print("""
                    - Type "b" to go back        - Type number of product for additional information        - Type "asc" to ascending order based on price        - Type "des" to descending order based on price
                    """);
            System.out.print("Input: ");
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("b")) {
                break;
            } else if (input.equalsIgnoreCase("asc")) {
                printItems(category, systemService.getAsceListByPrice(productList));
                continue;
            } else if (input.equalsIgnoreCase("des")) {
                printItems(category, systemService.getDescListByPrice(productList));
                continue;
            }
            //// checks if input is valid, if not catch the error and continue.
            try {
                viewProductInfo(productList.get((Integer.parseInt(input) - 1)));
            } catch (Exception e) {
                System.out.println("INVALID!\n");
            }
        }
        viewCategories();
    }

    private void viewProductInfo(Product product) {
        printProductInfo(product);
        while (true) {
            System.out.print("""
                    - Type "b" to go back        - Type "add" to place in cart
                    Input:\s""");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("b")) return;
            else if (input.equals("add")) {
                try {
                    System.out.print("Quantity: ");
                    int quantity = Integer.parseInt(scanner.nextLine().trim());
                    systemService.addToCart(product, quantity);
                    System.out.println("Added to cart!\n");
                    return;
                } catch (Exception e) {
                    System.out.println("INVALID!\n");
                }
            } else {
                System.out.println("INVALID!");
            }
        }
    }

    private void viewCartItems() {
        if(systemService.getCart().isEmpty()){
            System.out.println("THERE IS NO ITEMS IN CART!!!");
            return;
        }
        while (true) {
            printCartItems();
            System.out.print("""
                    - Type "b" to go back        - Type item number to remove or change quantity.
                    Input:\s""");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("b")) return;
            try {
                systemService.checkIfItemExists(Integer.parseInt(input) - 1);
                System.out.print("""
                        \n- Type "remove" to delete from cart       - Type "quantity" to change amount of order
                        Input:\s""");
                String input2 = scanner.nextLine().trim().toLowerCase();
                if (input2.equals("remove")) {
                    systemService.deleteCartItem(Integer.parseInt(input) - 1);
                    System.out.println("Removed Item from Cart!");
                } else if (input2.equals("quantity")) {
                    System.out.print("New Quantity: ");
                    int newQuantity = Integer.parseInt(scanner.nextLine().trim());
                    systemService.changeCartItemQuantity(Integer.parseInt(input) - 1, newQuantity);
                    System.out.println("Changed Quantity!");
                } else {
                    System.out.println("INVALID!");
                }
            } catch (Exception e) {
                System.out.println("INVALID!");
            }
        }
    }

    private void viewCheckout() {
        if (systemService.getCart().isEmpty()) {
            System.out.println("NO ITEM FOUND IN CART!");
            return;
        }
        double originalPrice = printOrderSummary();
        displayAvailableVouchers();
        processCheckoutInput(originalPrice);
        systemService.addOrderToHistory(systemService.getCart());
        systemService.clearCart();
    }


    private void processCheckoutInput(double originalPrice) {
        double appliedVoucherPrice = 0;
        StringBuilder voucherName = new StringBuilder();
        while (true) {
            System.out.print("""
                
                - Type "b" to go back        -Type "pay" to checkout        - Type name of voucher to apply it        - Type "description" to view voucher details
                Input:\s""");
            String input = scanner.nextLine().trim().toLowerCase();
            switch (input) {
                case "b" -> {
                    return;
                }
                case "description" -> {
                    viewVouchers();
                    continue;
                }
                case "pay" -> {
                    confirmOrder(originalPrice, appliedVoucherPrice, voucherName);
                    return;
                }
            }
            appliedVoucherPrice = systemService.applyVoucherIfApplicable(input.toUpperCase(), originalPrice);
            if (appliedVoucherPrice == 1) {
                System.out.println("INVALID!\n");
                continue;
            } else if (appliedVoucherPrice == 0) {
                System.out.println("VOUCHER IS NOT APPLICABLE!\n");
                continue;
            }
            voucherName.append(input.toUpperCase());
            System.out.println("VOUCHER APPLIED!");
        }
    }

    private void displayAvailableVouchers() {
        System.out.println("Vouchers: ");
        StringBuilder str = new StringBuilder();
        for (String voucher : systemService.getVouchers().keySet())
            str.append("\"" + voucher + "\"" + ", ");
        System.out.println(str);
    }

    private void confirmOrder(double originalPrice, double appliedVoucherPrice, StringBuilder voucher) {
        double total = 0;
        if(!(appliedVoucherPrice == 0 || appliedVoucherPrice == 1))
            total += appliedVoucherPrice;
        else total = originalPrice;
        System.out.printf("""
                
                💰 CASH ON DELIVERY
                Total: ₱%.2f
                Used voucher: %s
                
                - Type "y" to confirm order
                Input:\s""", total, voucher.isEmpty() ? "NO VOUCHER APPLIED" : voucher.toString());
        if (!scanner.nextLine().trim().equalsIgnoreCase("y")) {
            System.out.println("INVALID!");
            return;
        }
        System.out.println("✅ Order confirmed! Your items will be delivered.");
        simulateOrderArriving();
        System.out.println("✅ Your order have been delivered.");
        printReceipt(originalPrice, appliedVoucherPrice, voucher);
    }

    private void printReceipt(double originalPrice, double appliedVoucherPrice, StringBuilder voucher) {
        HashMap<Product, Integer> cart = systemService.getCart();
        StringBuilder str = new StringBuilder();
        str.append("""
                                             
                                             
                                             BuildMyPc
                                         Telp. 09165595489
                                         
                                         
                *****************************************************************
                                     C A S H   R E C E I P T                
                *****************************************************************
                
                   Description                           Q T Y        P R I C E            
                """);
        for(Product product: cart.keySet()) {
            int qty = cart.get(product);
            str.append("""
                       %-38s %-12d ₱%d
                    """.formatted(product.getPRODUCT_NAME(), qty, (product.getPRICE() * qty)));
        }
        str.append("""
                
                *****************************************************************
                   Original Price: %-33s ₱%.2f
                   Voucher used: %-34s- ₱%.2f
                -----------------------------------------------------------------
                   Total Paid: %-37s ₱%.2f
                
                *****************************************************************
                
                                       T H A N K  Y O U !
                """.formatted("", originalPrice,
                "\"" + voucher + "\"", (originalPrice - appliedVoucherPrice),
                "", appliedVoucherPrice));
        System.out.println(str);
    }

    // waits for 2 seconds, simulating order arriving
    private void simulateOrderArriving() {
        try {
            System.out.print("Order arriving");
            for (int i = 0; i < 2; i++) {
                Thread.sleep(1000);
                System.out.print(". ");
            }
            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void viewVouchers() {
        printVouchers();
        while (true) {
            System.out.print("""
                    - Type "b" to go back
                    Input:\s""");
            if (scanner.nextLine().trim().equalsIgnoreCase("b")) return;
            else {
                System.out.println("INVALID!\n");
            }
        }
    }

    ///
    ///    PRIVATE METHODS SECTION FOR PRINTING AND FORMATTING
    ///


    private void printVouchers() {
        StringBuilder str = new StringBuilder();
        str.append("""
                
                %-25s && %s &&
                
                """.formatted("", "VOUCHERS"));
        HashMap<String, String> vouchers = systemService.getVouchers();
        for (String voucher : vouchers.keySet()) {
            str.append("""
                    %-20s %s
                    """.formatted("\"" + voucher + "\"", vouchers.get(voucher)));
        }
        System.out.println(str);
    }

    private void printProductInfo(Product product) {
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
    }

    private void printCartItems() {
        HashMap<Product, Integer> cart = systemService.getCart();
        StringBuilder str = new StringBuilder();
        str.append("""
                
                %-25s && %s &&
                
                     %-44s %s
                """.formatted("", "CART",
                "CART ITEMS", " QUANTITY"));
        int i = 0;
        for (Product product : cart.keySet()) {
            str.append("""
                    %-53s %d
                    %s
                    
                    """.formatted((i + 1) + ". " + product.getPRODUCT_NAME(), cart.get(product),
                    "₱" + product.getPRICE() + "\t" + product.getREVIEW().trim()));
            i++;
        }
        System.out.println(str);
    }

    private void printCategories() {
        System.out.println("""
                
                                                    CATEGORIES
                
                . CPU                  . GRAPHIC CARDS      . MEMORY              . KEYBOARDS
                . MOTHERBOARDS         . STORAGE            . POWER SUPPLIES      . MOUSE
                . CASES                . COOLERS            . MONITORS            . AUDIO
                """);
        System.out.print("""
                - Type "b" to go back        - Type category to view items
                """);
    }

    private void printStartUI() {
        System.out.println("""
                
                BuildMyPc
                
                1.View Items
                2.Vouchers
                3.Order History
                4.View Cart
                5.Checkout
                6.Exit
                """);
        System.out.print("""
                -Type number to navigate
                Input:\s""");
    }

    /// /method helper for viewItems() method
    /// formats the string so that product name and price aligns okay
    private void printItems(String category, ArrayList<Product> productList) {
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

    private double printOrderSummary() {
        HashMap<Product, Integer> cart = systemService.getCart();
        StringBuilder str = new StringBuilder();
        str.append("""
                
                %-20s && %s &&
                
                     %-31s %-15s %s
                """.formatted("", "ORDER SUMMARY",
                "ITEMS", "QUANTITY", "PRICE"));
        double total = 0;
        for (Product product : cart.keySet()) {
            total += (product.getPRICE() * cart.get(product));
            str.append("""
                    %-39s %-12d ₱%d
                    """.formatted(product.getPRODUCT_NAME(), cart.get(product), product.getPRICE()));
        }
        System.out.println(str.append("\nTOTAL: ₱" + total + "\n"));
        return total;
    }
}

//fix bug cart not clearing after checkout
