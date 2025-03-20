import java.util.ArrayList;
import java.util.Scanner;

/** TransactionHandler class deals with the transactions made for the costumer and bank manager
 */
public class TransactionHandler {

    /** The handleWithdrawal method handles the withdrawal transaction of the customer account, checking how much they want to withdraw, if the amount is less than their current balance
     *  and which account they want to use.
     * @param customer : the customer account that we want to withdraw from.
     * @param accountType : the type of the account that the customer wants to withdraw from.
     * @param userInput : the input of the user using the console.
     */
    public static void handleWithdrawal(customer customer, String accountType, Scanner userInput) {
        System.out.println("How much do you want to withdraw?:");
        int money = userInput.nextInt();

        account account = getAccountByType(customer, accountType);

        if (account == null) {
            System.out.println("Invalid account type for withdrawal.");
            return;
        }

        while (account.getBalance() < money) {
            System.out.println("Insufficient Balance, Please Enter a Correct Amount: ");
            money = userInput.nextInt();
        }

        account.withdraw(money);
        Log.logWithdrawal(customer, accountType, money);
        System.out.println("Withdrawal successful! Your new balance is: " + account.getBalance());
    }

    /** The handleDeposit method will handle the transaction dealing with deposits from the customer account, checking how much they want to deposit, and to what account they want to use
     * @param customer : the customer that wants to deposit.
     * @param accountType : the account type that the customer wants to deposit to.
     * @param userInput: the input of the user using the console.
     */
    public static void handleDeposit(customer customer, String accountType, Scanner userInput) {
        System.out.println("How much do you want to deposit?:");
        int money = userInput.nextInt();

        account account = getAccountByType(customer, accountType);

        if (account == null) {
            System.out.println("Invalid account type for deposit.");
            return;
        }

        account.deposit(money);
        Log.logDeposit(customer, accountType, money);
        System.out.println("Deposit successful! Your new balance is: " + account.getBalance());
    }

    /** The handleBalanceInquiry method will handle the balance inquiry transaction of the customer, checking which account type they want to know the balance of.
     * @param customer : the customer that we will want to know the balance for.
     * @param accountType : the account type that the customer wants to know the balance of.
     */
    public static void handleBalanceInquiry(customer customer, String accountType) {
        account account = getAccountByType(customer, accountType);

        if (account == null) {
            System.out.println("Invalid account type selected.");
            return;
        }

        System.out.println(accountType + " Account Balance: " + account.getBalance());
        Log.logBalanceInquiry(customer, accountType);
    }

    /** The handleCreditPayment method will handle the credit payment transaction of the customer to their credit account
     * @param customer : the customer that we want to perform the transaction.
     * @param paymentAmount : the amount that the customer wants to pay to their credit balance.
     */
    public static void handleCreditPayment(customer customer, int paymentAmount) {

        // Accessing the Credit account of the customer
        credit creditAccount = customer.getCreditAccount();

        // Make the payment on the Credit account
        creditAccount.payCredit(paymentAmount);

        // Log the credit payment
        Log.logCreditPayment(customer, paymentAmount);

        // Provide feedback to the user
        System.out.println("Payment of $" + paymentAmount + " successfully made towards your credit balance.");
    }

    /** The handleTransfer method will handle the transfer transaction the
     * customer account and checking which direction they want to transfer, and if the amount they want to transfer is less or equal than their current balance.
     * @param customer: the customer that wants to transfer money between accounts.
     * @param transferDirection : the direction that the customer wants to transfer money, a checking to savings or b savings to checking.
     * @param transferAmount: the amount that the customer wants to transfer.
     */
    public static void handleTransfer(customer customer, String transferDirection, int transferAmount) {
        account fromAccount = null;
        account toAccount = null;
        String fromAccountType = "";
        String toAccountType = "";

        // Determine which accounts are involved in the transfer based on direction
        if (transferDirection.equalsIgnoreCase("A")) { // Checking to Savings
            fromAccount = customer.getCheckingAccount();
            toAccount = customer.getSavingAccount();
            fromAccountType = "Checking";
            toAccountType = "Savings";
        } else if (transferDirection.equalsIgnoreCase("B")) { // Savings to Checking
            fromAccount = customer.getSavingAccount();
            toAccount = customer.getCheckingAccount();
            fromAccountType = "Savings";
            toAccountType = "Checking";
        }

        // Check if valid accounts were selected
        if (fromAccount == null || toAccount == null) {
            System.out.println("Invalid account selection for transfer.");
            return;
        }

        // Check if the balance is sufficient for the transfer
        if (fromAccount.getBalance() < transferAmount) {
            System.out.println("Insufficient balance in the source account.");
            return;
        }
        
        // Perform the transfer: Withdraw from source and deposit into destination account
        fromAccount.withdraw(transferAmount);
        toAccount.deposit(transferAmount);

        // Log the transfer
        Log.logTransfer(customer, fromAccountType, transferAmount);

        // Provide feedback to the user
        System.out.println("Transfer of $" + transferAmount + " completed from " + fromAccountType + " to " + toAccountType);
    }

