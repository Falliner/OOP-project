/** The checking subclass of Account is the one, as the name implies, related to operations pertaining to checking accounts
 */
public class checking extends account{

	public checking(int id, float balance){
		super(id, balance);
	}
	
	public void deposit(float amount){
            balance += amount;
            System.out.println("Deposited $" + amount + " to checking account");

    }

    public void withdraw(float amount){
            balance -= amount;
            System.out.println("Withdrew $" + amount + " from Checking account");
    }
	
	


}