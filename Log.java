import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/** The Log class will handle all the logging of the program to the log.txt.
 *
 */
public class Log {
    /** The log method will handle the action of writing the messsage to the log.txt
     *
     * @param action : message that it will be writted to the log.txt
     */
    public static void log(String action) {
        try (FileWriter fw = new FileWriter("log.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(action); // Write the action to the log file
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the log file.");
            e.printStackTrace();
        }
    }

    /** The logBalanceInquiry method will handle the log format of the message to a customer inquiring on their balance
     *
     * @param customer : the customer that has inquired their balance.
     * @param accountType : the type of account the customer checked.
     */
    public static void logBalanceInquiry(customer customer, String accountType) {
        String logMessage = "";

        switch (accountType) {
            case "Checking": // Checking Account
                logMessage = customer.getFullName() + " made a balance inquiry on Checking-"
                        + customer.getCheckingAccount().getID() + ". "
                        + customer.getFullName() + "'s Balance for Checking-"
                        + customer.getCheckingAccount().getID() + ": "
                        + customer.getCheckingAccount().getBalance();
                break;
            case "Savings": // Savings Account
                logMessage = customer.getFullName() + " made a balance inquiry on Saving-"
                        + customer.getSavingAccount().getID() + ". "
                        + customer.getFullName() + "'s Balance for Saving-"
                        + customer.getSavingAccount().getID() + ": "
                        + customer.getSavingAccount().getBalance();
                break;
            case "Credit": // Credit Account
                logMessage = customer.getFullName() + " made a balance inquiry on Credit-"
                        + customer.getCreditAccount().getID() + ". "
                        + customer.getFullName() + "'s Balance for Credit-"
                        + customer.getCreditAccount().getID() + ": "
                        + customer.getCreditAccount().getBalance();
                break;
            default:
                logMessage = "Invalid account type for logging.";
        }

        //log method to write the message to the log file
        log(logMessage);
    }

    /** The logDeposit method will handle the log format of the message to a customer depositing on their account
     *
     * @param customer: the customer that has deposited to their account.
     * @param accountType : the type of the account that the customer used.
     * @param amount : the amount deposited.
     */
    public static void logDeposit(customer customer, String accountType, int amount) {
        String logMessage = "";
        switch (accountType) {
            case "Checking":
                logMessage = customer.getFullName() + " deposited $" + amount + " into Checking account "
                        + customer.getCheckingAccount().getID() + ". " + "\n"
                        + customer.getFullName() + " new balance for  "
                        + customer.getCheckingAccount().getID() + ": "
                        + customer.getCheckingAccount().getBalance();
                break;
            case "Savings":
                logMessage = customer.getFullName() + " deposited $" + amount + " into Savings account "
                        + customer.getSavingAccount().getID() + ". " + "\n"
                        + customer.getFullName() + " new balance for  "
                        + customer.getSavingAccount().getID() + ": "
                        + customer.getSavingAccount().getBalance();
                break;
            default:
                logMessage = "Invalid account type for logging deposit.";
        }
        log(logMessage);
    }

    /** The logWithdrawal method will handle the log format of the message to a customer withdrawing on their account
     *
     * @param customer : the customer that has withdrawn from their account.
     * @param accountType : the type of account they have withdrawn from.
     * @param amount: the amount withdrawn.
     */
    // Method to log withdrawal actions
    public static void logWithdrawal(customer customer, String accountType, int amount) {
        String logMessage = "";

        switch (accountType) {
            case "Checking": // Checking Account
                logMessage = customer.getFullName() + " withdrew $" + amount + " from Checking account "
                        + customer.getCheckingAccount().getID() + ".\n"
                        + customer.getFullName() + "'s new balance for Checking-"
                        + customer.getCheckingAccount().getID() + ": "
                        + customer.getCheckingAccount().getBalance();
                break;
            case "Savings": // Savings Account
                logMessage = customer.getFullName() + " withdrew $" + amount + " from Saving account "
                        + customer.getSavingAccount().getID() + ".\n"
                        + customer.getFullName() + "'s new balance for Saving-"
                        + customer.getSavingAccount().getID() + ": "
                        + customer.getSavingAccount().getBalance();
                break;
            default:
                logMessage = "Invalid account type for logging withdrawal.";
        }

        //log method to write the message to the log file
        log(logMessage);
    }

    /**The logCreditPayment method will handle the log format of the message to a customer payment to their credit account.
     *
     * @param customer : the customer that has made a payment to their credit account.
     * @param amount : the amount paid.
     */
    public static void logCreditPayment(customer customer, int amount) {
        String logMessage = "";
        logMessage += customer.getFullName() + " paid $" + amount + " towards his credit " + customer.getCreditAccount().getID() + "." +
                "\n" + customer.getFullName() + " new balance for  " + customer.getCreditAccount().getID() + " : " + customer.getCreditAccount().getBalance();
        log(logMessage);
    }

    /** The logTransfer method will handle the log format of the message to a customer transferring money between accounts.
     *
     * @param customer : the customer that has performed the transfer.
     * @param accountType : the account type that the customer has withdrawn from to deposit to their other account.
     * @param amount : the amount that they have transferred.
     */
    public static void logTransfer(customer customer,String accountType, int amount) {
        String logMessage = "";

        switch (accountType) {
            case "Checking": // Checking Account
                logMessage = customer.getFullName() + " transferred $" + amount + " from Checking account "
                        + customer.getCheckingAccount().getID() + " to Savings account " + customer.getSavingAccount().getID();
                break;
            case "Savings": // Savings Account
                logMessage = customer.getFullName() + " transferred $" + amount + " from Savings account "
                        + customer.getSavingAccount().getID() + " to Checking Account " + customer.getCheckingAccount().getID();
                break;
            default:
                logMessage = "Invalid account type for logging transfer.";
        }

        //log method to write the message to the log file
        log(logMessage);
    }

    /** The logPayment method will handle the log format of the message to a customer payment to another user.
     *
     * @param payer : the customer that will pay.
     * @param payee : the customer that gets pay.
     * @param amount : the amount paid.
     */
    public static void logPayment(customer payer, customer payee, int amount) {
        String logMessage = payer.getFullName() + " paid $" + amount + " to " + payee.getFullName() + ".\n"
                + payer.getFullName() + "'s new Checking Account balance: "
                + payer.getCheckingAccount().getBalance() + ".\n"
                + payee.getFullName() + "'s new Checking Account balance: "
                + payee.getCheckingAccount().getBalance();
        log(logMessage);
    }

    /** The logManager method will handle the log format of the message to a manager inquiring on all the accounts or a specific account of a customer.
     *
     * @param customer : the customer that the manager has inquiry on.
     * @param Action : the type of action the manager has performed.
     */
    public static void logManager(customer customer, String Action) {
        String logMessage = "";

        switch (Action) {
            case "accountInquiry": // Checking Account
                logMessage = "Manager inquired on customer " + customer.getFullName() + "'s accounts.";
                break;
            case "checkingInquiry": // Savings Account
                logMessage = "Manager inquired on customer " + customer.getFullName() + "'s checking account";
                break;
            case "savingsInquiry":
                logMessage = "Manager inquired on customer " + customer.getFullName() + "'s savings account";
                break;
            case "creditInquiry":
                logMessage = "Manager inquired on customer " + customer.getFullName() + "'s credit account";
                break;
            case "generateStatement":
                logMessage = "Manager generated statement for: " + customer.getFullName();
                break;
            default:
                logMessage = "Invalid action for logging manager.";
        }
        //log method to write the message to the log file
        log(logMessage);
    }

    /** The logManagerAction method will handle the log format of the message to a manager adding a customer, or processing transactions.
     *
     * @param action : the action that the manager has performed.
     */
    public static void logManagerAction(String action) {
        String logMessage = "";
        switch (action) {
            case "addUser":
                logMessage = "Manager added customer";
                break;
            case "transactionsProc":
                logMessage = "Manager processed all transactions";
                break;
            default:
                logMessage = "Invalid account type for logging manager action.";
        }
        log(logMessage);
    }

}