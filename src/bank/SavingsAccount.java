package bank;

public class SavingsAccount extends Account {

    private static final double MIN_BALANCE = 500;

    public SavingsAccount(String accountNumber, String holderName, double balance) {
        super(accountNumber, holderName, balance);
        
    }

    @Override 
    public void withdraw(double amount) throws InsufficientFundsException {
        if(amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero.");
        }
        if(getBalance() - amount < MIN_BALANCE) {
            throw new InsufficientFundsException("minimum balance must stay in the account.");
        }
        deductBalance(amount);
    }
} 
    

