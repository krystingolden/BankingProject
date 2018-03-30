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

    public int getPin(){
        return pin;
    }

    public String getAccessLevel(){
        return accessLevel;
    }

    public int getValidCheckingAccount(){
        return validCheckingAccount;
    }

    public int getValidSavingsAccount(){
        return validSavingsAccount;
    }

    public double getCheckingAmount(){
        return checkingAmount;
    }

    public double getSavingsAmount(){
        return savingsAmount;
    }


    @Override
    public String toString() {
        if(validCheckingAccount == 1 && validSavingsAccount == 1) {
            return (firstName + "\n" + "Checking amount: " + checkingAmount + "\n" + "Savings amount: " + savingsAmount);
        }
        else if(validCheckingAccount == 1) {
            return (firstName + "\n" + "Checking amount: " + checkingAmount);
        }
        else if(validSavingsAccount == 1) {
            return (firstName + "\n" + "Savings amount: " + savingsAmount);
        }
        else{
            return (firstName + "\n" + "You have no accounts at this bank");
        }
    }

    public double getAmountToProcess(int accountType){
        double requestedAmount;
        double amountToProcess = 0.0;
        System.out.println("Checking amount: " + checkingAmount);
        System.out.println("Savings amount: " + savingsAmount);
        if (accountType == 1) {
            if (checkingAmount > 0.0) {
                requestedAmount = getRequestedAmount();
                if(requestedAmount <= checkingAmount){
                    amountToProcess = requestedAmount;
                }
                else{
                    System.out.println("Your requested amount exceeds your Checking balance.");
                }
            }
            else{
                System.out.println("There are no funds in your Checking account.");
            }
        }else {
                if (savingsAmount > 0.0) {
                    requestedAmount = getRequestedAmount();
                    if(requestedAmount <= savingsAmount){
                        amountToProcess = requestedAmount;
                    }
                    else{
                        System.out.println("Your requested transfer amount exceeds your Savings balance.");
                    }
                }
                else{
                    System.out.println("There are no funds in your Savings account.");
                }
            }
        return amountToProcess;
    }

    public double getRequestedAmount(){
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

    public void processTransfer(int accountType, double requestedAmount){
        if (accountType == 1){
            savingsAmount += requestedAmount;
            checkingAmount -= requestedAmount;
        }else{
            checkingAmount += requestedAmount;
            savingsAmount -= requestedAmount;
        }
        System.out.println("Checking amount: " + checkingAmount);
        System.out.println("Savings amount: " + savingsAmount);
    }

    public void processWithdrawal(int accountType, double requestedAmount){
        if (accountType == 1){
            checkingAmount -= requestedAmount;
        }else{
            savingsAmount -= requestedAmount;
        }
        System.out.println("Checking amount: " + checkingAmount);
        System.out.println("Savings amount: " + savingsAmount);
    }

    public void processDeposit(int accountType, double requestedAmount){
        if (accountType == 1){
            checkingAmount += requestedAmount;
        }else{
            savingsAmount += requestedAmount;
        }
        System.out.println("Checking amount: " + checkingAmount);
        System.out.println("Savings amount: " + savingsAmount);
    }




}
