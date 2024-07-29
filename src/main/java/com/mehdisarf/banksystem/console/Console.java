package com.mehdisarf.banksystem.console;

import com.mehdisarf.banksystem.entity.BankAccount;
import com.mehdisarf.banksystem.exception.InsufficientBalanceException;
import com.mehdisarf.banksystem.exception.NegativeAmountException;
import com.mehdisarf.banksystem.service.TransactionType;
import com.mehdisarf.banksystem.service.strategy.TransactionService;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class Console {
    private static final String STARS = "****************************************";
    private static final String VALID_NUMBER_REQUEST_TEXT = "!!! Please Enter A \033[3mValid\033[0m Number !!!\t";
    private static final String TRY_AGAIN_TEXT = "Please Try Again\t";

    private static final String ERROR_OCCURRED_REQUEST_TEXT =
            "\n*** An Error Occured. Please Try Again And Enter A \033[3mValid\033[0m Number! ***\t";

    private final Map<String, TransactionService> transactionServiceMap;


    public Console(Map<String, TransactionService> transactionServiceMap) {
        this.transactionServiceMap = transactionServiceMap;
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

                        TransactionService transactionService = getTransactionService(String.valueOf(chosenOption));
                        Future<BankAccount> persistedAccountFuture =
                                executorService.submit(() -> transactionService.apply(null, null,
                                        transactionInfo.getHolderName(), transactionInfo.getAmount()));
                        BankAccount persistedAccount = persistedAccountFuture.get();
                        printMessageForSuccessfullAccountCreation(persistedAccount);
                    } else if (chosenOption == 2) {
                        TransactionInfo transactionInfo = getDepositInfoFromUser(scanner);

                        TransactionService transactionService = getTransactionService(String.valueOf(chosenOption));
                        Future<BankAccount> updatedAccountFuture =
                                executorService.submit(() -> transactionService.apply(transactionInfo.getMainAccountNumber(),
                                        null, null, transactionInfo.getAmount()));
                        BankAccount updatedAccount = updatedAccountFuture.get();
                        printMessageForSuccessfulDeposit(updatedAccount);
                    } else if (chosenOption == 3) {
                        TransactionInfo withdrawInfoFromUser = getWithdrawInfoFromUser(scanner);

                        TransactionService transactionService = getTransactionService(String.valueOf(chosenOption));
                        Future<BankAccount> updatedAccountFuture =
                                executorService.submit(() -> transactionService.apply(withdrawInfoFromUser.getMainAccountNumber(),
                                        null, null, withdrawInfoFromUser.getAmount()));
                        BankAccount updatedAccount = updatedAccountFuture.get();
                        printMessageForSuccessfulWithdraw(updatedAccount);
                    } else if (chosenOption == 4) {
                        TransactionInfo transferFundInfoFromUser = getTransferFundInfoFromUser(scanner);

                        ExecutorService executorsWithSingleThread = Executors.newFixedThreadPool(1);
                        TransactionService transactionService = getTransactionService(String.valueOf(chosenOption));
                        Future<BankAccount> bankAccountFuture =
                                executorsWithSingleThread.submit(() -> transactionService.apply(
                                        transferFundInfoFromUser.getMainAccountNumber(),
                                        transferFundInfoFromUser.getDestinationAccountNumber(),
                                        null,
                                        transferFundInfoFromUser.getAmount()));
                        bankAccountFuture.get();

                        TransactionService transactionService2 = getTransactionService(TransactionType.BALANCE);
                        Future<BankAccount> sourceAccountBalanceFuture = executorsWithSingleThread.submit(
                                () -> transactionService2.apply(transferFundInfoFromUser.getMainAccountNumber(),
                                        null, null, null));
                        Double sourceAccountBalance = sourceAccountBalanceFuture.get().getBalance();

                        Future<BankAccount> destinationAccountBalanceFuture = executorsWithSingleThread.submit(
                                () -> transactionService2.apply(transferFundInfoFromUser.getDestinationAccountNumber(),
                                        null, null, null));

                        Double destinationAccountBalance = destinationAccountBalanceFuture.get().getBalance();
                        executorsWithSingleThread.shutdown();
                        printMessageForSuccessfulTransferFund(sourceAccountBalance, destinationAccountBalance);
                    } else if (chosenOption == 5) {
                        Long accountNumber = getBalanceInfoFromUSer(scanner);

                        TransactionService transactionService = getTransactionService(String.valueOf(chosenOption));
                        Future<BankAccount> balanceFuture = executorService.submit(
                                () -> transactionService.apply(accountNumber, null, null, null));

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
                    Throwable cause = exception.getCause();

                    if (cause instanceof InsufficientBalanceException || cause instanceof NegativeAmountException)
                        printErrorMessage(cause.getMessage() + System.lineSeparator() + TRY_AGAIN_TEXT);
                    else
                        printErrorMessage(ERROR_OCCURRED_REQUEST_TEXT);
                }
            }
        } finally {
            executorService.shutdown();
        }

    }

    private TransactionService getTransactionService(String transactionType) {
        TransactionService transactionService = transactionServiceMap.get(transactionType);
        if (transactionService == null) {
            throw new RuntimeException("Unsupported transaction type");
        }
        return transactionService;
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
        if (amount < 0)
            throw new NegativeAmountException();

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
        if (initialBalance < 0)
            throw new NegativeAmountException();

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
        System.out.println("Congratulations on creating your new mehdisarf Bank System Account!");
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
