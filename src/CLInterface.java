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
        int input = scanner.nextInt();

        switch(input) {
            case 1: viewItems();
        }
    }

    public void viewItems() {
        System.out.println("""
                                            CATEGORIES
        1. CPU                  4. GRAPHIC CARTS      7. MEMORY              10. KEYBOARDS
        2. MOTHERBOARDS         5. STORAGE            8. POWER SUPPLIES      11. MOUSE
        3. CASES                6. COOLERS            9. MONITORS            12. HEADSETS & AUDIO
        """);
        System.out.println("INPUT: ");
        int category = scanner.nextInt();

    }
}

