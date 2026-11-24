package bank;

public class Customer {
    private String customerId;
    private String password;
    private Account account;

    public Customer(String customerId, String password, Account account) {
        this.customerId = customerId;
        this.password = password;
        this.account = account;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Account getAccount() {
        return account;
    }

    public boolean checkPassword(String enteredPassword) {
        return this.password.equals(enteredPassword);
    }

}
