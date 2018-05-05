package com.pluralsight;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Account {

    private int pin;
    private String firstName;
    private int validCheckingAccount;
    private double checkingAmount;
    private int validSavingsAccount;
    private double savingsAmount;
    private String accessLevel;


    public Account(int pin, String firstName, int validCheckingAccount, double checkingAmount, int validSavingsAccount,
                   double savingsAmount, String accessLevel) {
        this.pin = pin;
        this.firstName = firstName;
        this.validCheckingAccount = validCheckingAccount;
        this.checkingAmount = checkingAmount;
        this.validSavingsAccount = validSavingsAccount;
        this.savingsAmount = savingsAmount;
        this.accessLevel = accessLevel;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int newPin) {
        pin = newPin;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String newAccessLevel) {
        accessLevel = newAccessLevel;
    }

    public int getValidCheckingAccount() {
        return validCheckingAccount;
    }

    public int getValidSavingsAccount() {
        return validSavingsAccount;
    }

    public void setValidCheckingAccount(int valid) {
        validCheckingAccount = valid;
    }

    public void setValidSavingsAccount(int valid) {
        validSavingsAccount = valid;
    }

    public double getCheckingAmount() {
        return checkingAmount;
    }

    public double getSavingsAmount() {
        return savingsAmount;
    }

    public void setCheckingAmount(double amount) {
        checkingAmount = amount;
    }

    public void setSavingsAmount(double amount) {
        savingsAmount = amount;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String newFirstName) {
        firstName = newFirstName;
    }

    public void toStringForSummary() {
        if (validCheckingAccount == 1 && validSavingsAccount == 1) {
            System.out.println(firstName + "\n" + "Checking amount: " + checkingAmount + "\n" + "Savings amount: " + savingsAmount);
        } else if (validCheckingAccount == 1) {
            System.out.println(firstName + "\n" + "Checking amount: " + checkingAmount);
        } else if (validSavingsAccount == 1) {
            System.out.println(firstName + "\n" + "Savings amount: " + savingsAmount);
        } else {
            System.out.println(firstName + "\n" + "You have no accounts at this bank");
        }
    }

    public String toStringForConfirmActionDisplay() {
        return (firstName + ",  " + validCheckingAccount + ",  " + checkingAmount + ",  " + validSavingsAccount + ",  " +
                savingsAmount + ",  " + accessLevel);
    }

    @Override
    public String toString() {
        return (pin + ",  " + firstName + ",  " + validCheckingAccount + ",  " + checkingAmount + ",  " + validSavingsAccount + ",  " +
                savingsAmount + ",  " + accessLevel);
    }

    public double getAmountToProcess(int accountType) {
        double requestedAmount;
        double amountToProcess = 0.0;
        System.out.println("Checking amount: " + checkingAmount);
        System.out.println("Savings amount: " + savingsAmount);
        if (accountType == 1) {
            if (checkingAmount > 0.0) {
                requestedAmount = getRequestedAmount();
                if (requestedAmount <= checkingAmount) {
                    amountToProcess = requestedAmount;
                } else {
                    System.out.println("Your requested amount exceeds your Checking balance.");
                }
            } else {
                System.out.println("There are no funds in your Checking account.");
            }
        } else {
            if (savingsAmount > 0.0) {
                requestedAmount = getRequestedAmount();
                if (requestedAmount <= savingsAmount) {
                    amountToProcess = requestedAmount;
                } else {
                    System.out.println("Your requested transfer amount exceeds your Savings balance.");
                }
            } else {
                System.out.println("There are no funds in your Savings account.");
            }
        }
        return amountToProcess;
    }

    public double getRequestedAmount() {
        System.out.println("Enter the amount to process");
        Scanner keyboard = new Scanner(System.in);
        double requestedAmount;
        do {
            try {
                requestedAmount = keyboard.nextDouble();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid entry. Try again");
                keyboard.next();
            }
        } while (true);
        return requestedAmount;
    }

    public void processTransaction(int accountType, double requestedAmount, String transactionType) {

        switch (transactionType) {
            case "Transfer":
                if (accountType == 1) {
                    savingsAmount += requestedAmount;
                    checkingAmount -= requestedAmount;
                } else {
                    checkingAmount += requestedAmount;
                    savingsAmount -= requestedAmount;
                }
                break;
            case "Withdrawal":
                if (accountType == 1) {
                    checkingAmount -= requestedAmount;
                } else {
                    savingsAmount -= requestedAmount;
                }
                break;
            case "Deposit":
                if (accountType == 1) {
                    checkingAmount += requestedAmount;
                } else {
                    savingsAmount += requestedAmount;
                }
                break;
            default:
                System.out.println("An error occurred");
                break;
        }

        System.out.println("Checking amount: " + checkingAmount);
        System.out.println("Savings amount: " + savingsAmount);
    }


}
