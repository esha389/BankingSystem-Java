package bank;
public class CurrentAccount extends Account {
    private static final double OVERDRAFT_LIMIT = 10000;

    public CurrentAccount(String accountNumber, String holderName, double balance) {
        super(accountNumber, holderName, balance);
    }

    @Override 
    public void withdraw(double amount) throws InsufficientFundsException {
        if(amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }
        if(getBalance() - amount < -OVERDRAFT_LIMIT) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal Overdraft limit exceeded.");
        }
        deductBalance(amount);
    }
}
