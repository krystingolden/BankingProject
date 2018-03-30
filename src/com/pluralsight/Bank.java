package com.pluralsight;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

public class Bank {

    private int pin;
    private String accessLevel;

    private ArrayList<Account> accounts = new ArrayList<>();

    public void loadAccountsFromFile() throws IOException {

        //Get the path to the file and open it
        try (BufferedReader br = Files.newBufferedReader(Paths.get("UserAccountRecord.csv"))) {
            String loadedAccountInfo;
            //Until there are no more lines to read from the file, loop through and add them to the list
            while ((loadedAccountInfo = br.readLine()) != null) {
                //Split the loaded account string into parts, separated by the ","
                String[] splitLoadedAccountInfo = loadedAccountInfo.split(",");

                //Store the parts into variables
                int pin = parseInt(splitLoadedAccountInfo[0]);
                String firstName = splitLoadedAccountInfo[1];
                int validCheckingAccount = parseInt(splitLoadedAccountInfo[2]);
                double checkingAmount = parseDouble(splitLoadedAccountInfo[3]);
                int validSavingsAccount = parseInt(splitLoadedAccountInfo[4]);
                double savingsAmount = parseDouble(splitLoadedAccountInfo[5]);
                String accessLevel = splitLoadedAccountInfo[6];


                //Instantiate the account and populate it with the record from the file
                Account newAccount = new Account(pin, firstName, validCheckingAccount, checkingAmount, validSavingsAccount,
                        savingsAmount, accessLevel);
                //Add the new account to the arraylist of accounts
                accounts.add(newAccount);

            }
            //If there is an issue opening the file, it will throw a message
        } catch (IOException e) {
            System.out.println(e.getClass().getSimpleName() + " - " + e.getMessage());
        }
    }

    public boolean promptForPin() throws Exception {
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

    public boolean searchForPin(int tempPin) {
        boolean matchFound = false;
        for (int i = 0; i < accounts.size(); i++) {
            Account currentAccount = accounts.get(i);
            int accountPin = currentAccount.getPin();
            if (tempPin == accountPin) {
                matchFound = true;
                pin = accountPin;
                System.out.println(currentAccount.toString());
            }
        }
        return matchFound;
    }

    public String getAccessLevel() {
        for (int i = 0; i < accounts.size(); i++) {
            Account currentAccount = accounts.get(i);
            if (currentAccount.getPin() == pin) {
                accessLevel = currentAccount.getAccessLevel();
            }
        }
        return accessLevel;
    }

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

    public double getAmountToProcess(int accountType) {
        double amountToProcess = 0.0;
        for (int i = 0; i < accounts.size(); i++) {
            Account currentAccount = accounts.get(i);
            if (currentAccount.getPin() == pin) {
                amountToProcess = currentAccount.getAmountToProcess(accountType);
            }
        }
        return amountToProcess;
    }

    public void processTransaction(int accountType, double amount, String transactionType) {
        for (int i = 0; i < accounts.size(); i++) {
            Account currentAccount = accounts.get(i);
            if (currentAccount.getPin() == pin) {
                if(transactionType.equalsIgnoreCase("Transfer")) {
                    currentAccount.processTransfer(accountType, amount);
                }
                else if (transactionType.equalsIgnoreCase("Withdrawal")){
                    currentAccount.processWithdrawal(accountType, amount);
                }
                else{
                    currentAccount.processDeposit(accountType, amount);
                }
            }
        }
    }

}
