package com.pluralsight;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    private Bank bank;

    public Menu(Bank bank) {
        this.bank = bank;
    }

    public void displayMenu(String accessLevel) {

        System.out.println();
        System.out.println("1) Transfer funds between your accounts");
        System.out.println("2) Withdraw funds from your account");
        System.out.println("3) Deposit funds to your account");
        System.out.println("4) View your account information");
        if (accessLevel.equalsIgnoreCase("Admin")) {
            System.out.println("5) Add a client");
            System.out.println("6) Remove a client");
            System.out.println("7) Edit a client");
            System.out.println("8) Sort the account records");
            System.out.println("9) View a client");
        }
        System.out.println("0) Quit");
        System.out.println();
        System.out.println("Please choose an action:");
    }

    public int getUserOption(String accessLevel) throws Exception {

        int clientOptions = 4;
        int adminOptions = 11;

        //Determine the max choices available to the user based on their access level
        int maxChoices;
        if (accessLevel.equalsIgnoreCase("Admin")) {
            maxChoices = adminOptions;
        } else {
            maxChoices = clientOptions;
        }

        //Retrieve the menu choice from the user
        //Continue to prompt the user if either the number entered is not in the menu or
        //if the user's entry is not an integer
        Scanner keyboard = new Scanner(System.in);
        int choice;
        do {
            try {
                choice = keyboard.nextInt();
                if (choice >= 0 && choice <= maxChoices) {
                    break;
                } else {
                    System.out.println("Your entry must be a valid menu item (1 - " + maxChoices + " or 0). Try again");
                }
            } catch (InputMismatchException e) {
                System.out.println("Your entry must be a number. Try again");
                keyboard.next();
            }
        } while (true);
        return choice;
    }

    public void performUserOption(int userOption) throws Exception {

        switch (userOption) {
            //Option 1 = transfer funds
            case 1:
                String fromOrTo = "From";
                String transactionType = "Transfer";
                sendForProcessingOrMessage(fromOrTo, transactionType);
                break;
            //Option 2 = withdraw funds
            case 2:
                fromOrTo = "From";
                transactionType = "Withdrawal";
                sendForProcessingOrMessage(fromOrTo, transactionType);
                break;
            //Option 3 = deposit funds
            case 3:
                fromOrTo = "To";
                transactionType = "Deposit";
                sendForProcessingOrMessage(fromOrTo, transactionType);
                break;
            //Option 4 = view account information
            case 4:
                String viewScope = "Single";
                bank.displayAccountInformation(viewScope);
                break;
            //Option 5 = add a client
            case 5:
                bank.addClient();
                break;
            //Option 6 = remove a client
            case 6:
                bank.removeClient();
                break;
            //Option 7 = edit a client
            case 7:
                bank.editClient();
                break;
            //Option 8 = sort the account records
            case 8:
                System.out.println("These are the clients prior to sorting");
                viewScope = "All";
                bank.displayAccountInformation(viewScope);
                bank.sortTheAccounts();
                bank.displayAccountInformation(viewScope);
                break;
            //Option 9 = search for a client
            case 9:
                viewScope = "AdminSingle";
                bank.displayAccountInformation(viewScope);
                break;
            default:
                System.out.println("An error occurred");
                break;
        }
    }

    public void saveToFile() throws Exception {
        bank.saveAccountsToFile();
    }

    public void sendForProcessingOrMessage(String fromOrTo, String transactionType) {
        int accountType;
        //Determine which account should be confirmed to exist
        if (transactionType.equalsIgnoreCase("Transfer")) {
            accountType = 0;
        } else {
            accountType = bank.determineFromOrToAccount(fromOrTo);
        }
        //If the account exists, determine the amount to be used during the transaction based on user prompt
        //If the request is valid (not requesting a transaction with 'nothing'),
        //process the deposit on the specified account using the specified amount
        //otherwise give message to warn user
        if (bank.confirmCheckingSavingsExist(accountType)) {

            if (transactionType.equalsIgnoreCase("Transfer")) {
                accountType = bank.determineFromOrToAccount(fromOrTo);
            }

            double amount = bank.sendForAmountToProcess(accountType);
            if (amount > 0.0) {
                bank.sendForProcessing(accountType, amount, transactionType);
                System.out.println(transactionType + " successfully processed.");
            } else {
                System.out.println("This transaction cannot be processed.");
            }
        } else {
            if (transactionType.equalsIgnoreCase("Transfer")) {
                System.out.println("You must have both checking and savings accounts in order to process a transfer.");
            } else if (accountType == 1) {
                System.out.println("You do not have a checking account.");
            } else {
                System.out.println("You do not have a savings account.");
            }
        }
    }
}
