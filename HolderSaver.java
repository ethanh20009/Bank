import java.util.*;
import java.io.*;

public class HolderSaver {
    public HolderSaver(){}

    public static ArrayList<BankHolderInterface> loadBankHolders(String filePath)
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

    public static void saveBankHolders(String filePath, ArrayList<BankHolderInterface> holders)
    {
        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath)))
        {
            for (BankHolderInterface bankHolder : holders)
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
