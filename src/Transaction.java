import java.time.LocalDate;

public class Transaction {
    private TransactionType type;
    private LocalDate date;
    private double amount;
    private String category;

    public Transaction(TransactionType type, LocalDate date, double amount, String category) {
        this.type = type;
        this.date = date;
        this.amount = amount;
        this.category = category;
    }

    public TransactionType getType() { return type; }
    public LocalDate getDate() { return date; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
}

