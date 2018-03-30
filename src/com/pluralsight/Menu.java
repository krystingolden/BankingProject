package com.pluralsight;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Menu {

    private Bank bank;

    public Menu (Bank bank){
        this.bank = bank;
    }

    public void displayMenu(String accessLevel) {

        System.out.println();
        System.out.println("1) Transfer funds");
        System.out.println("2) Withdraw funds");
        System.out.println("3) Deposit funds");
        System.out.println("4) View account information");
        if (accessLevel.equalsIgnoreCase("Admin")){
            System.out.println("5) Add a client");
            System.out.println("6) Remove a client");
            System.out.println("7) Edit a client");
            System.out.println("8) Open an account");
            System.out.println("9) Close an account");
            System.out.println("10) Sort the account records");
            System.out.println("11) Search for clients/administrators");
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
        if (accessLevel.equalsIgnoreCase("Admin")){
            maxChoices = adminOptions;
        }
        else{
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
                //Set accountType to represent both accounts (Checking and Savings)
                int accountType = 0;
                //Set the transaction type
                String transactionType = "Transfer";
                //If both accounts exist
                if (bank.confirmCheckingSavingsExist(accountType)) {
                    //Set action to be "From" an account
                    String fromOrTo = "From";
                    //Determine on which account the action will be performed based on user prompt
                    accountType = bank.determineFromOrToAccount(fromOrTo);
                    //Determine the amount to be used during the transaction based on user prompt
                    double transferAmount = bank.getAmountToProcess(accountType);
                    //If the request is valid based on current account balance
                    if(transferAmount > 0.0){
                        //Process the transfer on the specified account using the specified amount
                        bank.processTransaction(accountType, transferAmount, transactionType);
                        System.out.println("Transfer successfully processed.");
                    }
                    //Otherwise warn user
                    else {
                        System.out.println("This transfer request cannot be processed.");
                    }
                }
                //Otherwise warn user
                else {
                    System.out.println("You must have both checking and savings accounts in order to process a transfer.");
                }
                break;
            //Option 2 = withdraw funds
            case 2:
                //Set action to be "From" an account
                String fromOrTo = "From";
                //Determine on which account the action will be performed based on user prompt
                accountType = bank.determineFromOrToAccount(fromOrTo);
                //Set the transaction type
                transactionType = "Withdrawal";
                //If the account to be withdrawn from exists
                if (bank.confirmCheckingSavingsExist(accountType)) {
                    //Determine the amount to be used during the transaction based on user prompt
                    double withdrawalAmount = bank.getAmountToProcess(accountType);
                    //If the request is valid based on current account balance
                    if(withdrawalAmount > 0.0){
                        //Process the withdrawal on the specified account using the specified amount
                        bank.processTransaction(accountType, withdrawalAmount, transactionType);
                        System.out.println("Withdrawal successfully processed.");
                    }
                    //Otherwise warn user
                    else {
                        System.out.println("This withdrawal request cannot be processed.");
                    }
                }
                //Otherwise warn user
                else {
                    if (accountType == 1){
                        System.out.println("You do not have a checking account.");
                    }
                    else{
                        System.out.println("You do not have a savings account.");
                    }
                }
                break;
            //Option 3 = deposit funds
            case 3:
                //Set action to be "To" an account
                fromOrTo = "To";
                //Determine on which account the action will be performed based on user prompt
                accountType = bank.determineFromOrToAccount(fromOrTo);
                //Set the transaction type
                transactionType = "Deposit";
                //If the account to be deposited to exists
                if (bank.confirmCheckingSavingsExist(accountType)) {
                    //Determine the amount to be used during the transaction based on user prompt
                    double depositAmount = bank.getAmountToProcess(accountType);
                    //If the request is valid (not requesting a deposit of 'nothing')
                    if(depositAmount > 0.0){
                        //Process the deposit on the specified account using the specified amount
                        bank.processTransaction(accountType, depositAmount, transactionType);
                        System.out.println("Deposit successfully processed.");
                    }
                    //Otherwise warn user
                    else {
                        System.out.println("You cannot deposit 'nothing'.");
                    }
                }
                //Otherwise warn user
                else {
                    if (accountType == 1){
                        System.out.println("You do not have a checking account.");
                    }
                    else{
                        System.out.println("You do not have a savings account.");
                    }
                }
                break;
            //Option 4 = view account information
            case 4:

                break;
            //Option 5 = add a client
            case 5:

                break;
            //Option 6 = remove a client
            case 6:

                break;
            //Option 7 = edit a client
            case 7:

                break;
            //Option 8 = open an account
            case 8:

                break;
            //Option 9 = close an account
            case 9:

                break;
            //Option 10 = sort the account records
            case 10:

                break;
            //Option 11 = search for clients/administrators
            case 11:

                break;
            default:
                System.out.println("An error occurred");
                break;
        }
    }
}
