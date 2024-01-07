package gui;

import java.util.Scanner;

import bookstore.Item;
import bookstore.ItemFactory;
import bookstore.ItemManager;
import bookstore.ShoppingCart;

public class AdvancedBookstoreApplication
{

    private Scanner reader;


    public AdvancedBookstoreApplication()
    {
        reader = new Scanner(System.in);
    }


    public int printMenu()
    {
        int answerOperation = 0;
        while (answerOperation > 5 || answerOperation <= 0)
        {
            System.out.println("Choose(1-4)\n 1. Show items\n 2. Add item to cart\n 3. Show cart\n "
                               + "4. Remove item from cart\n 5. Exit");
            answerOperation = reader.nextInt();
            if (answerOperation > 5 || answerOperation <= 0)
                System.out.println("Wrong answer! Try again...");
        }

        return answerOperation;
    }


    public Scanner getReader()
    {
        return reader;
    }


    public static void main(String args[])
    {

        AdvancedBookstoreApplication app = new AdvancedBookstoreApplication();

        ItemManager itemManager = new ItemManager();
        ItemFactory itemFactory = new ItemFactory();

        //	Book bookRef;

        Item item = itemFactory.createBook("Discours de la methode", 50.00, "Rene Descartes", 1637, 0, 0);
        itemManager.addItem(item);
        item = itemFactory.createBook("The Meditations", 30.00, "Marcus Aurelius", 180, 1, 1);
        itemManager.addItem(item);
        item = itemFactory.createBook("The Bacchae", 30.00, "Euripides", -405, 1, 2);
        itemManager.addItem(item);
        item = itemFactory.createBook("The Trojan Women", 40.00, "Euripides", -415, 0, 3);
        itemManager.addItem(item);

        //CD cdRef;
        item = itemFactory.createCD("Piece of Mind", 10.0, "Iron Maiden", 4.0, 4);
        itemManager.addItem(item);
        item = itemFactory.createCD("Matter of Life and Death", 12.0, "Iron Maiden", 2.0, 5);
        itemManager.addItem(item);
        item = itemFactory.createCD("Perfect Strangers", 12.00, "Deep Purple", 1.0, 6);
        itemManager.addItem(item);

        ShoppingCart cart = new ShoppingCart();

        while (true)
        {
            int operation = app.printMenu();
            if (operation == 1)
            {
                itemManager.reportAllItems();
            }
            else if (operation == 2)
            {
                System.out.println("Choose the id of the item that you want to add to your cart");
                int id = app.getReader().nextInt();
                if (id > itemManager.getAllItems().size())
                    System.out.println("Error: there is no product with the specified id");
                else
                    cart.addItem(itemManager.getItem(id));
            }
            else if (operation == 3)
            {
                cart.showDetails();
            }
            else if (operation == 4)
            {
                System.out.println("Choose the id of the item that you want to remove from your cart");
                int id = app.getReader().nextInt();
                if (id > itemManager.getAllItems().size())
                    System.out.println("Error: there is no product with the specified id");
                else
                    cart.removeItem(id);
            }
            else
            {
                break;
            }
        }


    }
}

/* what you should NOT do:
 * 		amazon.createItem(1,"Discours de la methode", 50.00, "",0, "Rene Descartes", 1637,  0);		
		amazon.createItem(1,"The Meditations", 30.00, "",0, "Marcus Aurelius", 180, 1); 
		amazon.createItem(1,"The Bacchae",30.00, "",0, "Euripides", -405, 1);	
		amazon.createItem(1,"The Trojan Women",40.00, "",0, "Euripides",-415, 0);	
				amazon.createItem(2,"Piece of Mind", 10.0,"Iron Maiden",4.0,"",0,0);	
		amazon.createItem(2,"Matter of Life and Death", 12.0,"Iron Maiden",2.0,"",0,0);	
		amazon.createItem(2,"Perfect Strangers", 12.00, "Deep Purple",1.0,"",0,0);	
 */


