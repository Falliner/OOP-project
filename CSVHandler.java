import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

/** The CSVHandler class will handle the csv file operations of the program.
 *
 */
public class CSVHandler {
    public static int currentMaxId; // number of the current id in file
    public static int currentMaxCheckingId = 0; // number of the current checking account id
    public static int currentMaxSavingId = 0; // number of the current savings account id
    public static int currentMaxCreditId = 0; // number of the current credit account id

    /**
     * The readCsv method is for the purpose of reading the file to acquire all the important information about the accounts
     * @param fileName : the name of the file which will be used to take the information out of and later update.
     *
     */
    public static void readCsv(String fileName, ArrayList<customer> customers) {
        try {
            Scanner scanner = new Scanner(new File(fileName));
            Map<String, Integer> columnIndices = new HashMap<>();

            // Read the headers and map column indices
            if (scanner.hasNextLine()) {
                String headerLine = scanner.nextLine();
                String[] headers = headerLine.split(",");
                for (int i = 0; i < headers.length; i++) {
                    columnIndices.put(headers[i].trim(), i);
                }
            }

            // Check if all required headers are present
            String[] requiredHeaders = {"Identification Number", "First Name", "Last Name", "Date of Birth",
                    "Address", "Phone Number", "Checking Account Number",
                    "Checking Starting Balance", "Savings Account Number",
                    "Savings Starting Balance", "Credit Account Number",
                    "Credit Max", "Credit Starting Balance"};

            for (String header : requiredHeaders) {
                if (!columnIndices.containsKey(header)) {
                    System.out.println("Missing header: " + header);
                    return;
                }
            }

            // Read each line of data
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = parseCsvLine(line);

                try {
                    // Safely retrieve each index, as validated above
                    int idIndex = columnIndices.get("Identification Number");
                    int firstNameIndex = columnIndices.get("First Name");
                    int lastNameIndex = columnIndices.get("Last Name");
                    int dobIndex = columnIndices.get("Date of Birth");
                    int addressIndex = columnIndices.get("Address");
                    int phoneNumberIndex = columnIndices.get("Phone Number");
                    int checkingAccountIndex = columnIndices.get("Checking Account Number");
                    int checkingBalanceIndex = columnIndices.get("Checking Starting Balance");
                    int savingAccountIndex = columnIndices.get("Savings Account Number");
                    int savingBalanceIndex = columnIndices.get("Savings Starting Balance");
                    int creditAccountIndex = columnIndices.get("Credit Account Number");
                    int creditMaxIndex = columnIndices.get("Credit Max");
                    int creditBalanceIndex = columnIndices.get("Credit Starting Balance");

                    // Parse and process data as in your original code
                    int id = Integer.parseInt(data[idIndex].trim());
                    int checkingId = Integer.parseInt(data[checkingAccountIndex].trim());
                    int savingId = Integer.parseInt(data[savingAccountIndex].trim());
                    int creditId = Integer.parseInt(data[creditAccountIndex].trim());

                    float checkingBalance = Float.parseFloat(data[checkingBalanceIndex].trim());
                    float savingBalance = Float.parseFloat(data[savingBalanceIndex].trim());
                    float creditBalance = Float.parseFloat(data[creditBalanceIndex].trim());

                    currentMaxId = Math.max(currentMaxId, id);
                    currentMaxCheckingId = Math.max(currentMaxCheckingId, checkingId);
                    currentMaxSavingId = Math.max(currentMaxSavingId, savingId);
                    currentMaxCreditId = Math.max(currentMaxCreditId, creditId);

                    checking checkingAccount = new checking(checkingId, checkingBalance);
                    saving savingAccount = new saving(savingId, savingBalance);
                    credit creditAccount = new credit(creditId, Integer.parseInt(data[creditMaxIndex].trim()), creditBalance);

                    customers.add(new customer(id, data[firstNameIndex].trim(), data[lastNameIndex].trim(),
                            data[dobIndex].trim(), data[addressIndex].trim(),
                            data[phoneNumberIndex].trim(), checkingAccount,
                            savingAccount, creditAccount));

                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Data error on line: " + line);
                    e.printStackTrace();
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            e.printStackTrace();
        }
    }

    /**
     * The updateCsv method is for the purpose of updating the file after performing the specified type of action within the menu,
     * it now directly outputs the changes into a copy of the file
     *
     */
    public static void updateCsv(ArrayList<customer> customers) {

        String newFileName = "CS 3331 - Bank Users Copy.csv";

        try {
            File csvFile = new File(newFileName);
            boolean fileExists = csvFile.exists();


            FileWriter fw = new FileWriter(newFileName, false);//the true in this line was causing it to actually add new lines instead of overwriting the information
            //changing it to false makes it actually overwrite the information, i had to remove the if (!file exists) too so it would print the header!
            PrintWriter pw = new PrintWriter(fw);


            pw.println("Identification Number,First Name,Last Name,Date of Birth,Address,Phone Number,Checking Account Number,Checking Starting Balance,Savings Account Number,Savings Starting Balance,Credit Account Number,Credit Max,Credit Starting Balance");


            for (customer customer : customers) {
                pw.println(
                        customer.getID() + "," +
                                customer.getFirstName() + "," +
                                customer.getLastName() + "," +
                                customer.getDob() + "," +
                                "\"" + customer.getAddress() + "\"," +
                                customer.getPhoneNumber() + "," +
                                customer.getCheckingAccount().getID() + "," +
                                customer.getCheckingAccount().getBalance() + "," +
                                customer.getSavingAccount().getID() + "," +
                                customer.getSavingAccount().getBalance() + "," +
                                customer.getCreditAccount().getID() + "," +
                                customer.getCreditAccount().getCreditLimit() + "," +
                                customer.getCreditAccount().getBalance()
                );
            }

            pw.close();
            System.out.println("Data successfully written to " + newFileName);
        } catch (IOException e) {
            System.out.println("Error: Could not write to file");
            e.printStackTrace();
        }
    }


    /**
     * The parseCsvLine helper method is to parse the current line in the CSV to be properly read, since its now utilizing a hashmap to
     * process the information, it needs this helper method to read the information correctly
     * @param line : The current line of the CSV file to process them and utilize them for their proper actions and operations.
     * @return a string array to read the file properly
     */
    private static String[] parseCsvLine(String line) {
        Pattern pattern = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String[] result = pattern.split(line.trim());
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].replaceAll("^\"|\"$", "").trim();
        }
        return result;
    }



    /**
     * The processTransactions method takes a file named transactions.csv and process immediately the information that is set into the file
     * this is a manager function where they're basically "approving" transactions being made and its essentially massively doing operations
     * on multiple people with proper operations made to every account and properly logged.
     * @param fileName : the name of the file which will be used to take the information out of and later update.
     */
    public static void processTransactions(String fileName, ArrayList<customer> customers) {
        try (Scanner scanner = new Scanner(new File(fileName))) {

            if (scanner.hasNextLine()) scanner.nextLine();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                String fromFirstName;
                String fromLastName;
                String fromAccountType;
                String toFirstName;
                String toLastName;
                String toAccountType;
                float amount;
                int fromCustomerIndex;
                int toCustomerIndex;


                String action = data[3].trim();
                switch (action.toLowerCase()) {
                    case "pays":
                        System.out.println("Pay Action");
                        fromFirstName = data[0].trim();
                        fromLastName = data[1].trim();
                        fromAccountType = data[2].trim();
                        toFirstName = data[4].trim();
                        toLastName = data[5].trim();
                        toAccountType = data[6].trim();
                        amount = Float.parseFloat(data[7].trim());

                        fromCustomerIndex = UserOperations.findCustomerByName(fromFirstName + " " + fromLastName, customers);
                        if (fromCustomerIndex == -1) {
                            System.out.println("Customer not found: " + fromFirstName + " " + fromLastName);
                            continue;
                        }
                        toCustomerIndex = UserOperations.findCustomerByName(toFirstName + " " + toLastName, customers);
                        if (toCustomerIndex == -1) {
                            System.out.println("Customer not found: " + toFirstName + " " + toLastName);
                            continue;
                        }

                        if(fromAccountType.equals("Checking")) {
                            if(customers.get(fromCustomerIndex).getCheckingAccount().getBalance() < amount) {
                                System.out.println("Not Sufficient Balance on Checking Account");
                                continue;
                            }
                        }
                        if(fromAccountType.equals("Savings")) {
                            if(customers.get(fromCustomerIndex).getSavingAccount().getBalance() < amount) {
                                System.out.println("Not Sufficient Balance on Saving Account");
                                continue;
                            }
                        }

                        if(fromAccountType.equals("Checking") && toAccountType.equals("Checking")) {
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(fromCustomerIndex).getCheckingAccount().withdraw(amount);
                            System.out.println(toFirstName + " " + toLastName + " ");
                            customers.get(toCustomerIndex).getCheckingAccount().deposit(amount);
                            System.out.println(customers.get(fromCustomerIndex).getFullName() + " pay " + customers.get(toCustomerIndex).getFullName());
                        } else if (fromAccountType.equals("Savings") && toAccountType.equals("Savings")) {
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(fromCustomerIndex).getSavingAccount().withdraw(amount);
                            System.out.println(toFirstName + " " + toLastName + " ");
                            customers.get(toCustomerIndex).getSavingAccount().deposit(amount);
                            System.out.println(customers.get(fromCustomerIndex).getFullName() + " pay " + customers.get(toCustomerIndex).getFullName());
                        } else if (fromAccountType.equals("Checking") && toAccountType.equals("Savings")) {
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(fromCustomerIndex).getCheckingAccount().withdraw(amount);
                            System.out.println(toFirstName + " " + toLastName + " ");
                            customers.get(toCustomerIndex).getSavingAccount().deposit(amount);
                            System.out.println(customers.get(fromCustomerIndex).getFullName() + " pay " + customers.get(toCustomerIndex).getFullName());
                        } else if (fromAccountType.equals("Savings") && toAccountType.equals("Checking")) {
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(fromCustomerIndex).getSavingAccount().withdraw(amount);
                            System.out.println(toFirstName + " " + toLastName + " ");
                            customers.get(toCustomerIndex).getCheckingAccount().deposit(amount);
                            System.out.println(customers.get(fromCustomerIndex).getFullName() + " pay " + customers.get(toCustomerIndex).getFullName());
                        }
                        break;
                    case "transfers":
                        System.out.println("Transfers Action");
                        fromFirstName = data[0].trim();
                        fromLastName = data[1].trim();
                        fromAccountType = data[2].trim();
                        toFirstName = data[4].trim();
                        toLastName = data[5].trim();
                        toAccountType = data[6].trim();
                        amount = Float.parseFloat(data[7].trim());

                        fromCustomerIndex = UserOperations.findCustomerByName(fromFirstName + " " + fromLastName, customers);
                        if (fromCustomerIndex == -1) {
                            System.out.println("Customer not found: " + fromFirstName + " " + fromLastName);
                            continue;
                        }
                        toCustomerIndex = UserOperations.findCustomerByName(toFirstName + " " + toLastName, customers);
                        if (toCustomerIndex == -1) {
                            System.out.println("Customer not found: " + toFirstName + " " + toLastName);
                            continue;
                        }

                        if(fromAccountType.equals("Checking")) {
                            if(customers.get(fromCustomerIndex).getCheckingAccount().getBalance() < amount) {
                                System.out.println("Not Sufficient Balance on Checking Account");
                                continue;
                            }
                        }
                        if(fromAccountType.equals("Savings")) {
                            if(customers.get(fromCustomerIndex).getSavingAccount().getBalance() < amount) {
                                System.out.println("Not Sufficient Balance on Saving Account");
                                continue;
                            }
                        }

                        if(fromAccountType.equals("Checking") && toAccountType.equals("Savings")) {
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(fromCustomerIndex).getCheckingAccount().withdraw(amount);
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(toCustomerIndex).getSavingAccount().deposit(amount);
                        } else if (fromAccountType.equals("Savings") && toAccountType.equals("Checking")) {
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(fromCustomerIndex).getSavingAccount().withdraw(amount);
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(toCustomerIndex).getCheckingAccount().deposit(amount);
                        } else if (fromAccountType.equals("Credit") && toAccountType.equals("Checking")) {
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(fromCustomerIndex).getCreditAccount().withdraw(amount);
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(toCustomerIndex).getCheckingAccount().deposit(amount);
                        } else if (fromAccountType.equals("Credit") && toAccountType.equals("Savings")) {
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(fromCustomerIndex).getCreditAccount().withdraw(amount);
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(toCustomerIndex).getSavingAccount().deposit(amount);
                        } else if (fromAccountType.equals("Checking") && toAccountType.equals("Credit")) {
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(fromCustomerIndex).getCheckingAccount().withdraw(amount);
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(toCustomerIndex).getCreditAccount().deposit(amount);
                        } else if (fromAccountType.equals("Savings") && toAccountType.equals("Credit")) {
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(fromCustomerIndex).getSavingAccount().withdraw(amount);
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(toCustomerIndex).getCreditAccount().deposit(amount);
                        }
                        System.out.println(customers.get(fromCustomerIndex).getFullName() + " transfer " + amount);
                        break;
                    case "inquires":
                        System.out.println("Inquires Action");
                        fromFirstName = data[0].trim();
                        fromLastName = data[1].trim();
                        fromAccountType = data[2].trim();
                        fromCustomerIndex = UserOperations.findCustomerByName(fromFirstName + " " + fromLastName, customers);
                        if (fromCustomerIndex == -1) {
                            System.out.println("Customer not found: " + fromFirstName + " " + fromLastName);
                            continue;
                        }

                        System.out.println("Customer " + customers.get(fromCustomerIndex).getFullName());

                        if(fromAccountType.equals("Checking")) {
                            System.out.println("Checking Account Balance: " + customers.get(fromCustomerIndex).getCheckingAccount().getBalance());
                        } else if (fromAccountType.equals("Savings")) {
                            System.out.println("Saving Account Balance: " + customers.get(fromCustomerIndex).getSavingAccount().getBalance());
                        } else if (fromAccountType.equals("Credit")) {
                            System.out.println("Credit Account Balance: " + customers.get(fromCustomerIndex).getCreditAccount().getBalance());
                        }

                        break;
                    case "withdraws":
                        System.out.println("Withdraws Action");
                        fromFirstName = data[0].trim();
                        fromLastName = data[1].trim();
                        fromAccountType = data[2].trim();
                        amount = Float.parseFloat(data[7].trim());
                        fromCustomerIndex = UserOperations.findCustomerByName(fromFirstName + " " + fromLastName, customers);
                        if (fromCustomerIndex == -1) {
                            System.out.println("Customer not found: " + fromFirstName + " " + fromLastName);
                            continue;
                        }
                        if(fromAccountType.equals("Checking")) {
                            if(customers.get(fromCustomerIndex).getCheckingAccount().getBalance() < amount) {
                                System.out.println("Not Sufficient Balance on Checking Account");
                                continue;
                            }
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(fromCustomerIndex).getCheckingAccount().withdraw(amount);
                        } else if (fromAccountType.equals("Savings")) {
                            if(customers.get(fromCustomerIndex).getSavingAccount().getBalance() < amount) {
                                System.out.println("Not Sufficient Balance on Saving Account");
                                continue;
                            }
                            System.out.println(fromFirstName + " " + fromLastName + " ");
                            customers.get(fromCustomerIndex).getSavingAccount().withdraw(amount);
                        }

                        break;
                    case "deposits":
                        System.out.println("Deposits Action");
                        toFirstName = data[4].trim();
                        toLastName = data[5].trim();
                        toAccountType = data[6].trim();
                        amount = Float.parseFloat(data[7].trim());
                        toCustomerIndex = UserOperations.findCustomerByName(toFirstName + " " + toLastName, customers);
                        if (toCustomerIndex == -1) {
                            System.out.println("Customer not found: " + toFirstName + " " + toLastName);
                            continue;
                        }
                        if(toAccountType.equals("Checking")) {
                            System.out.println(toFirstName + " " + toLastName + " ");
                            customers.get(toCustomerIndex).getCheckingAccount().deposit(amount);
                        } else if (toAccountType.equals("Savings")) {
                            System.out.println(toFirstName + " " + toLastName + " ");
                            customers.get(toCustomerIndex).getSavingAccount().deposit(amount);
                        }
                        break;
                    default:
                        System.out.println("Unknown action: " + action);
                        break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Transaction file not found.");
            e.printStackTrace();
        }
    }


}
