import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/** The UserOperations class will handle the operations that deals with creating users, generating statements and finding the users by name or id.
 *
 */
public class UserOperations {
    public static double startingCheckingBalance;
    public static double startingSavingBalance;
    public static double startingCreditBalance;


    /**
     * The addNewUser method is as the name implies, adds a new user going through every field required in the CSV in the proper order
     * filling with a placeholder amount for the balances and giving IDS based on the max ID currently in the CSV
     * for the credit score it generates a random score based on the input as like in real life they would give you a credit score based on the previous
     * @param userInput : takes the userInput (Scanner) as the main paramater to fill the fields of the CSV with
     * the information of the new customer
     */
    public static void addNewUser(Scanner userInput, ArrayList<customer> customers) {
        System.out.println("Enter user details:");

        System.out.print("First Name: ");
        String firstName = userInput.nextLine();

        System.out.print("Last Name: ");
        String lastName = userInput.nextLine();

        System.out.print("Date of Birth (Day-Month-Year): ");
        String dob = userInput.nextLine();

        System.out.print("Address: ");
        String address = userInput.nextLine();

        System.out.print("Phone Number: ");
        String phoneNumber = userInput.nextLine();

        System.out.print("Credit Score: ");
        int creditScore = userInput.nextInt();
        userInput.nextLine();

        int userId = ++CSVHandler.currentMaxId;

        float initialCheckingBalance = 0.0f;//place holders values, actually work better for testing
        float initialSavingsBalance = 0.0f;
        float initialCreditBalance = 0.0f;

        int creditLimit = getCreditLimitBasedOnScore(creditScore);

        checking checkingAccount = new checking(++CSVHandler.currentMaxCheckingId, initialCheckingBalance);
        saving savingAccount = new saving(++CSVHandler.currentMaxSavingId, initialSavingsBalance);
        credit creditAccount = new credit(++CSVHandler.currentMaxCreditId, creditLimit, initialCreditBalance);

        customer newCustomer = new customer(
                userId,
                firstName,
                lastName,
                dob,
                address,
                phoneNumber,
                checkingAccount,
                savingAccount,
                creditAccount
        );
        customers.add(newCustomer);

        System.out.println("New user created successfully!");
    }
    /**
     * The getCreditLimitBasedOnScore is a helper method for addNewUser, it takes the credit score input from the method mentioned
     * to generate a random max credit score
     * @param creditScore : takes the credit score input from addNewUser to generate a random credit score with specific
     * marks.
     */

    public static int getCreditLimitBasedOnScore(int creditScore){ //changed it to public so it can be used by the JUnit test class
        Random rand = new Random();
        if (creditScore <= 580) {
            return 100 + rand.nextInt(600); // $100 - $699
        } else if (creditScore <= 669) {
            return 700 + rand.nextInt(4300); // $700 - $4999
        } else if (creditScore <= 739) {
            return 5000 + rand.nextInt(2500); // $5000 - $7499
        } else if (creditScore <= 799) {
            return 7500 + rand.nextInt(8500); // $7500 - $15999
        } else {
            return 16000 + rand.nextInt(9000); // $16000 - $25000
        }
    }

    /**
     * The findCustomerByName method is a helper method which assists on finding users by name for their intended purposes
     * @param name : The name of the user which is being searched for, as the name of the method implies
     * @return the index of the customer on the customer list
     */
    public static int findCustomerByName(String name, ArrayList<customer> customers) {

        for(int i = 0; i < customers.size(); i++){
            if (customers.get(i).getFullName().equalsIgnoreCase(name)){
                return i;
            }
        }
        return -1; // Customer not found
    }

    public static int findCustomerByAccID(int id, ArrayList<customer> customers) {
        for(int i = 0; i < customers.size(); i++){
            if(customers.get(i).getID() == id){
                return i;
            }


            if(customers.get(i).getID() == id){
                return i;
            }


            if(customers.get(i).getID() == id){
                return i;
            }
        }
        return -1;
    }

