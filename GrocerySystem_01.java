import java.util.Scanner;

public class GrocerySystem_01 {
    // Categories
    static String[] categories = {"Dairy", "Vegetables", "Fruits", "Hygiene"};

    // Items, Stocks, and Prices for each category
    static String[][] items = {
        {"Milk", "Cheese", "Butter"},
        {"Carrot", "Potato", "Onion"},
        {"Apple", "Banana", "Orange"},
        {"Soap", "Shampoo", "Toothpaste"}
    };

    static int[][] stocks = {
        {10, 5, 7},
        {20, 30, 15},
        {25, 40, 30},
        {12, 10, 8}
    };

    static double[][] prices = {
        {2.5, 3.0, 4.0},
        {1.0, 0.5, 0.8},
        {1.2, 0.8, 1.0},
        {2.0, 5.0, 3.5}
    };

    // Cart
    static String[] cartItems = new String[50];
    static int[] cartQuantities = new int[50];
    static double[] cartPrices = new double[50];
    static int cartSize = 0;

    static int[][] initialStocks = new int[stocks.length][stocks[0].length];

    public static void main(String[] args) 
    {
        Categories();
    }
        static void Categories()
        {
        // Save initial stocks for reset purposes
        for (int i = 0; i < stocks.length; i++)
         {
            System.arraycopy(stocks[i], 0, initialStocks[i], 0, stocks[i].length);
        }

        Scanner in = new Scanner(System.in);
        int categoryChoice;

        while (true) 
        {
            System.out.println("\nCategories:");
            for (int i = 0; i < categories.length; i++) {
                System.out.println((i + 1) + ". " + categories[i]);
            }
            System.out.println("0. Exit");
            System.out.print("Choose a category: ");
            categoryChoice = in.nextInt();

            if (categoryChoice == 0) {
                manageCart(in); // Call manageCart after exiting the category selection loop
                if (categoryChoice == 0) break; // Exit if the user chooses to generate the bill
            }

            if (categoryChoice > 0 && categoryChoice <= categories.length) {
                manageSubCategory(in, categoryChoice - 1);
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }

        in.close();
    }

    static void manageSubCategory(Scanner in, int categoryIndex) {
        while (true) {
            System.out.println("\nItems in " + categories[categoryIndex] + ":");
            for (int i = 0; i < items[categoryIndex].length; i++) {
                System.out.println((i + 1) + ". " + items[categoryIndex][i] + " - Stock: " + stocks[categoryIndex][i] + ", Price: $" + prices[categoryIndex][i]);
            }
            System.out.println("0. Go back");
            System.out.print("Choose an item to buy or 0 to return: ");
            int itemChoice = in.nextInt();

            if (itemChoice == 0) break;

            if (itemChoice > 0 && itemChoice <= items[categoryIndex].length) {
                buyItem(in, categoryIndex, itemChoice - 1);
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void buyItem(Scanner in, int categoryIndex, int itemIndex) {
        System.out.print("Enter quantity to buy (0 to return): ");
        int quantity = in.nextInt();

        if (quantity == 0) return;

        if (quantity > 0 && quantity <= stocks[categoryIndex][itemIndex]) {
            // Update stock
            stocks[categoryIndex][itemIndex] -= quantity;

            // Add to cart
            boolean itemExists = false;
            for (int i = 0; i < cartSize; i++) {
                if (cartItems[i].equals(items[categoryIndex][itemIndex])) {
                    cartQuantities[i] += quantity;
                    cartPrices[i] += quantity * prices[categoryIndex][itemIndex];
                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) {
                cartItems[cartSize] = items[categoryIndex][itemIndex];
                cartQuantities[cartSize] = quantity;
                cartPrices[cartSize] = quantity * prices[categoryIndex][itemIndex];
                cartSize++;
            }

            System.out.println("Added to cart successfully.");
        } else {
            System.out.println("Invalid quantity. Available stock: " + stocks[categoryIndex][itemIndex]);
        }
    }

    static void manageCart(Scanner in) {
        while (true) {
            System.out.println("\nCart Options:");
            System.out.println("1. View Cart");
            System.out.println("2. Modify Cart");
            System.out.println("3. Generate Bill");
            System.out.println("4. Clear Cart");
            System.out.println("5. Return to Categories");
            System.out.print("Choose an option: ");
            int choice = in.nextInt();

            switch (choice) {
                case 1:
                    viewCart();
                    break;
                case 2:
                    modifyCart(in);
                    break;
                case 3:
                    generateBill();
                    break;
                case 4:
                    clearCart();
                    break;
                case 5:
                    Categories(); // Return to categories menu
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    static void viewCart() {
        System.out.println("\nYour Cart:");
        if (cartSize == 0) {
            System.out.println("Cart is empty.");
            return;
        }

        for (int i = 0; i < cartSize; i++) {
            System.out.println((i + 1) + ". " + cartItems[i] + " - Quantity: " + cartQuantities[i] + ", Price: $" + cartPrices[i]);
        }
    }

    static void modifyCart(Scanner in) {
        viewCart();

        if (cartSize == 0) return;

        System.out.print("Enter item number to modify: ");
        int itemNumber = in.nextInt();

        if (itemNumber <= 0 || itemNumber > cartSize) {
            System.out.println("Invalid item number.");
            return;
        }

        int index = itemNumber - 1;
        System.out.println("1. Add stock");
        System.out.println("2. Delete stock");
        System.out.print("Choose an option: ");
        int option = in.nextInt();

        if (option == 1) {
            System.out.print("Enter quantity to add: ");
            int addQuantity = in.nextInt();
            if (addQuantity < 0) {
                System.out.println("Invalid quantity.");
                return;
            }

            // Update stock
            for (int i = 0; i < items.length; i++) {
                for (int j = 0; j < items[i].length; j++) {
                    if (items[i][j].equals(cartItems[index])) {
                        stocks[i][j] -= addQuantity;
                    }
                }
            }

            // Update cart
            cartQuantities[index] += addQuantity;
            cartPrices[index] += (cartPrices[index] / cartQuantities[index]) * addQuantity;
            System.out.println("Stock added successfully.");
        } else if (option == 2) {
            System.out.print("Enter quantity to delete: ");
            int deleteQuantity = in.nextInt();
            if (deleteQuantity < 0 || deleteQuantity > cartQuantities[index]) {
                System.out.println("Invalid quantity.");
                return;
            }

            // Update stock
            for (int i = 0; i < items.length; i++) {
                for (int j = 0; j < items[i].length; j++) {
                    if (items[i][j].equals(cartItems[index])) {
                        stocks[i][j] += deleteQuantity;
                    }
                }
            }

            // Update cart
            cartQuantities[index] -= deleteQuantity;
            cartPrices[index] -= (cartPrices[index] / cartQuantities[index]) * deleteQuantity;
            System.out.println("Stock deleted successfully.");
        } else {
            System.out.println("Invalid option.");
        }
    }

    static void generateBill() {
        System.out.println("\nYour Bill:");
        double total = 0;
        for (int i = 0; i < cartSize; i++) {
            System.out.println(cartItems[i] + " - Quantity: " + cartQuantities[i] + ", Price: $" + cartPrices[i]);
            total += cartPrices[i];
        }
        System.out.println("Total: $" + total);
        System.out.println("Thank you for shopping with us.");
        System.exit(0); // Terminate the program
    }

    static void clearCart() {
        // Reset cart
        for (int i = 0; i < cartSize; i++) {
            for (int j = 0; j < items.length; j++) {
                for (int k = 0; k < items[j].length; k++) {
                    if (items[j][k].equals(cartItems[i])) {
                        stocks[j][k] += cartQuantities[i];
                    }
                }
            }
        }
        cartSize = 0;
        System.out.println("Cart cleared successfully.");
    }
}