package com.azkivam.banksystem.console;

import com.azkivam.banksystem.entity.BankAccount;
import com.azkivam.banksystem.service.Bank;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class Console {

    private Bank bank;

    private static final String STARS = "****************************************";
    private static final String VALID_NUMBER_REQUEST_TEXT = "\n*** Please enter a \033[3mvalid\033[0m number! ***\t";

    public Console(Bank bank) {
        this.bank = bank;
    }

    private void displayHeader() {
        System.out.println(STARS);
        System.out.printf("%30s%n", "Azki-Vam Banking System");
        System.out.println(STARS);
    }

    private void displayMainMenu() {
        System.out.println();
        System.out.println("1. Create a new account");
        System.out.println("2. I have already an account");
        System.out.println("3. Exit");
        System.out.print("----------------------------------------");
    }

    private void displayContinueMenu() {
        System.out.println();
        System.out.println("1. Return to the Main Menu");
        System.out.println("2. Exit");
        System.out.print("----------------------------------------");
    }

    public void runConsole() {
        displayHeader();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                displayMainMenu();
                System.out.print("\n>>> Choose An Option:");
                int chosenOption = scanner.nextInt();
                if (chosenOption == 1) {
                    System.out.println("----------------------------------------");


                    System.out.print("Please Enter Your Name:");
                    scanner.nextLine();
                    String holderName = scanner.nextLine();
                    System.out.print("Enter The Initial Balance:");
                    double initialBalance = scanner.nextDouble();
                    BankAccount persistedAccount = bank.createAccount(holderName, initialBalance);

                    System.out.println("\n----------------------------------------");
                    System.out.println("Congratulations on creating your new Azkivam Bank System Account!");
                    System.out.println("Your Account Number:<< " + persistedAccount.getAccountNumber() + " >>");
                    System.out.println("Please keep the Account Number for your records, as it can help you to work with your account.");
                    System.out.println("Enjoy! :)\n");
                    // display signup form
                    // display transaction type menu
//                    displayContinueMenu();
                    break;
                } else if (chosenOption == 2) {
                    System.out.println("log into the account");
                    // display transaction type menu
//                    displayContinueMenu();
                    break;
                } else if (chosenOption == 3) {
                    System.exit(0);
                } else {
                    System.out.println(STARS);
                    System.out.println(System.lineSeparator().repeat(6));
                    System.out.print(VALID_NUMBER_REQUEST_TEXT);
                }
            } catch (InputMismatchException exception) {
                System.out.println(STARS);
                System.out.println(System.lineSeparator().repeat(6));
                System.out.print(VALID_NUMBER_REQUEST_TEXT);
                scanner.next(); // nemituni nextInt() bezari chon uni ke scanner poshtesh
                // gir karde o narafte jolo, int nabude ke ba nextInt() bere jolo alan.
            }
        }
    }
}
