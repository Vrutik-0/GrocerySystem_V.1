import java.util.*;

public class GrocerySystem_01 
{ 
    static String RESET = "\033[0m";
    static String BOLD = "\033[1m";
    static String ITALIC = "\033[3m";
    static String UNDERLINE = "\033[4m";
    static String RED = "\033[0;31m";
    static String GREEN = "\033[0;32m";

    static Scanner in = new Scanner(System.in);
    // Categories
    static String[] categories = {"Dairy", "Snacks", "Bakery", "Vegetables", "Fruits", "Hygiene", "Beverages"};

    // Items, Stocks, and Prices for each category
    static String[][] items = 
    {
        {"Milk", "Cheese", "Butter", "Yogurt"},
        {"Chips", "Cookies", "Noodles","Biscuits"},
        {"Bread","Pastry", "Donut", "Muffin"},
        {"Potato(Kg)", "Onion(Kg)", "Tomato(Kg)","Capsicum(Kg)"},
        {"Apple(Dozen)", "Banana(Dozen)", "Orange(Dozen)", "Mango(Dozen)"},
        {"Soap", "Shampoo", "Toothpaste","Sanitizer"},
        {"Juice", "Soft Drink", "Energy Drink", "Water"}
    };

    static int[][] stocks = 
    {
        {20, 25, 25, 15},
        {30, 20, 25, 18},
        {15, 12, 20, 15},
        {15, 13, 10, 8},
        {7, 8, 5, 6},
        {12, 10, 8, 10},
        {10, 20, 15,15}
    };

    static double[][] prices = 
    {
        {32.50, 45, 40, 36.50},
        {8.50, 21, 14, 9},
        {35, 25, 12.50, 14.50},
        {23, 28.50, 24, 55},
        {59, 54, 64.50, 89.50},
        {33.50, 54.50, 65, 45},
        {20, 38.50, 55, 16.50}
    };

    // Cart
    static String[] cartItems = new String[50];
    static int[] cartQuantities = new int[50];
    static double[] cartPrices = new double[50];
    static int cartSize = 0;

    static int[][] initialStocks = new int[stocks.length][stocks.length];

    public static void main(String[] args) 
    {
        System.out.println(ITALIC+""+BOLD+"Welcome to the Grocery Store!"+RESET);   
        Categories();
    }
        static void Categories()
        {
        // Save initial stocks for reset purposes
        for (int i = 0; i < stocks.length; i++)
         {
            System.arraycopy(stocks[i], 0, initialStocks[i], 0, stocks[i].length);
        }

        //Scanner in = new Scanner(System.in);
        int categoryChoice;

        while (true) 
        {
            System.out.println(UNDERLINE+"\nCategories:"+RESET);
            for (int i = 0; i < categories.length; i++) 
            {
                System.out.println((i + 1) + ". " + categories[i]);
            }
            System.out.println(BOLD+"0. Exit"+RESET);
            System.out.print("Choose a category: ");
            categoryChoice = in.nextInt();

            if (categoryChoice == 0) 
            {
                manageCart(); // Call manageCart after exiting the category selection loop
                if (categoryChoice == 0) break; // Exit if the user chooses to generate the bill
            }

            if (categoryChoice > 0 && categoryChoice <= categories.length) 
            {
                manageSubCategory(categoryChoice - 1);
            } 
            else
             {
                System.out.println(RED+"Invalid choice. Try again."+RESET);
            }
        }

        in.close();
    }

    static void manageSubCategory(int categoryIndex)
     {
        while (true) 
        {
            System.out.println("\nItems in " + categories[categoryIndex] + ":");
            for (int i = 0; i < items[categoryIndex].length; i++)
             {
                System.out.println((i + 1) + ". " + items[categoryIndex][i] + " - Stock: " + stocks[categoryIndex][i] + ", Price: Rs " + prices[categoryIndex][i]);
            }
            System.out.println(BOLD+"0. Go back"+RESET);
            System.out.print("Choose an item to buy or 0 to return: ");
            int itemChoice = in.nextInt();

            if (itemChoice == 0) break;

            if (itemChoice > 0 && itemChoice <= items[categoryIndex].length) 
            {
                buyItem(categoryIndex, itemChoice - 1);
            }
             else 
            {
                System.out.println(RED+"Invalid choice. Try again."+RESET);
            }
        }
    }

