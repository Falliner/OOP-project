/** The credit subclass of Account is the one, as the name implies, related to operations pertaining to credit accounts
 */
public class credit extends account{
	
	private int creditLimit;

	public credit(int id, int creditLimit, float balance) {
        super(id,balance); 
        this.creditLimit = creditLimit;
    }

    public int getCreditLimit(){
    	return creditLimit;
    }
    
	public void payCredit(float amount){
		if(balance + amount > creditLimit){
			System.out.println("You cannot go over your credit limit.");
		}else{
			balance += amount;
			System.out.println("Paid $" + amount + " towards credit balance.");
		}

    }
    
    public void deposit(float amount){
        balance += amount;
        System.out.println("Deposited $" + amount + " to Savings account.");
        
    }

    public void withdraw(float amount){
        balance -= amount;
        System.out.println("Withdrew $" + amount + " from Savings account.");
        
    }


}