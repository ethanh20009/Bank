import java.io.Serializable;

public class BankHolder implements BankHolderInterface, Serializable {

    private int balance;
    private Person person;
    private int pin;
    private long cardNumber;
    private int accountNumber;

    public BankHolder(Person person, int pin, long cardNumber, int accountNumber)
    {
        this.person = person;
        this.pin = pin;
        this.cardNumber = cardNumber;
        this.accountNumber = accountNumber;
    }

    @Override
    public int getBalance() {
        return this.balance;
    }

    @Override
    public int withdraw(int amount) {
        if (this.balance >= amount)
        {
            this.balance-=amount;
            return amount;
        }
        return -1;

    }

    
    @Override
    public int deposit(int amount) {
        if (amount > 0)
        {
            this.balance += amount;
            return this.balance;
        }
        return this.balance;

    }

    
    @Override
    public boolean changePin(int newPin) {
        if (newPin >= 1000 && newPin <= 9999)
        {
            this.pin = newPin;
            return true;
        }
        return false;
    }

    @Override
    public boolean matchPin(int pinInput) {
        if (this.pin == pinInput)
        {
            return true;
        }

        else{
            return false;
        }
    }

    @Override
    public long getNumber()
    {
        return this.cardNumber;
    }

    @Override
    public boolean matchNumberAndPin(long number, int pinInput) {
        if (number == this.cardNumber && matchPin(pinInput))
        {
            return true;
        }
        return false;
    }

    @Override
    public Person getPerson()
    {
        return this.person;
    }

    @Override
    public int getPin()
    {
        return this.pin;
    }

    @Override
    public boolean matchAccountNumber(int accountNumber) {
        return this.accountNumber == accountNumber;
    }

    @Override
    public int getAccountNumber() {
        return this.accountNumber;
    }

    
    
}