    static void buyItem(int categoryIndex, int itemIndex) 
    {
        System.out.print("Enter quantity to buy (0 to return): ");
        int quantity = in.nextInt();

        if (quantity == 0) return;

        if (quantity > 0 && quantity <= stocks[categoryIndex][itemIndex]) {
            // Update stock
            stocks[categoryIndex][itemIndex] -= quantity;

            // Add to cart
            boolean itemExists = false;
            for (int i = 0; i < cartSize; i++) 
            {
                if (cartItems[i].equals(items[categoryIndex][itemIndex])) 
                {
                    cartQuantities[i] += quantity;
                    cartPrices[i] += quantity * prices[categoryIndex][itemIndex];
                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) 
            {
                cartItems[cartSize] = items[categoryIndex][itemIndex];
                cartQuantities[cartSize] = quantity;
                cartPrices[cartSize] = quantity * prices[categoryIndex][itemIndex];
                cartSize++;
            }

            System.out.println("Added to cart successfully.");
        } 
        else 
        {
            System.out.println(RED+"Invalid quantity. Available stock: "+RESET + stocks[categoryIndex][itemIndex]);
        }
    }

    static void manageCart()
     {
        while (true) 
        {
            System.out.println(UNDERLINE+"\nCart Options:"+RESET);
            System.out.println("1. View Cart");
            System.out.println("2. Modify Cart");
            System.out.println("3. Return to Categories");
            System.out.println("4. Clear Cart");
            System.out.println("5. Generate Bill");
            System.out.print(ITALIC+"\n Choose an option: "+RESET);
            int choice = in.nextInt();

            switch (choice) 
            {
                case 1:
                    viewCart();
                    break;
                case 2:
                    modifyCart();
                    break;
                case 3:
                      Categories();
                      break;
                case 4:
                    clearCart();
                    break;
                case 5:
                generateBill();
                break;
                default:
                    System.out.println(RED+"Invalid choice. Try again."+RESET);
            }
        }
    }

    static void viewCart() 
    {
        System.out.println("\nYour Cart:");
        if (cartSize == 0) {
            System.out.println("Cart is empty.");
            return;
        }

        for (int i = 0; i < cartSize; i++) {
            System.out.println((i + 1) + ". " + cartItems[i] + " - Quantity: " + cartQuantities[i] + ", Price: Rs " + cartPrices[i]);
        }
    }

    static void modifyCart()
     {
        viewCart();

        if (cartSize == 0) return;

        System.out.print("Enter item number to modify: ");
        int itemNumber = in.nextInt();

        if (itemNumber <= 0 || itemNumber > cartSize)
         {
            System.out.println(RED+"Invalid item number."+RESET);
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
            if (addQuantity < 0)
             {
                System.out.println(RED+"Invalid quantity."+RESET);
                return;
            }

            // Update stock
            for (int i = 0; i < items.length; i++) 
            {
                for (int j = 0; j < items[i].length; j++) 
                {
                    if (items[i][j].equals(cartItems[index])) 
                    {
                        stocks[i][j] -= addQuantity;
                    }
                }
            }

            // Update cart
            cartQuantities[index] += addQuantity;
            cartPrices[index] += (cartPrices[index] / cartQuantities[index]) * addQuantity;
            System.out.println("Stock added successfully.");
        } 
        else if (option == 2) 
        {
            System.out.print("Enter quantity to delete: ");
            int deleteQuantity = in.nextInt();
            if (deleteQuantity < 0 || deleteQuantity > cartQuantities[index]) 
            {
                System.out.println(RED+"Invalid quantity."+RESET);
                return;
            }

            // Update stock
            for (int i = 0; i < items.length; i++)
             {
                for (int j = 0; j < items[i].length; j++)
                 {
                    if (items[i][j].equals(cartItems[index]))
                     {
                        stocks[i][j] += deleteQuantity;
                    }
                }
            }

            // Update cart
            cartQuantities[index] -= deleteQuantity;
            cartPrices[index] -= (cartPrices[index] / cartQuantities[index]) * deleteQuantity;
            System.out.println("Stock deleted successfully.");
        } 
        else 
        {
            System.out.println(RED+"Invalid option."+RESET);
        }
    }

    static void clearCart() 
    {
        // Reset cart
        for (int i = 0; i < cartSize; i++) 
        {
            for (int j = 0; j < items.length; j++)
             {
                for (int k = 0; k < items[j].length; k++) 
                {
                    if (items[j][k].equals(cartItems[i])) 
                    {
                        stocks[j][k] += cartQuantities[i];
                    }
                }
            }
        }
        cartSize = 0;
        System.out.println("Cart cleared successfully.");
    }

    static void generateBill()
     {
        System.out.println("\nYour Bill:");
        double total = 0;
        for (int i = 0; i < cartSize; i++) 
        {
            System.out.println(cartItems[i] + " - Quantity: " + cartQuantities[i] + " - Price: Rs " + cartPrices[i]);
            total += cartPrices[i];
        }
        System.out.println("\n Total: Rs " + total);
        System.out.println(GREEN+"\n Thank you for shopping with us."+RESET);
        System.exit(0); // Terminate the program
    }
}