    /**
     * The generateUserTransactionFile method generates a transaction file, it takes the current time that the file was requested
     * and prints out the information that it had originally at the moment of requesting the information
     * and the information AFTER operations are made, showing starting balances and the final balances at the moment of requesting the file.
     * @param customer : takes the current customer using the system at the moment as a parameter
     * @return technically returns a .txt file with all the transactions and information requested per the guidelines.
     */
    public static void generateUserTransactionFile(customer customer) {
        // Date Format
        String dateOfStatement = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Create a filename based on the user name and current date
        String fileName = customer.getFullName().replace(" ", "_") + "_Transactions_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".txt";

        // Get current balances
        double checkingStartingBalance = customer.getCheckingAccount().getBalance();
        double savingsStartingBalance = customer.getSavingAccount().getBalance();
        double creditStartingBalance = customer.getCreditAccount().getBalance();

        // Read the log file to get all the transactions of the current user
        List<String> transactions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("log.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(customer.getFullName())) {
                    transactions.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the log file.");
            e.printStackTrace();
        }

        // Sintax of the file
        StringBuilder content = new StringBuilder();
        content.append("User Transactions File\n")
                .append("========================\n")
                .append("Account Information:\n")
                .append("User Name: ").append(customer.getFullName()).append("\n")
                .append("Address: ").append(customer.getAddress()).append("\n")
                .append("Phone Number: ").append(customer.getPhoneNumber()).append("\n")
                .append("\nStarting Balances:\n")
                .append("Checking Balance: $").append(String.format("%.2f", startingCheckingBalance)).append("\n")
                .append("Savings Balance: $").append(String.format("%.2f", startingSavingBalance)).append("\n")
                .append("Credit Balance: $").append(String.format("%.2f", startingCreditBalance)).append("\n\n")
                .append("\nEnding Balances:\n")
                .append("Checking Balance: $").append(String.format("%.2f", checkingStartingBalance)).append("\n")
                .append("Savings Balance: $").append(String.format("%.2f", savingsStartingBalance)).append("\n")
                .append("Credit Balance: $").append(String.format("%.2f", creditStartingBalance)).append("\n\n")
                .append("All Transactions:\n")
                .append("-----------------\n");

        // Add every transaction to the file
        for (String transaction : transactions) {
            content.append(transaction).append("\n");
        }

        // Date of the current time
        content.append("\nDate of Statement: ").append(dateOfStatement).append("\n");

        // Write to a new file
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content.toString());
            System.out.println("User transaction file created: " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while writing the transaction file.");
            e.printStackTrace();
        }
    }

    /**
     * The generateBankStatement method takes a customer from the list and generates a statement with all their information including up to date
     * balances  from their accounts and their personal information, and it also prints out all of their
     * transactions from the log.txt just like the transactions method
     * @param selectedCustomer : the customer from
     */

    public static void generateBankStatement(customer selectedCustomer){
        List<String> transactions = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader("log.txt"))){
            String line;
            while((line = reader.readLine()) != null){

                if(line.contains(selectedCustomer.getFullName())){
                    transactions.add(line);
                }
            }
        }catch(IOException e){
            System.out.println("An error occurred while reading the log file.");
            e.printStackTrace();
        }

        String statement = "Bank Statement for: " + selectedCustomer.getFullName() + "\n" +
                "Address: " + selectedCustomer.getAddress() + "\n" +
                "Phone Number: " + selectedCustomer.getPhoneNumber() + "\n" +
                "-------------------------------\n" +
                "Account Balances:\n" +
                "Checking Account Balance: $" + selectedCustomer.getCheckingAccount().getBalance() + "\n" +
                "Savings Account Balance: $" + selectedCustomer.getSavingAccount().getBalance() + "\n" +
                "Credit Account Balance: $" + selectedCustomer.getCreditAccount().getBalance() + "\n" +
                "-------------------------------\n" +
                "Recent Transactions (Session):\n";


        StringBuilder content = new StringBuilder(statement);
        for (String transaction : transactions) {
            content.append(transaction).append("\n");
        }


        String fileName = "BankStatement_" + selectedCustomer.getID() + ".txt";
        try(PrintWriter pw = new PrintWriter(new FileWriter(fileName))){
            pw.print(content.toString());
            System.out.println("Bank statement successfully generated: " + fileName);
        }catch (IOException e){
            System.out.println("Error: Could not generate bank statement.");
            e.printStackTrace();
        }
    }










}
