package com.pluralsight;

public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("Hello! Welcome to the bank of java!");
        System.out.println("");

        //Instantiate the user
        Bank bank = new Bank();

        //Load all account information from the file
        bank.loadAccountsFromFile();

        //Continually prompt the user for their pin until a valid pin is entered
        while (!(bank.promptForPin())) {
            System.out.println("There are no client accounts with that pin.");
        }

        //Instantiate the menu
        Menu menu = new Menu(bank);

        //Display the menu to the user based on their access level
        String accessLevel = bank.getAccessLevel();
        menu.displayMenu(accessLevel);

        //Retrieve the user's choice from the menu
        int userOption = menu.getUserOption(accessLevel);

        //Perform the option requested by the user until they want to quit
        while (userOption != 0) {
            menu.performUserOption(userOption);
            menu.displayMenu(accessLevel);
            userOption = menu.getUserOption(accessLevel);
        }

        System.out.println();
        System.out.println("Thank you for choosing the bank of java! Good-bye!");

    }
}
