import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

// ---------------- ABSTRACT CLASS ----------------
abstract class BaseBudget {
    abstract void addExpense(double amount);
    abstract double getBalance();
    abstract void saveToFile() throws IOException;
}

// -------------------- USER CLASS --------------------
class User {
    private String type;
    private String[] categories;

    public User(String type) {
        this.type = type.toLowerCase();
        setCategories();
    }

    // Set categories based on user type
    private void setCategories() {
        if (type.equals("student")) {
            categories = new String[]{"House", "Food", "Health", "Saving"};
        } 
        else if (type.equals("govt")) {
            categories = new String[]{"House", "EMI", "Health", "Saving"};
        } 
        else {
            categories = new String[]{"House", "Shopping", "EMI", "Health", "Saving"};
        }
    }

    public String[] getUserCategories() {
        return categories;
    }
}

// -------------------- BUDGET CLASS --------------------
class Budget extends BaseBudget {
    private double income;
    private double expenses = 0;
    private HashMap<String, Double> categorySplit;

    public Budget(double income, String[] categories) {
        this.income = income;
        categorySplit = new HashMap<>();

        double split = income / categories.length;
        for (String c : categories) {
            categorySplit.put(c, split);
        }
    }

    @Override
    public void addExpense(double amount) {
        expenses += amount;
    }

    @Override
    public double getBalance() {
        return income - expenses;
    }

    @Override
    public void saveToFile() throws IOException {
        FileWriter fw = new FileWriter("budget.txt");
        fw.write("Income: " + income + "\n");
        fw.write("Total Expense: " + expenses + "\n");
        fw.write("Remaining Balance: " + getBalance() + "\n\n");

        fw.write("Category Split:\n");
        for (String key : categorySplit.keySet()) {
            fw.write(key + ": " + categorySplit.get(key) + "\n");
        }

        fw.close();
    }

    public HashMap<String, Double> getCategorySplit() {
        return categorySplit;
    }
}

// -------------------- MAIN APP --------------------
public class SmartSpendApp {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // Choose user type
            System.out.print("Enter your type (Student/Govt/Private): ");
            User user = new User(sc.nextLine());

            // Enter income
            System.out.print("Enter monthly income: ");
            double income = sc.nextDouble();

            // Create budget
            Budget budget = new Budget(income, user.getUserCategories());

            // Show auto-created categories
            System.out.println("\n--- Auto Budget Categories ---");
            for (String c : budget.getCategorySplit().keySet()) {
                System.out.println(c + " : " + budget.getCategorySplit().get(c));
            }

            // Expense input
            System.out.print("\nEnter expense amount: ");
            double amount = sc.nextDouble();
            budget.addExpense(amount);

            System.out.println("Remaining Balance: " + budget.getBalance());

            budget.saveToFile();
            System.out.println("\nSaved to budget.txt!");

        } catch (Exception e) {
            System.out.println("Error: Invalid Input!");
        }
    }
}
