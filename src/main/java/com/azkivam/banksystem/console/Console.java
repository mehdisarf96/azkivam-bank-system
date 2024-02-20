package com.azkivam.banksystem.console;

import com.azkivam.banksystem.entity.BankAccount;
import com.azkivam.banksystem.service.Bank;
import com.azkivam.banksystem.service.TransactionType;
import com.azkivam.banksystem.service.strategy.TransactionFactory;
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

    private TransactionFactory transactionFactory;

    public Console(Bank bank, TransactionFactory transactionFactory) {
        this.bank = bank;
        this.transactionFactory = transactionFactory;
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
                        TransactionInfo transactionInfo = getCreationInfoFromUSer(scanner);
                        Future<BankAccount> persistedAccountFuture =
                                executorService.submit(() -> transactionFactory.execute(
                                        TransactionType.CREATE, null, null,
                                        transactionInfo.getHolderName(), transactionInfo.getAmount()));
                        BankAccount persistedAccount = persistedAccountFuture.get();
                        printMessageForSuccessfullAccountCreation(persistedAccount);
                    } else if (chosenOption == 2) {
                        TransactionInfo transactionInfo = getDepositInfoFromUser(scanner);
                        Future<BankAccount> updatedAccountFuture =
                                executorService.submit(
                                        () -> transactionFactory.execute(
                                                TransactionType.DEPOSIT, transactionInfo.getMainAccountNumber(),
                                                null, null, transactionInfo.getAmount()));
                        BankAccount updatedAccount = updatedAccountFuture.get();
                        printMessageForSuccessfulDeposit(updatedAccount);
                    } else if (chosenOption == 3) {
                        TransactionInfo withdrawInfoFromUser = getWithdrawInfoFromUser(scanner);
                        Future<BankAccount> updatedAccountFuture =
                                executorService.submit(() -> transactionFactory.execute(
                                        TransactionType.WITHDRAW, withdrawInfoFromUser.getMainAccountNumber(),
                                        null, null, withdrawInfoFromUser.getAmount()));
                        BankAccount updatedAccount = updatedAccountFuture.get();
                        printMessageForSuccessfulWithdraw(updatedAccount);
                    } else if (chosenOption == 4) {
                        TransactionInfo transferFundInfoFromUser = getTransferFundInfoFromUser(scanner);

                        ExecutorService executorsWithSingleThread = Executors.newFixedThreadPool(1);
                        Future<BankAccount> bankAccountFuture = executorsWithSingleThread.submit(() -> transactionFactory.execute(
                                TransactionType.TRANSFER,
                                transferFundInfoFromUser.getMainAccountNumber(),
                                transferFundInfoFromUser.getDestinationAccountNumber(),
                                null,
                                transferFundInfoFromUser.getAmount()));
                        bankAccountFuture.get();

                        Future<BankAccount> sourceAccountBalanceFuture = executorsWithSingleThread.submit(
                                () -> transactionFactory.execute(
                                        TransactionType.BALANCE, transferFundInfoFromUser.getMainAccountNumber(),
                                        null, null, null));
                        Double sourceAccountBalance = sourceAccountBalanceFuture.get().getBalance();

                        Future<BankAccount> destinationAccountBalanceFuture = executorsWithSingleThread.submit(
                                () -> transactionFactory.execute(
                                        TransactionType.BALANCE, transferFundInfoFromUser.getDestinationAccountNumber(),
                                        null, null, null));
                        Double destinationAccountBalance = destinationAccountBalanceFuture.get().getBalance();
                        executorsWithSingleThread.shutdown();
                        printMessageForSuccessfulTransferFund(sourceAccountBalance, destinationAccountBalance);
                    } else if (chosenOption == 5) {
                        Long accountNumber = getBalanceInfoFromUSer(scanner);

                        Future<BankAccount> balanceFuture = executorService.submit(
                                () -> transactionFactory.execute(
                                        TransactionType.BALANCE, accountNumber, null,
                                        null, null));

                        Double currentBalance = balanceFuture.get().getBalance();
                        printMessageForSuccessfulGettingBalance(currentBalance);
                    } else if (chosenOption == 6) {
                        System.exit(0);
                    } else {
                        printErrorMessage(VALID_NUMBER_REQUEST_TEXT);
                    }
                } catch (InputMismatchException exception) {
                    printErrorMessage(VALID_NUMBER_REQUEST_TEXT);
                    scanner.next();
                } catch (ExecutionException | InterruptedException exception) {
                    printErrorMessage(ERROR_OCCURRED_REQUEST_TEXT);
                }
            }
        } finally {
            executorService.shutdown();
        }

    }

    private Long getBalanceInfoFromUSer(Scanner scanner) {
        System.out.println("----------------------------------------");
        System.out.print("Please Enter The Account Number:");
        return scanner.nextLong();
    }

    private TransactionInfo getTransferFundInfoFromUser(Scanner scanner) {
        System.out.println("----------------------------------------");
        System.out.print("Please Enter The \"Source\" Account Number:");
        Long sourceAccountNumber = scanner.nextLong();
        System.out.print("Please Enter The \"Destination\" Account Number:");
        Long destinationAccountNumber = scanner.nextLong();
        System.out.print("Enter The Amount Which You Want To Transfer:");
        double amount = scanner.nextDouble();

        TransactionInfo transactionInfo = new TransactionInfo();
        transactionInfo.setMainAccountNumber(sourceAccountNumber);
        transactionInfo.setDestinationAccountNumber(destinationAccountNumber);
        transactionInfo.setAmount(amount);

        return transactionInfo;
    }

    private TransactionInfo getDepositInfoFromUser(Scanner scanner) {
        System.out.println("----------------------------------------");
        System.out.print("Please Enter The Account Number:");
        Long accountNumber = scanner.nextLong();
        System.out.print("Enter The Amount:");
        double amount = scanner.nextDouble();

        TransactionInfo transactionInfo = new TransactionInfo();
        transactionInfo.setMainAccountNumber(accountNumber);
        transactionInfo.setAmount(amount);

        return transactionInfo;
    }

    private TransactionInfo getWithdrawInfoFromUser(Scanner scanner) {
        return getDepositInfoFromUser(scanner);
    }

    private TransactionInfo getCreationInfoFromUSer(Scanner scanner) {
        System.out.println("----------------------------------------");
        System.out.print("Please Enter Your Name:");
        scanner.nextLine();
        String holderName = scanner.nextLine();
        System.out.print("Enter The Initial Balance:");
        double initialBalance = scanner.nextDouble();

        TransactionInfo transactionInfo = new TransactionInfo();
        transactionInfo.setAmount(initialBalance);
        transactionInfo.setHolderName(holderName);
        return transactionInfo;
    }


    private int readOptionsFromUser(Scanner scanner) {
        System.out.print("\n>>> Choose An Option:");
        return scanner.nextInt();
    }

    private void printErrorMessage(String message) {
        System.out.println(STARS);
//        System.out.println(System.lineSeparator().repeat(1));
        System.err.print(message);
    }

    private void printMessageForSuccessfullAccountCreation(BankAccount persistedAccount) {
        System.out.println("\n----------------------------------------");
        System.out.println("Congratulations on creating your new Azkivam Bank System Account!");
        System.out.println("Your Account Number:<< " + persistedAccount.getAccountNumber() + " >>");
        System.out.println("Please keep the Account Number for your records, as it can help you to work with your account.");
        System.out.println("Enjoy! :)\n");
    }

    private void printMessageForSuccessfulDeposit(BankAccount updatedAccount) {
        System.out.println("\n----------------------------------------");
        System.out.println("The Deposit Operation Successfully Happened.");
        System.out.println("Your Account Balance:<< " + updatedAccount.getBalance() + " >>\n");
    }

    private static void printMessageForSuccessfulWithdraw(BankAccount updatedAccount) {
        System.out.println("\n----------------------------------------");
        System.out.println("The Withdraw Operation Successfully Happened.");
        System.out.println("Your Account Balance:<< " + updatedAccount.getBalance() + " >>\n");
    }


    private void printMessageForSuccessfulTransferFund(Double sourceAccountBalance,
                                                              Double destinationAccountBalance) {
        System.out.println("\n----------------------------------------");
        System.out.println("Source Account Balance: " + sourceAccountBalance);
        System.out.println("Destination Account Balance: " + destinationAccountBalance);
    }

    private void printMessageForSuccessfulGettingBalance(Double accountBalance) {
        System.out.println("\n----------------------------------------");
        System.out.println("Account Balance: " + accountBalance);
    }

    private void displayHeader() {
        System.out.println(STARS);
        System.out.printf("%30s%n", "Azki-Vam Banking System");
        System.out.println(STARS);
    }

    private void displayMainMenu() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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

    class TransactionInfo {
        private Long mainAccountNumber;
        private Long destinationAccountNumber;
        private String holderName;
        private Double amount;

        public Long getMainAccountNumber() {
            return mainAccountNumber;
        }

        public void setMainAccountNumber(Long mainAccountNumber) {
            this.mainAccountNumber = mainAccountNumber;
        }

        public Long getDestinationAccountNumber() {
            return destinationAccountNumber;
        }

        public void setDestinationAccountNumber(Long destinationAccountNumber) {
            this.destinationAccountNumber = destinationAccountNumber;
        }

        public String getHolderName() {
            return holderName;
        }

        public void setHolderName(String holderName) {
            this.holderName = holderName;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }
    }
}
