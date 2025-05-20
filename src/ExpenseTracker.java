import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExpenseTracker {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n== Expense Tracker Menu ==");
            System.out.println("1. Add Income");
            System.out.println("2. Add Expense");
            System.out.println("3. View Monthly Summary");
            System.out.println("4. Load Transactions from File");
            System.out.println("5. Save Transactions to File");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // flush

            switch (choice) {
                case 1 -> addTransaction(TransactionType.INCOME);
                case 2 -> addTransaction(TransactionType.EXPENSE);
                case 3 -> viewMonthlySummary();
                case 4 -> loadFromFile();
                case 5 -> saveToFile();
                case 6 -> System.exit(0);
                default -> System.out.println("Invalid option!");
            }
        }
    }

    private static void addTransaction(TransactionType type) {
        System.out.print("Enter date (yyyy-MM-dd): ");
        LocalDate date = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // flush

        System.out.print("Enter category (" +
                (type == TransactionType.INCOME ? "Salary/Business" : "Food/Rent/Travel") + "): ");
        String category = scanner.nextLine();

        transactions.add(new Transaction(type, date, amount, category));
        System.out.println("Transaction added.");
    }

    private static void viewMonthlySummary() {
        Map<String, Double> incomeSummary = new HashMap<>();
        Map<String, Double> expenseSummary = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

        for (Transaction t : transactions) {
            String key = t.getDate().format(formatter) + " - " + t.getCategory();
            Map<String, Double> map = (t.getType() == TransactionType.INCOME) ? incomeSummary : expenseSummary;
            map.put(key, map.getOrDefault(key, 0.0) + t.getAmount());
        }

        System.out.println("\n== Monthly Income Summary ==");
        incomeSummary.forEach((k, v) -> System.out.printf("%s: ₹%.2f%n", k, v));

        System.out.println("\n== Monthly Expense Summary ==");
        expenseSummary.forEach((k, v) -> System.out.printf("%s: ₹%.2f%n", k, v));
    }

    private static void loadFromFile() {
        System.out.print("Enter filename (e.g., data.csv): ");
        String fileName = scanner.nextLine();

        try {
            List<String> lines = Files.readAllLines(Paths.get(fileName));
            lines.remove(0); // remove header

            for (String line : lines) {
                String[] parts = line.split(",");
                TransactionType type = TransactionType.valueOf(parts[0].trim().toUpperCase());
                LocalDate date = LocalDate.parse(parts[1].trim());
                double amount = Double.parseDouble(parts[2].trim());
                String category = parts[3].trim();

                transactions.add(new Transaction(type, date, amount, category));
            }

            System.out.println("Transactions loaded successfully.");
        } catch (IOException e) {
            System.out.println("Failed to read the file.");
        }
    }

    private static void saveToFile() {
        System.out.print("Enter filename to save (e.g., output.csv): ");
        String fileName = scanner.nextLine();

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("TYPE,DATE,AMOUNT,CATEGORY");
            for (Transaction t : transactions) {
                writer.printf("%s,%s,%.2f,%s%n",
                        t.getType(),
                        t.getDate(),
                        t.getAmount(),
                        t.getCategory());
            }
            System.out.println("Transactions saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving to file.");
        }
    }
}
