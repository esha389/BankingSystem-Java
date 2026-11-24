package bank;

import java.util.Scanner;

public class BankApp {
    private Bank bank;
    private Scanner scanner;

    public BankApp(Bank bank) {
        this.bank = bank;
        this.scanner = new Scanner(System.in);
    }

    // ---------- starting point ----------
    public void run() {
        System.out.println("=== Welcome to the Bank ===");
        boolean again = true;

        while (again) {
            Customer customer = login();
            if (customer == null) {
                System.out.println("Too many failed attempts. Goodbye.");
                break;
            }

            showMenu(customer);

            System.out.print("Log in as another customer? (yes/no): ");
            again = scanner.nextLine().trim().equalsIgnoreCase("yes");
        }

        System.out.println("Thank you for banking with us.");
        scanner.close();
    }

    // ---------- login: 3 attempts ----------
    private Customer login() {
        for (int attempt = 1; attempt <= 3; attempt++) {
            System.out.print("Customer ID: ");
            String id = scanner.nextLine().trim();
            System.out.print("Password: ");
            String password = scanner.nextLine().trim();

            try {
                Customer customer = bank.login(id, password);
                System.out.println("Login successful. Welcome, "
                        + customer.getAccount().getHolderName() + "!");
                return customer;
            } catch (InvalidCredentialsException e) {
                System.out.println(e.getMessage() + " Attempts left: " + (3 - attempt));
            }
        }
        return null;
    }

    // ---------- menu loop ----------
    private void showMenu(Customer customer) {
        Account account = customer.getAccount();   // the logged-in customer's own account
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. View account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer");
            System.out.println("5. Logout");

            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    viewAccount(account);
                    break;
                case 2:
                    deposit(account);
                    break;
                case 3:
                    withdraw(account);
                    break;
                case 4:
                    transfer(account);
                    break;
                case 5:
                    loggedIn = false;
                    System.out.println("Logged out.");
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1 to 5.");
            }
        }
    }

    // ---------- the four actions ----------
    private void viewAccount(Account account) {
        System.out.println(account);
    }

    private void deposit(Account account) {
        try {
            double amount = readAmount("Amount to deposit: ");
            bank.deposit(account, amount);
            System.out.println("Deposit successful. New balance: " + account.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void withdraw(Account account) {
        try {
            double amount = readAmount("Amount to withdraw: ");
            bank.withdraw(account, amount);
            System.out.println("Withdrawal successful. New balance: " + account.getBalance());
        } catch (InsufficientFundsException | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void transfer(Account account) {
        System.out.print("Destination account number: ");
        String destination = scanner.nextLine().trim();

        try {
            double amount = readAmount("Amount to transfer: ");
            bank.transfer(account, destination, amount);   // source is always the logged-in account
            System.out.println("Transfer successful. New balance: " + account.getBalance());
        } catch (AccountNotFoundException | InsufficientFundsException
                 | IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ---------- safe input helpers ----------
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private double readAmount(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (Double.isNaN(value) || Double.isInfinite(value)) {
                    System.out.println("Please enter a valid number.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}