package com.azkivam.banksystem.console;

import com.azkivam.banksystem.entity.BankAccount;
import com.azkivam.banksystem.service.Bank;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class Console {
    private static final String STARS = "****************************************";
    private static final String VALID_NUMBER_REQUEST_TEXT = "!!! Please Enter A \033[3mValid\033[0m Number !!!\t";

    private static final String ERROR_OCCURRED_REQUEST_TEXT =
            "\n*** An Error Occured. Please Try Again And Enter A \033[3mValid\033[0m Number! ***\t";

    private Bank bank;

    public Console(Bank bank) {
        this.bank = bank;
    }

    public void runConsole() {
        displayHeader();
        ExecutorService executorService = Executors.newFixedThreadPool(8);
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                try {
                    displayMainMenu();
                    int chosenOption = readOptionsFromUser(scanner);
                    if (chosenOption == 1) {
                        System.out.println("----------------------------------------");
                        System.out.print("Please Enter Your Name:");
                        scanner.nextLine();
                        String holderName = scanner.nextLine();
                        System.out.print("Enter The Initial Balance:");
                        double initialBalance = scanner.nextDouble();

                        Future<BankAccount> persistedAccountFuture =
                                executorService.submit(() -> bank.createAccount(holderName, initialBalance));
                        BankAccount persistedAccount = persistedAccountFuture.get();

                        printMessageForSuccessfullAccountCreation(persistedAccount);
//                    break;
                    } else if (chosenOption == 2) {
                        System.out.println("----------------------------------------");
                        System.out.print("Please Enter The Account Number:");
                        Long accountNumber = scanner.nextLong();
                        System.out.print("Enter The Amount:");
                        double amount = scanner.nextDouble();

                        Future<BankAccount> updatedAccountFuture =
                                executorService.submit(() -> bank.deposit(accountNumber, amount));
                        BankAccount updatedAccount = updatedAccountFuture.get();
                        printMessageForSuccessfulDeposit(updatedAccount);
//                    break;

                    } else if (chosenOption == 3) {
                        System.out.println("----------------------------------------");
                        System.out.print("Please Enter The Account Number:");
                        Long accountNumber = scanner.nextLong();
                        System.out.print("Enter The Amount:");
                        double amount = scanner.nextDouble();

                        Future<BankAccount> updatedAccountFuture =
                                executorService.submit(() -> bank.withdraw(accountNumber, amount));
                        BankAccount updatedAccount = updatedAccountFuture.get();
                        printMessageForSuccessfulWithdraw(updatedAccount);
//                    break;

                    } else if (chosenOption == 4) {
                        System.out.println("----------------------------------------");
                        System.out.print("Please Enter The \"Source\" Account Number:");
                        Long sourceAccountNumber = scanner.nextLong();
                        System.out.print("Please Enter The \"Destination\" Account Number:");
                        Long destinationAccountNumber = scanner.nextLong();
                        System.out.print("Enter The Amount Which You Want To Transfer:");
                        double amount = scanner.nextDouble();

                        ExecutorService executorsWithSingleThread = Executors.newFixedThreadPool(1);
                        executorsWithSingleThread.submit(() -> bank.transferFunds(
                                sourceAccountNumber,
                                destinationAccountNumber,
                                amount));

                        Future<Double> sourceAccountBalanceFuture =
                                executorsWithSingleThread.submit(() -> bank.displayAccountBalance(sourceAccountNumber));
                        Double sourceAccountBalance = sourceAccountBalanceFuture.get();

                        Future<Double> destinationAccountBalanceFuture =
                                executorsWithSingleThread.submit(() -> bank.displayAccountBalance(destinationAccountNumber));
                        Double destinationAccountBalance = destinationAccountBalanceFuture.get();

                        printMessageForSuccessfulTransferFund(sourceAccountBalance, destinationAccountBalance);
//                    break;
                    } else if (chosenOption == 5) {
                        System.out.println("----------------------------------------");
                        System.out.print("Please Enter The Account Number:");
                        Long accountNumber = scanner.nextLong();

                        Future<Double> balanceFuture =
                                executorService.submit(() -> bank.displayAccountBalance(accountNumber));

                        Double currentBalance = balanceFuture.get();
                        printMessageForSuccessfulGettingBalance(currentBalance);
                    } else if (chosenOption == 6) {
                        System.exit(0);
                    } else {
                        printErrorMessage(VALID_NUMBER_REQUEST_TEXT);
                    }
                } catch (InputMismatchException exception) {
                    printErrorMessage(VALID_NUMBER_REQUEST_TEXT);
                    scanner.next(); // nemituni nextInt() bezari chon uni ke scanner poshtesh
                    // gir karde o narafte jolo, int nabude ke ba nextInt() bere jolo alan.
                } catch (ExecutionException | InterruptedException exception) {
                    printErrorMessage(ERROR_OCCURRED_REQUEST_TEXT );
                }
            }
        } finally {
            executorService.shutdown();
        }

    }


    private static int readOptionsFromUser(Scanner scanner) {
        System.out.print("\n>>> Choose An Option:");
        return scanner.nextInt();
    }

    private static void printErrorMessage(String message) {
        System.out.println(STARS);
//        System.out.println(System.lineSeparator().repeat(1));
        System.err.print(message);
    }

    private static void printMessageForSuccessfullAccountCreation(BankAccount persistedAccount) {
        System.out.println("\n----------------------------------------");
        System.out.println("Congratulations on creating your new Azkivam Bank System Account!");
        System.out.println("Your Account Number:<< " + persistedAccount.getAccountNumber() + " >>");
        System.out.println("Please keep the Account Number for your records, as it can help you to work with your account.");
        System.out.println("Enjoy! :)\n");
    }

    private static void printMessageForSuccessfulDeposit(BankAccount updatedAccount) {
        System.out.println("\n----------------------------------------");
        System.out.println("The Deposit Operation Successfully Happened.");
        System.out.println("Your Account Balance:<< " + updatedAccount.getBalance() + " >>\n");
    }

    private static void printMessageForSuccessfulWithdraw(BankAccount updatedAccount) {
        System.out.println("\n----------------------------------------");
        System.out.println("The Withdraw Operation Successfully Happened.");
        System.out.println("Your Account Balance:<< " + updatedAccount.getBalance() + " >>\n");
    }


    private static void printMessageForSuccessfulTransferFund(Double sourceAccountBalance,
                                                              Double destinationAccountBalance) {
        System.out.println("\n----------------------------------------");
        System.out.println("Source Account Balance: " + sourceAccountBalance);
        System.out.println("Destination Account Balance: " + destinationAccountBalance);
    }

    private static void printMessageForSuccessfulGettingBalance(Double accountBalance) {
        System.out.println("\n----------------------------------------");
        System.out.println("Account Balance: " + accountBalance);
    }

    private void displayHeader() {
        System.out.println(STARS);
        System.out.printf("%30s%n", "Azki-Vam Banking System");
        System.out.println(STARS);
    }

    private void displayMainMenu() {
        System.out.println();
        System.out.println("1. Create a new account");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Transfer Funds");
        System.out.println("5. Get Balance");
        System.out.println("6. Exit");
        System.out.println("----------------------------------------");
    }

    private void displayContinueMenu() {
        System.out.println();
        System.out.println("1. Return to the Main Menu");
        System.out.println("2. Exit");
        System.out.print("----------------------------------------");
    }
}
