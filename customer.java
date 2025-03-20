/** The customer subclass is the implementation of the person interface
 * this class holds all the personal information related to customers as well as having methods related to recovering information that 
 * should be available based on the role requesting it, i.e, managers are able to utilize more information as well as inputting when creating
 * new customers(users)
 */
public class customer implements person{
	private checking checkingAccount;
    private saving savingAccount;
    private credit creditAccount;
    private int id;
    protected String firstName;
    protected String lastName;
    protected String dob;
    protected String address;
    protected String phoneNumber;

    public customer(int id, String firstName, String lastName, String dob, String address, String phoneNumber, checking checkingAccount, saving savingAccount, credit creditAccount){
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.id = id;
        this.checkingAccount = checkingAccount;
        this.savingAccount = savingAccount;
        this.creditAccount = creditAccount;
    }

    // Methods to access the accounts
    public checking getCheckingAccount(){
        return checkingAccount;
    }

    public saving getSavingAccount(){
        return savingAccount;
    }

    public credit getCreditAccount(){
        return creditAccount;
    }

    public void inquireBalance(String accountType){
    		System.out.println("Checking balance: $" + checkingAccount.getBalance());
    		System.out.println("Checking balance: $" + savingAccount.getBalance());
    		System.out.println("Credit balance: $" + creditAccount.getBalance());
    	
    }

    public int getID() {
        return id;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName(){
        return lastName;
    }
    public String getDob(){return dob;}
    public String getAddress(){return address;}
    public String getPhoneNumber(){return phoneNumber;}

    public String getFullName() {
        return firstName + " " + lastName;
    }
}