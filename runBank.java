import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;


/**
 * The runBank class is used as the main for the program, it will inquire about balance of a written person within a file
 * detecting that said person (by user input) is within the ranks of bank customers, allowing them to do multiple operations
 * such as reading user balance, able to deposit money into an account, withdraw from it
 * and deposit/pay another user, as well as it will present the information.  
 * 
 * This work is to be done as a team. It is not permitted to share, reproduce, or alter
 * any part of this assignment for any purpose. Students are not permitted to share
 * code, upload this assignment online in any form, or view/receive/modify code
 * written by anyone else. This assignment is part of an academic course at The
 * University of Texas at El Paso and a grade will be assigned for the work produced
 * individually by the student.
 * 
 * Professor: Doctor Bhanukiran Gurijala
 * Course: CS 3331 – Advanced Object-Oriented Programming 
 * CRN: 15799
 * Programming Assignment 2: Project part 2
 * 
 * 
 * Today's date: 10/28/2024
 * Due Date: 11/12/2024
 * 
 * @author Javier Falliner, Carlos Banda Facio
 */
public class runBank{
    public static ArrayList<customer> customers = new ArrayList<>(); // Array that holds our customers information


    /**
     * The main holds the menu and all the calls towards the classes to perform the correct operations, its divided in two menus for different operations
     * the manager functionality that only holds two options (inquire and exit), and the User menu where after inputting the name of the person whose the operations they will be made on
     * they are able to operate all the menu and their functionalities, the only difference in between the both is that after exiting the User menu
     * the CSV file is updated with the new information, while the Manager only exits the menu and is only able to inquire on customer balances
     *
     */
public static void main(String [] args){


    File csvFile = new File("CS 3331 - Bank Users Copy.csv");
    String fileName = csvFile.exists() ? "CS 3331 - Bank Users Copy.csv" : "CS 3331 - Bank Users.csv";
    CSVHandler.readCsv(fileName, customers);//reads either or, if the copy file does not exist it reads from the original file

    Scanner userInput = new Scanner(System.in);
    String userName = " ";
    int menu = 0; 
    String menuText = " "; 
    int money = 0;

    System.out.println("Are you a Bank Manager or an Existing User? (Type 'Manager' or 'User')");
    String role = userInput.nextLine();
    boolean isManager = role.equalsIgnoreCase("Manager");
    boolean isExistingUser = role.equalsIgnoreCase("User");

    //Keep asking for input until the user enters a valid option
    while (!isManager && !isExistingUser) {
        System.out.println("Please enter a valid option (Type 'Manager' or 'User')");
        role = userInput.nextLine();
        isManager = role.equalsIgnoreCase("Manager");
        isExistingUser = role.equalsIgnoreCase("User");

    }
    int index = UserOperations.findCustomerByName(userName, customers);

    if(isExistingUser){
        System.out.println("Please enter your name:");
        userName = userInput.nextLine();
        index = UserOperations.findCustomerByName(userName, customers);

        while (index == -1){
            System.out.println("User Name no found, Please Enter a valid user name:");
            userName = userInput.nextLine();
            index = UserOperations.findCustomerByName(userName, customers);
        }

        System.out.println("Please enter your password: ");
        String password = userInput.nextLine();
        while(!password.matches("^[a-zA-Z0-9]+$")){//decided against doing a method and checked something if the ^[a-zA-Z0-9]+ from bash also worked in java, just had to add a + 
            System.out.println("Invalid password, please try again: ");
            //nothing special in this, just checks that the password is alphanumeric, no spaces and no special characters (can't do daniel@ 2424 or something along those lines)
            password = userInput.nextLine();
        }
        
        UserOperations.startingCheckingBalance = customers.get(index).getCheckingAccount().getBalance();
        UserOperations.startingSavingBalance = customers.get(index).getSavingAccount().getBalance();
        UserOperations.startingCreditBalance = customers.get(index).getCreditAccount().getBalance();
    }

    while(menu != 8){


        System.out.println("Welcome to Bank of El Paso!");

        if (isManager){
            System.out.println("What would you like to do: ");
            System.out.println("1. Inquire Account Information \n2. Create New User \n3. Transactions \n4. Generate Bank Statement \n5. Exit");
        }else{
            System.out.println("What would you like to do: ");
            System.out.println("1. Inquire about balance \n2. Make a deposit \n3. Withdraw money \n4. Make a Credit payment \n5. Make a Transfer between accounts \n6. Pay another user \n7. Generate all the transactions made \n8. Exit");
        }


        if (userInput.hasNextInt()){
            menu = userInput.nextInt();
            userInput.nextLine();
            if (!isManager) { //user operations
                switch(menu) {
                    case 1:
                        System.out.println("Which type of account do you want to know the balance? \nA. Checking. \nB. Savings. \nC. Credit");
                        menuText = userInput.next();

                        switch (menuText.toUpperCase()) {
                            case "A":
                                TransactionHandler.handleBalanceInquiry(customers.get(index), "Checking");
                                break;
                            case "B":
                                TransactionHandler.handleBalanceInquiry(customers.get(index), "Savings");
                                break;
                            case "C":
                                TransactionHandler.handleBalanceInquiry(customers.get(index), "Credit");
                                break;
                            default:
                                System.out.println("Invalid account type selected.");
                        }
                        break;

                    case 2:
                        System.out.println("Which type of account do you want to deposit to? \nA. Checking. \nB. Savings.");
                        menuText = userInput.next();

                        switch (menuText.toUpperCase()) {
                            case "A": // Checking Account
                                TransactionHandler.handleDeposit(customers.get(index), "Checking", userInput);
                                break;
                            case "B": // Savings Account
                                TransactionHandler.handleDeposit(customers.get(index), "Savings", userInput);
                                break;
                            default:
                                System.out.println("Invalid account type selected.");
                        }
                        break;
                    case 3:
                        System.out.println("Which type of account do you want to withdraw from? \nA. Checking. \nB. Savings.");
                        menuText = userInput.next();

                        switch (menuText.toUpperCase()) {
                                case "A": // Checking Account
                                TransactionHandler.handleWithdrawal(customers.get(index), "Checking", userInput);
                                break;
                                case "B": // Savings Account
                                    TransactionHandler.handleWithdrawal(customers.get(index), "Savings", userInput);
                                    break;
                                default:
                                    System.out.println("Invalid account type selected.");
                            }
                        break;
                    case 4:
                        System.out.println("How much are you paying towards your Credit?: ");
                        money = userInput.nextInt();
                        TransactionHandler.handleCreditPayment(customers.get(index), money);
                        break;
                    case 5:
                        System.out.println("Which type of account are you using? \nA. Checking To Savings. \nB. Savings To Checking.");
                        menuText = userInput.next();
                        System.out.println("How much do you want to transfer?: ");
                        money = userInput.nextInt();
                        TransactionHandler.handleTransfer(customers.get(index), menuText, money);
                        break;
                    case 6:
                        System.out.println("Which user are you paying?: ");
                        String paidCust = userInput.nextLine();
                        int paidIndex = UserOperations.findCustomerByName(paidCust, customers);
                            while(paidIndex == -1 || paidIndex == index){
                                System.out.println("User not found or you tried to input your own user, Please Enter a valid User:");
                                paidCust = userInput.nextLine();
                                paidIndex = UserOperations.findCustomerByName(paidCust, customers);
                            }
                            System.out.println("How much do you want to pay " + customers.get(paidIndex).getFullName() + "?");
                            money = userInput.nextInt();
                            TransactionHandler.handlePayment(customers.get(index), customers.get(paidIndex), money, userInput);
                        break;
                    case 7:
                        UserOperations.generateUserTransactionFile(customers.get(index));
                        break;


                    case 8:
                        System.out.println("Thank you for using our services, closing application.");
                        CSVHandler.updateCsv(customers);
                        System.exit(0);
                        break;


                    default:
                        System.out.println("Invalid option.");
                        break;
                }

            } else if (isManager) {
                // Bank Manager operations
                switch(menu){
                    case 1:
                        System.out.println("A. Inquire by account name. \nB. Inquire account by type/number.");
                        menuText = userInput.next();
                        userInput.nextLine();
                        TransactionHandler.managerInquiry(menuText,userInput,customers);
                        break;

                    case 2:
                        UserOperations.addNewUser(userInput, customers);
                        Log.logManagerAction("addUser");
                        break;

                    case 3:
                        CSVHandler.processTransactions("Transactions.csv", customers);
                        System.out.println("All Transactions processed.");
                        Log.logManagerAction("transactionsProc");
                        break;

                    case 4:
                        System.out.println("A. Generate statement by customer name. \nB. Generate statement by ID of Customer.");
                        menuText = userInput.next();
                        userInput.nextLine();
                        TransactionHandler.generateStatement(menuText,userInput,customers);
                        break;

                    case 5: 
                        System.out.println("Thank you for using our services, closing application.");
                        CSVHandler.updateCsv(customers);
                        System.exit(0);
                        break;
                }

            } else {
                System.out.println("Invalid option for your role.");
            }
        }
    }
}

}

