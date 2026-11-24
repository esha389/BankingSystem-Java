package bank;

public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        BankApp app = new BankApp(bank);
        app.run();
    }
}