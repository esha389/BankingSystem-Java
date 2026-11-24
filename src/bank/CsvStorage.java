package bank;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CsvStorage {
    private String filePath;
    private String transactionsPath;  // transactions.csv

public CsvStorage(String filePath, String transactionsPath) {
    this.filePath = filePath;
    this.transactionsPath = transactionsPath;
}

    public void saveAccounts(List<Account> accounts) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("accountNumber,holderName,type,balance");   // header row

            for (Account account : accounts) {
                String type = (account instanceof SavingsAccount) ? "SAVINGS" : "CURRENT";
                writer.println(account.getAccountNumber() + ","
                        + account.getHolderName() + ","
                        + type + ","
                        + account.getBalance());
            }
        } catch (IOException e) {
            System.out.println("Could not save accounts: " + e.getMessage());
        }
    }

    public List<Account> loadAccounts() {
    List<Account> accounts = new ArrayList<>();

    File file = new File(filePath);
    if (!file.exists()) {
        return accounts;   // first run: nothing to load yet
    }

    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        reader.readLine();   // skip the header row

        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;    // skip blank lines
            }

            String[] parts = line.split(",");
            if (parts.length != 4) {
                System.out.println("Warning: skipping bad line: " + line);
                continue;
            }

            try {
                String accountNumber = parts[0].trim();
                String holderName = parts[1].trim();
                String type = parts[2].trim();
                double balance = Double.parseDouble(parts[3].trim());

                if (type.equals("SAVINGS")) {
                    accounts.add(new SavingsAccount(accountNumber, holderName, balance));
                } else if (type.equals("CURRENT")) {
                    accounts.add(new CurrentAccount(accountNumber, holderName, balance));
                } else {
                    System.out.println("Warning: unknown account type, skipping: " + line);
                }
            } catch (NumberFormatException e) {
                System.out.println("Warning: bad balance, skipping: " + line);
            }
        }
    } catch (IOException e) {
        System.out.println("Could not load accounts: " + e.getMessage());
    }

    return accounts;
}

   public void logTransaction(String accountNumber, String type, double amount, double balanceAfter) {
    boolean newFile = !new File(transactionsPath).exists();

    // the "true" means: add to the end of the file, do not erase it
    try (PrintWriter writer = new PrintWriter(new FileWriter(transactionsPath, true))) {
        if (newFile) {
            writer.println("dateTime,accountNumber,type,amount,balanceAfter");
        }
        String time = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        writer.println(time + "," + accountNumber + "," + type + ","
                + amount + "," + balanceAfter);
    } catch (IOException e) {
        System.out.println("Could not log transaction: " + e.getMessage());
    }
}
}    

    