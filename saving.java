/** The saving subclass of Account is the one, as the name implies, related to operations pertaining to saving accounts
 */
public class saving extends account{
	
	public saving(int id, float balance){
		super(id,balance);
	}

	public void deposit(float amount){
        balance += amount;
        System.out.println("Deposited $" + amount + " to Savings account.");
        
    }

    public void withdraw(float amount){
        balance -= amount;
        System.out.println("Withdrawn $" + amount + " to Savings account.");
    }

    
}