package bank;
public abstract class Account{
    private String accountNumber;
    private String holderName;
    private double balance;

    public Account(String accountNumber, String holderName, double balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        } 
        balance += amount;
    }

    public abstract void withdraw(double amount) throws InsufficientFundsException;

    protected void deductBalance(double amount) {
        this.balance -= amount;
    }

    @Override 
    public String toString() {
        return "Account Number: " + accountNumber + ", Holder Name: " + holderName + ", Balance: " + balance;
    }
}
