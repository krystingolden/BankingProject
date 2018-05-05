package com.pluralsight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class Bank {

    private int pin;
    private String accessLevel;
    private Scanner keyboard = new Scanner(System.in);

    private List<Account> accounts = new ArrayList<>();

    public void loadAccountsFromFile() throws IOException {

        //Get the path to the file and open it
        //Until there are no more lines to read from the file, loop through and add them to the list
        //Split the loaded account string into parts, separated by the ","
        //Instantiate the account and populate it with the record from the file
        //Add the new account to the arraylist of accounts
        try (BufferedReader br = Files.newBufferedReader(Paths.get("UserAccountRecord.csv"))) {
            String loadedAccountInfo;
            while ((loadedAccountInfo = br.readLine()) != null) {

                String[] splitLoadedAccountInfo = loadedAccountInfo.split(",");

                int pin = parseInt(splitLoadedAccountInfo[0]);
                String firstName = splitLoadedAccountInfo[1];
                int validCheckingAccount = parseInt(splitLoadedAccountInfo[2]);
                double checkingAmount = parseDouble(splitLoadedAccountInfo[3]);
                int validSavingsAccount = parseInt(splitLoadedAccountInfo[4]);
                double savingsAmount = parseDouble(splitLoadedAccountInfo[5]);
                String accessLevel = splitLoadedAccountInfo[6];

                Account newAccount = new Account(pin, firstName, validCheckingAccount, checkingAmount, validSavingsAccount,
                        savingsAmount, accessLevel);

                accounts.add(newAccount);

            }
            //If there is an issue opening the file, it will throw a message
        } catch (IOException e) {
            System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    //Prompt the client for the already established pin for use in upcoming transactions
    //and validate to confirm the client exists
    public boolean promptForExistingPin() throws Exception {
        System.out.println("Please enter your pin");
        Scanner keyboard = new Scanner(System.in);
        int tempPin;
        do {
            try {
                tempPin = keyboard.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid entry. Try again");
                keyboard.next();
            }
        } while (true);
        return searchForPin(tempPin);
    }

    //Prompt the new client for the pin to be attached to their new client profile
    public int promptForNewPin() {
        System.out.println("Enter the pin:");
        int pin;
        do {
            try {
                pin = keyboard.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Your entry must be a number. Try again");
                keyboard.next();
            }
        } while (true);
        return pin;
    }

    //Validate that the entered pin exists and if found, greet the client
    public boolean searchForPin(int tempPin) {
        boolean matchFound = false;
        for (int i = 0; i < accounts.size(); i++) {
            Account currentAccount = accounts.get(i);
            int accountPin = currentAccount.getPin();
            if (tempPin == accountPin) {
                matchFound = true;
                pin = accountPin;
                System.out.println("Hello " + currentAccount.getFirstName());
            }
        }
        return matchFound;
    }

    //Determine if the client is a "user" or an "admin"
    public String getAccessLevel() {
        for (int i = 0; i < accounts.size(); i++) {
            Account currentAccount = accounts.get(i);
            if (currentAccount.getPin() == pin) {
                accessLevel = currentAccount.getAccessLevel();
            }
        }
        return accessLevel;
    }

    //Confirm that the account(s) involved in the proposed transaction exist
    public boolean confirmCheckingSavingsExist(int accountType) {
        boolean confirmation = false;
        for (int i = 0; i < accounts.size(); i++) {
            Account currentAccount = accounts.get(i);
            if (currentAccount.getPin() == pin) {
                if (accountType == 0) {
                    if (currentAccount.getValidCheckingAccount() == 1 && currentAccount.getValidSavingsAccount() == 1) {
                        confirmation = true;
                    }
                } else if (accountType == 1) {
                    if (currentAccount.getValidCheckingAccount() == 1) {
                        confirmation = true;
                    }
                } else {
                    if (currentAccount.getValidSavingsAccount() == 1) {
                        confirmation = true;
                    }
                }
            }
        }
        return confirmation;
    }


    //Determine the account to/from which the transaction should occur based on a prompt to the client
    public int determineFromOrToAccount(String fromOrTo) {
        if (fromOrTo.equalsIgnoreCase("From")) {
            System.out.println("From which account?");
        } else {
            System.out.println("To which account?");
        }
        System.out.println("1) Checking");
        System.out.println("2) Savings");
        Scanner keyboard = new Scanner(System.in);
        int accountType;
        do {
            try {
                accountType = keyboard.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid entry. Try again");
                keyboard.next();
            }
        } while (true);
        return accountType;
    }


    public double sendForAmountToProcess(int accountType) {
        double amountToProcess = 0.0;
        for (int i = 0; i < accounts.size(); i++) {
            Account currentAccount = accounts.get(i);
            if (currentAccount.getPin() == pin) {
                amountToProcess = currentAccount.getAmountToProcess(accountType);
            }
        }
        return amountToProcess;
    }

    public void sendForProcessing(int accountType, double amount, String transactionType) {
        for (int i = 0; i < accounts.size(); i++) {
            Account currentAccount = accounts.get(i);
            if (currentAccount.getPin() == pin) {
                currentAccount.processTransaction(accountType, amount, transactionType);
            }
        }
    }

    public void addClient() throws Exception {

        //Prompt the user for the client's details and store the parts into variables
        pin = promptForNewPin();

        System.out.println("Enter the first name:");
        String firstName;
        firstName = keyboard.next();


        System.out.println("Does the client have a checking account? (Y/N)");
        int validCheckingAccount;
        validCheckingAccount = validateAccount();
        double checkingAmount;
        if (validCheckingAccount == 1) {
            System.out.println("Enter the starting amount of the checking account:");
            checkingAmount = getAccountAmount();
        } else {
            checkingAmount = 0;
        }


        System.out.println("Does the client have a savings account? (Y/N)");
        int validSavingsAccount;
        validSavingsAccount = validateAccount();
        double savingsAmount;
        if (validSavingsAccount == 1) {
            System.out.println("Enter the starting amount of the savings account:");
            savingsAmount = getAccountAmount();
        } else {
            savingsAmount = 0;
        }

        System.out.println("What is the client's access level? (User/Admin)");
        String enteredAccessLevel;
        do {
            try {
                enteredAccessLevel = keyboard.next();
                if (enteredAccessLevel.equalsIgnoreCase("User")) {
                    accessLevel = "User";
                    break;
                }
                if (enteredAccessLevel.equalsIgnoreCase("Admin")) {
                    accessLevel = "Admin";
                    break;
                } else {
                    System.out.println("Invalid entry. Try again");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid entry. Try again");
                keyboard.next();
            }
        } while (true);


        //Instantiate the account and populate it with the record from the file
        Account newAccount = new Account(pin, firstName, validCheckingAccount, checkingAmount, validSavingsAccount,
                savingsAmount, accessLevel);

        //Add the new account to the arraylist of accounts
        accounts.add(newAccount);
        System.out.println("Client successfully added");
    }

    public int validateAccount() {
        int validAccount;
        do {
            try {
                String userAnswer = getUserAnswerYN();
                if (userAnswer.equalsIgnoreCase("Y")) {
                    validAccount = 1;
                    break;
                }
                if (userAnswer.equalsIgnoreCase("N")) {
                    validAccount = 0;
                    break;
                } else {
                    System.out.println("Incorrect entry. Try again");
                }
            } catch (InputMismatchException e) {
                System.out.println("Your entry must be a number. Try again");
                keyboard.next();
            }
        } while (true);
        return validAccount;
    }

    public double getAccountAmount() {
        double accountAmount;
        do {
            try {
                accountAmount = keyboard.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Your entry must be a dollar amount. Try again");
                keyboard.next();
            }
        } while (true);
        return accountAmount;
    }

    public void removeClient() {
        boolean matchFound = false;
        System.out.println("What is the first name of the client you'd like to remove?");
        String tempName = keyboard.next();
        for (int i = 0; i < accounts.size(); i++) {
            Account currentAccount = accounts.get(i);
            String firstName = currentAccount.getFirstName();
            if (tempName.equalsIgnoreCase(firstName)) {
                matchFound = true;
                System.out.println(currentAccount.toStringForConfirmActionDisplay());
                System.out.println("Do you want to remove this client? (Y/N)");
                String userAnswer = getUserAnswerYN();
                if (userAnswer.equalsIgnoreCase("Y")) {
                    accounts.remove(currentAccount);
                    System.out.println("Client successfully removed.");
                }
            }
        }
        if (!matchFound) {
            System.out.println("There are no contacts with that first name.");
        }
    }

    public void editClient() throws Exception {
        boolean matchFound = false;
        System.out.println("What is the first name of the client you'd like to edit?");
        String tempName = keyboard.next();
        for (int i = 0; i < accounts.size(); i++) {
            Account currentClient = accounts.get(i);
            String firstName = currentClient.getFirstName();
            if (tempName.equalsIgnoreCase(firstName)) {
                matchFound = true;
                System.out.println(currentClient.toStringForConfirmActionDisplay());
                System.out.println("Do you want to edit this client? (Y/N)");
                String userAnswer = getUserAnswerYN();
                if (userAnswer.equalsIgnoreCase("Y")) {
                    //Display the edit menu to the user
                    displayEditMenu();

                    int userOption = getUserOption();

                    //Edit the client detail that corresponds with the users option
                    switch (userOption) {
                        //Option 1 = edit pin
                        case 1:
                            System.out.println("What is the edited pin?");
                            int newPin = promptForNewPin();
                            currentClient.setPin(newPin);
                            break;
                        //Option 2 = edit first name
                        case 2:
                            System.out.println("What is the edited first name?");
                            String newFirstName = keyboard.next();
                            currentClient.setFirstName(newFirstName);
                            break;
                        //Option 3 = add checking account
                        case 3:
                            if (currentClient.getValidCheckingAccount() == 1) {
                                System.out.println("There is already a checking account for this client.");
                            } else {
                                currentClient.setValidCheckingAccount(1);
                                System.out.println("Enter the starting amount of the checking account:");
                                double checkingAmount = getAccountAmount();
                                currentClient.setCheckingAmount(checkingAmount);
                                System.out.println("Account added.");
                            }
                            break;
                        //Option 4 = add savings account
                        case 4:
                            if (currentClient.getValidSavingsAccount() == 1) {
                                System.out.println("There is already a savings account for this client.");
                            } else {
                                currentClient.setValidSavingsAccount(1);
                                System.out.println("Enter the starting amount of the savings account:");
                                double savingsAmount = getAccountAmount();
                                currentClient.setSavingsAmount(savingsAmount);
                                System.out.println("Account added.");
                            }
                            break;
                        //Option 5 = remove checking account
                        case 5:
                            if (currentClient.getValidCheckingAccount() == 0) {
                                System.out.println("There is no checking account to be removed for this client.");
                            } else {
                                currentClient.setValidCheckingAccount(0);
                                double checkingAmount = 0.0;
                                currentClient.setCheckingAmount(checkingAmount);
                                System.out.println("Account removed.");
                            }
                            break;
                        //Option 6 = remove savings account
                        case 6:
                            if (currentClient.getValidSavingsAccount() == 0) {
                                System.out.println("There is no savings account to be removed for this client.");
                            } else {
                                currentClient.setValidSavingsAccount(0);
                                double savingsAmount = 0.0;
                                currentClient.setSavingsAmount(savingsAmount);
                                System.out.println("Account removed.");
                            }
                            break;
                        //Option 7 = modify access level
                        case 7:
                            String currentAccessLevel = currentClient.getAccessLevel();
                            System.out.println("Access level is currently " + currentAccessLevel + ".");
                            System.out.println("Do you want to modify it?");
                            userAnswer = getUserAnswerYN();
                            if (userAnswer.equalsIgnoreCase("Y")) {
                                if (currentAccessLevel.equalsIgnoreCase("Admin")) {
                                    currentClient.setAccessLevel("User");
                                } else {
                                    currentClient.setAccessLevel("Admin");
                                }
                            }
                            break;
                        default:
                            System.out.println("An error occurred");
                            break;
                    }
                }
            }
        }
        if (!matchFound) {
            System.out.println("There are no contacts with that first name.");
        }
    }

    public void displayEditMenu() {
        System.out.println();
        System.out.println("1) Modify pin");
        System.out.println("2) First Name");
        System.out.println("3) Add checking account");
        System.out.println("4) Add savings account");
        System.out.println("5) Remove checking account");
        System.out.println("6) Remove savings account");
        System.out.println("7) Modify access level");
        System.out.println();
        System.out.println("Please choose an option:");
    }

    public int getUserOption() throws Exception {
        //Retrieve the menu choice from the user
        //Continue to prompt the user if either the number entered is not in the menu or
        //if the user's entry is not an integer
        Scanner keyboard = new Scanner(System.in);
        int choice;
        do {
            try {
                choice = keyboard.nextInt();
                if (choice > 0 && choice < 8) {
                    break;
                } else {
                    System.out.println("Your entry must be a valid menu item (1 - 7). Try again");
                }
            } catch (InputMismatchException e) {
                System.out.println("Your entry must be a number. Try again");
                keyboard.next();
            }
        } while (true);
        return choice;
    }


    public void saveAccountsToFile() throws IOException {
        //Get the path to the file which will be created/overwritten,
        // read all the lines from the list into the file and close it
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("UserAccountRecord.csv"))) {
            //As long as there are still lines to read from the list, loop through and write them out
            for (Account clientInfo : accounts) {
                StringBuilder sb = new StringBuilder();
                sb.append(clientInfo);
                writer.write(sb.toString());
                writer.newLine();
            }
            writer.close();
            //If there is an issue creating/opening the file to be written to, it will throw a message
        } catch (IOException e) {
            System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    public void displayAccountInformation(String viewScope) {
        //Display information for individual client with matching pin only (user/admin option)
        switch (viewScope) {
            case "Single":
                for (int i = 0; i < accounts.size(); i++) {
                    Account currentAccount = accounts.get(i);
                    if (currentAccount.getPin() == pin) {
                        currentAccount.toStringForSummary();
                    }
                }
                break;
            //Display information for individual client with matching first name (admin option)
            case "AdminSingle":
                boolean matchFound = false;
                System.out.println("What is the first name of the client you'd like to view?");
                String tempName = keyboard.next();
                for (int i = 0; i < accounts.size(); i++) {
                    Account currentClient = accounts.get(i);
                    String firstName = currentClient.getFirstName();
                    if (tempName.equalsIgnoreCase(firstName)) {
                        matchFound = true;
                        System.out.println(currentClient.toStringForConfirmActionDisplay());
                        System.out.println("Is this the correct client? (Y/N)");
                        String userAnswer = getUserAnswerYN();
                        if (userAnswer.equalsIgnoreCase("Y")) {
                            System.out.println(currentClient.toString());
                        }
                    }
                }
                if (!matchFound) {
                    System.out.println("There are no contacts with that first name.");
                }
                break;
            //Display information for all clients (admin option)
            case "All":
                for (int i = 0; i < accounts.size(); i++) {
                    Account currentAccount = accounts.get(i);
                    System.out.println(currentAccount.toString());
                }
                break;
            default:
                System.out.println("An error occurred");
                break;
        }
    }


    public void sortTheAccounts() throws Exception {

        displaySortMenu();

        int userOption = getUserOption();

        //Sort the contacts based on the detail that corresponds with the users option
        switch (userOption) {
            //Option 1 = sort by first name
            case 1:
                Collections.sort(accounts, Comparator.comparing(Account::getFirstName));
                break;
            //Option 2 = sort by clients with valid checking accounts
            case 2:
                Collections.sort(accounts, Comparator.comparing(Account::getValidCheckingAccount));
                break;
            //Option 3 = sort by clients with valid savings accounts
            case 3:
                Collections.sort(accounts, Comparator.comparing(Account::getValidSavingsAccount));
                break;
            //Option 4 = sort by checking amount
            case 4:
                Collections.sort(accounts, Comparator.comparing(Account::getCheckingAmount));
                break;
            //Option 5 = sort by savings amount
            case 5:
                Collections.sort(accounts, Comparator.comparing(Account::getSavingsAmount));
                break;
            //Option 6 = sort by access level
            case 6:
                Collections.sort(accounts, Comparator.comparing(Account::getAccessLevel));
                break;
            default:
                System.out.println("An error occurred");
                break;
        }
        System.out.println("Accounts successfully sorted.");
    }

    public void displaySortMenu() {

        System.out.println();
        System.out.println("1) By First Name");
        System.out.println("2) By Clients With Valid Checking Accounts");
        System.out.println("3) By Clients With Valid Savings Accounts");
        System.out.println("4) By Checking Amount");
        System.out.println("5) By Savings Amount");
        System.out.println("6) By Access Level");
        System.out.println();
        System.out.println("Please choose how the accounts should be sorted:");
    }

    public String getUserAnswerYN() {
        String userAnswer = keyboard.next();
        while (!(userAnswer.equalsIgnoreCase("Y") || userAnswer.equalsIgnoreCase("N"))) {
            System.out.println("Invalid entry. Try again");
            userAnswer = keyboard.next();
        }
        return userAnswer;
    }
}
