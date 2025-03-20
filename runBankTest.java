import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Scanner; 

class runBankTest{
    private ArrayList<customer> customers;

    @BeforeEach
    void setUp(){
        //initialize a list of customers for testing
        customers = new ArrayList<>();
        checking checkingAcc1 = new checking(1, 500.0f);
        saving savingAcc1 = new saving(1, 1000.0f);
        credit creditAcc1 = new credit(1, 2000, 500.0f);
        customer customer1 = new customer(1, "John", "Doe", "01-01-1990", "123 Main St", "1234567890", checkingAcc1, savingAcc1, creditAcc1);

        checking checkingAcc2 = new checking(2, 300.0f);
        saving savingAcc2 = new saving(2, 1500.0f);
        credit creditAcc2 = new credit(2, 2500, 800.0f);
        customer customer2 = new customer(2, "Jane", "Smith", "02-02-1992", "456 Elm St", "0987654321", checkingAcc2, savingAcc2, creditAcc2);

        customers.add(customer1);
        customers.add(customer2);
    }

    @AfterEach
    void tearDown(){
        customers = null;
    }

    @Test
    void testFindCustomerByName(){
        int index = UserOperations.findCustomerByName("John Doe", customers);
        assertEquals(0, index, "Customer 'John Doe' should be at index 0.");

        index = UserOperations.findCustomerByName("Alice Brown", customers);
        assertEquals(-1, index, "Customer 'Alice Brown' should not be found.");
    }

    @Test
    void testFindCustomerByAccID(){
        int index = UserOperations.findCustomerByAccID(2, customers);
        assertEquals(1, index, "Customer with ID 2 should be at index 1.");

        index = UserOperations.findCustomerByAccID(99, customers);
        assertEquals(-1, index, "Customer with ID 99 should not be found.");
    }

    @Test
    void testGenerateBankStatement(){
        customer testCustomer = customers.get(0);

        UserOperations.generateBankStatement(testCustomer);

        String expectedFileName = "BankStatement_" + testCustomer.getID() + ".txt";
        java.io.File file = new java.io.File(expectedFileName);
        assertTrue(file.exists(), "Bank statement file should be created.");

        
        if (file.exists()){
            file.delete();
        }
    }

    @Test
    void testReadCsv(){
        String testFileName = "CS 3331 - Bank Users.csv";

      
        try(FileWriter fw = new FileWriter(testFileName); PrintWriter pw = new PrintWriter(fw)){
            pw.println("Identification Number,First Name,Last Name,Date of Birth,Address,Phone Number,Checking Account Number,Checking Starting Balance,Savings Account Number,Savings Starting Balance,Credit Account Number,Credit Max,Credit Starting Balance");
            pw.println("3,Emily,Jones,03-03-1993,\"789 Pine St\",\"1122334455\",3,600.0,3,800.0,3,1500,400.0");
        }catch(Exception e){
            fail("Setup for testReadCsv failed: " + e.getMessage());
        }

        
        ArrayList<customer> testCustomers = new ArrayList<>();
        CSVHandler.readCsv(testFileName, testCustomers);

        assertEquals(1, testCustomers.size(), "There should be 1 customer read from the file.");
        customer readCustomer = testCustomers.get(0);
        assertEquals("Emily", readCustomer.getFirstName(), "First name should match.");
        assertEquals(3, readCustomer.getCheckingAccount().getID(), "Checking account ID should match.");

    
    }

    @Test
    void testUpdateCsv(){
        String testFileName = "CS 3331 - Bank Users-Copy.csv"; 
    
        CSVHandler.updateCsv(customers);

        File file = new File(testFileName);
        assertTrue(file.exists(), "The updated CSV file should be created.");

        file.delete();
    }
@Test
    void testHandleWithdrawal() {
        customer testCustomer = customers.get(0);
        Scanner mockInput = new Scanner("200"); 

        runBank.handleWithdrawal(testCustomer, "Checking", mockInput);

        assertEquals(300.0f, testCustomer.getCheckingAccount().getBalance(), "Balance should be reduced by withdrawal amount.");
    }

    @Test
    void testHandleDeposit() {
        customer testCustomer = customers.get(1);
        Scanner mockInput = new Scanner("500"); 

        runBank.handleDeposit(testCustomer, "Savings", mockInput);

        assertEquals(2000.0f, testCustomer.getSavingAccount().getBalance(), "Balance should be increased by deposit amount.");
    }

    @Test
    void testHandleBalanceInquiry(){
        customer testCustomer = customers.get(0);
    
        runBank.handleBalanceInquiry(testCustomer, "Credit");
        assertEquals(500.0f, testCustomer.getCreditAccount().getBalance(), "Should correctly return the credit balance.");
    }

    @Test
    void testHandleCreditPayment(){
        customer testCustomer = customers.get(1);

        runBank.handleCreditPayment(testCustomer, 300);

        assertEquals(500.0f, testCustomer.getCreditAccount().getBalance(), "Credit balance should decrease by the payment amount.");
    }

    @Test
    void testHandleTransfer(){
        customer testCustomer = customers.get(0);

        runBank.handleTransfer(testCustomer, "A", 100); 

        assertEquals(400.0f, testCustomer.getSavingAccount().getBalance(), "Savings account should be increased by the transfer amount.");
        assertEquals(400.0f, testCustomer.getCheckingAccount().getBalance(), "Checking account should be decreased by the transfer amount.");
    }

}  
