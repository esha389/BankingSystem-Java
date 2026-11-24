package bank;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;


public class Bank {
    private HashMap<String, Customer> customers;
    private CsvStorage storage;

public Bank() {
    this.customers = new HashMap<>();
    this.storage = new CsvStorage("accounts.csv", "transactions.csv");
    HashMap<String, Account> saved = new HashMap<>();
    for (Account account : storage.loadAccounts()) {
        saved.put(account.getAccountNumber(), account);
    }

    // credentials stay hardcoded; use the saved account if there is one, else the default
    this.customers.put("cust1", new Customer("cust1", "pass1",
            saved.getOrDefault("SA001", new SavingsAccount("SA001", "John Doe", 1000))));
    this.customers.put("cust3", new Customer("cust3", "pass3",
            saved.getOrDefault("SA002", new SavingsAccount("SA002", "Alice Johnson", 1500))));
    this.customers.put("cust2", new Customer("cust2", "pass2",
            saved.getOrDefault("CA001", new CurrentAccount("CA001", "Jane Smith", 2000))));
}

    public Customer login(String customerId, String password) throws InvalidCredentialsException {
        Customer customer = customers.get(customerId);
        if (customer != null && customer.checkPassword(password)) {
            return customer;
        } else {
            throw new InvalidCredentialsException("Invalid customer ID or password.");
        }

    
    }

    public Account findAccount(String accountNumber) throws AccountNotFoundException {
        for ( Customer customer : customers.values()){
            Account account = customer.getAccount();
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        throw new AccountNotFoundException("Account not found.");
    }

    public void transfer(Account source, String destinationNumber, double amount)
        throws AccountNotFoundException, InsufficientFundsException {

    if (source.getAccountNumber().equals(destinationNumber)) {
        throw new IllegalArgumentException("Cannot transfer to the same account.");
    }

    Account destination = findAccount(destinationNumber);
    source.withdraw(amount);
    destination.deposit(amount);

    // reached only if everything above worked
    storage.saveAccounts(getAllAccounts());
    storage.logTransaction(source.getAccountNumber(), "TRANSFER_OUT", amount, source.getBalance());
    storage.logTransaction(destination.getAccountNumber(), "TRANSFER_IN", amount, destination.getBalance());
}

    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        for (Customer customer : customers.values()) {
            accounts.add(customer.getAccount());
        }
        return accounts;
    }

    public void deposit(Account account, double amount) {
    account.deposit(amount);   // if this throws, the lines below never run
    storage.saveAccounts(getAllAccounts());
    storage.logTransaction(account.getAccountNumber(), "DEPOSIT", amount, account.getBalance());
}

public void withdraw(Account account, double amount) throws InsufficientFundsException {
    account.withdraw(amount);  // if this throws, the lines below never run
    storage.saveAccounts(getAllAccounts());
    storage.logTransaction(account.getAccountNumber(), "WITHDRAW", amount, account.getBalance());
}

}