    /** THe handlePayment method will handle the payment transaction of the current customer and the customer that they want to pay to.
     * @param customer: the customer that wants to pay.
     * @param paidCustomer : the customer that they want to pay to.
     * @param paymentAmount : the amount that the current customer wants to pay to the paidCustomer.
     * @param userInput: the input of the user using the console.
     */
    public static void handlePayment(customer customer, customer paidCustomer, int paymentAmount, Scanner userInput) {

        account account = getAccountByType(customer, "Checking");
        account paidaccount = getAccountByType(paidCustomer, "Checking");


        while (account.getBalance() < paymentAmount) {
            System.out.println("Insufficient Balance, Please Enter a Correct Amount: ");
            paymentAmount = userInput.nextInt();
        }

        account.withdraw(paymentAmount);
        paidaccount.deposit(paymentAmount);
        Log.logPayment(customer, paidCustomer, paymentAmount);
        System.out.println("Payment successful! Your new balance is: " + account.getBalance());
    }

    /** The managerInquiry method will handle the manager inquiry of the customers accounts, either by name or account number.
     * @param selection : the selection of the manager whether it wants to search by name or number.
     * @param userInput : the input of the user using the console.
     * @param customers : the array lists of the current customers in the database.
     */
    public static void managerInquiry( String selection, Scanner userInput, ArrayList<customer> customers) {

        if (selection.equalsIgnoreCase("A")) {
            System.out.println("Whose account are you looking for?");
            String menuText = userInput.nextLine();
            int index = UserOperations.findCustomerByName(menuText, customers);
            while (index == -1) {
                System.out.println("User Name not found, Please Enter a valid user name:");
                String userName = userInput.nextLine();
                index = UserOperations.findCustomerByName(userName, customers);
            }
            System.out.println("Customer " + customers.get(index).getFullName() + " accounts: ");
            System.out.println("Checking Account Balance: " + customers.get(index).getCheckingAccount().getBalance());
            System.out.println("Savings Account Balance: " + customers.get(index).getSavingAccount().getBalance());
            System.out.println("Credit Account Balance: " + customers.get(index).getCreditAccount().getBalance());
            Log.logManager(customers.get(index),"accountInquiry");
        } else if (selection.equalsIgnoreCase("B")){ 
            System.out.println("What is the account type? ");
            String menuText = userInput.next();
            userInput.nextLine();
            int accountID;
            int accIndex;
            System.out.println("What is the account's USER number?(Not Account number!): ");
                        accountID = userInput.nextInt();
                        accIndex = UserOperations.findCustomerByAccID(accountID, customers);
            while(accIndex == -1){
                System.out.println("Account ID not found, Please Enter a valid account ID:");
                accountID = userInput.nextInt();
                accIndex = UserOperations.findCustomerByAccID(accountID, customers);
            }


            if(menuText.equalsIgnoreCase("Checking")){
                    System.out.println("Customer " + customers.get(accIndex).getFullName() + " checking account: ");
                    System.out.println("Checking Account Balance: " + customers.get(accIndex).getCheckingAccount().getBalance());
                    Log.logManager(customers.get(accIndex),"checkingInquiry");
            }else if(menuText.equalsIgnoreCase("Savings")){
                    System.out.println("Customer " + customers.get(accIndex).getFullName() + " saving account: ");
                    System.out.println("Checking Account Balance: " + customers.get(accIndex).getSavingAccount().getBalance());
                    Log.logManager(customers.get(accIndex),"savingsInquiry");
            }else if(menuText.equalsIgnoreCase("Credit")){
                    System.out.println("Customer " + customers.get(accIndex).getFullName() + " credit account: ");
                    System.out.println("Checking Account Balance: " + customers.get(accIndex).getCreditAccount().getBalance());
                    Log.logManager(customers.get(accIndex),"creditInquiry");

            }
        }
    }

    /** The generateStatement method will handle the generation of the statement of the users.
     * @param selection : the selection that the manager will decide, depending if they want to enter the name or id of the account.
     * @param userInput : the user input using the console.
     * @param customers : the array list that holds the information of the customers.
     */

    public static void generateStatement(String selection, Scanner userInput, ArrayList<customer> customers) {
        if(selection.equalsIgnoreCase("A")){
            System.out.println("Which person are you generating the statement?: ");
            String menuText = userInput.nextLine();
            int index = UserOperations.findCustomerByName(menuText, customers);
            while (index == -1) {
                System.out.println("UserName not found, Please enter a valid user name:");
                String userName = userInput.nextLine();
                index = UserOperations.findCustomerByName(userName, customers);
            }
                System.out.println("Generating statement for: " + customers.get(index).getFullName());
                UserOperations.generateBankStatement(customers.get(index));
                Log.logManager(customers.get(index),"generateStatement");

        } else if(selection.equalsIgnoreCase("B")){
            System.out.println("Input the ID of the person you are generating a bank statement for: ");
            int accID = userInput.nextInt();
            int accIndex = UserOperations.findCustomerByAccID(accID, customers);
            while(accIndex == -1){
                System.out.println("User ID not found, Please enter a valid ID:");
                accID = userInput.nextInt();
                accIndex = UserOperations.findCustomerByAccID(accID, customers);
            }
                System.out.println("Generating statement for: " + customers.get(accIndex).getFullName());
                UserOperations.generateBankStatement(customers.get(accIndex));
                Log.logManager(customers.get(accIndex),"generateStatement");


        }
    }


    /** The getAccountType method is a helper method that will get the account type from the customer class.
     * @param customer: the customer that we want to get the account from.
     * @param accountType: the type of the customer account that we want to return.
     * @return the customer account
     */

    // Helper method to get an account by type
    private static account getAccountByType(customer customer, String accountType) {
        switch (accountType) {
            case "Checking":
                return customer.getCheckingAccount();
            case "Savings":
                return customer.getSavingAccount();
            case "Credit":
                return customer.getCreditAccount();
            default:
                return null; // Invalid account type
        }
    }

}