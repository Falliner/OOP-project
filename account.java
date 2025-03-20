/** The Account class is an abstract class that holds the methods and basic information related to the accounts, its subclasses, 
 * checking, credit and savings, handle the actual operations related to the specified account 
 */
public abstract class account{
    protected int id;
    protected float balance;


    public account(int id, float balance){
        this.id = id;
        this.balance = balance;
    }

    public int getID(){
        return id;
    }

    public float getBalance(){
        return balance;
    }

    public abstract void deposit(float amount);//Checking, Saving and Credit will inherit this appropiately
    public abstract void withdraw(float amount);
    
}
