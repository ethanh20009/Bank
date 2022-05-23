import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Bank implements BankInterface{

    private ArrayList<BankHolderInterface> holders;

    public Bank(){holders = new ArrayList<BankHolderInterface>();}

    /**
     * Creates bank with previous holders by passing in file path of objects
     * @param filePath
     */
    public Bank(String filePath){
        this.holders = loadBankHolders(filePath);
    }

    @Override
    public BankHolderInterface getHolder(long cardNumber, int pin) {

        for (BankHolderInterface bankHolder : holders)
        {
            if (bankHolder.matchNumberAndPin(cardNumber, pin))
            {
                return bankHolder;
            }
        }
        return null;
    }

    @Override
    public BankHolderInterface addBankHolder(Person person) {
        int pin = generatePin();
        long cardNumber = generateCardNumber();
        int accNum = generateAccountNumber();
        BankHolderInterface bankHolder = new BankHolder(person, pin, cardNumber, accNum);
        this.holders.add(bankHolder);
        return bankHolder;
    }

    @Override
    public void removeBankHolder(BankHolderInterface bankHolder) {
        this.holders.remove(bankHolder);
    }

    private int generatePin()
    {
        return 1000 + (int)(Math.random() * 9000);
    }

    private long generateCardNumber()
    {
        return 100000000000L + (long)(Math.random()*900000000000L);
    }

    private int generateAccountNumber()
    {
        return 1000000 + (int)(Math.random() * 9000000);
    }

    @Override
    public boolean transfer(BankHolderInterface holder, int accountNumber, int amount) {
        for (BankHolderInterface bankHolder : holders)
        {
            if (bankHolder.matchAccountNumber(accountNumber))
            {
                int withdrawn = -1;
                synchronized(holder){
                    withdrawn = holder.withdraw(amount);
                }
                if (withdrawn < 0) {return false;}
                synchronized(bankHolder)
                {
                    bankHolder.deposit(withdrawn);
                }
                return true;
            }
        }
        return false;
        
    }

    private ArrayList<BankHolderInterface> loadBankHolders(String filePath)
    {
        try(ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(filePath)))
        {
            ArrayList<BankHolderInterface> bankHolders = new ArrayList<BankHolderInterface>();
            BankHolder holder;
            while ((holder = (BankHolder)objectInputStream.readObject()) != null)
            {
                bankHolders.add(holder);
            }
            return bankHolders;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return new ArrayList<BankHolderInterface>();
    }

    public void saveBankHolders(String filePath)
    {
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath)))
        {
            for (BankHolderInterface bankHolder : this.holders)
            {
                objectOutputStream.writeObject(bankHolder);
            }
            objectOutputStream.writeObject(null);
            objectOutputStream.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }
    
}
