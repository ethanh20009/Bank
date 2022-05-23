public interface BankHolderInterface {

    /**
     * Returns bank holder balance
     * @return balance
     */
    public int getBalance();

    /**
     * Withdraws and returns money taken out
     * @param amount money to take out
     * @return Amount taken out, if unable to: returns -1
     */
    public int withdraw(int amount);

    /**
     * Returns new balance after method finished
     */
    public int deposit(int amount);

    /**
     * Returns success as bool
     */
    public boolean changePin(int newPin);

    /**
     * Returns success as bool
     */
    public boolean matchPin(int pinInput);

    /**
     * Returns holder's card number
     * @return card number
     */
    public long getNumber();

    /**
     * Returns true if card number and pin match
     * @return boolean success
     */
    public boolean matchNumberAndPin(long number, int pinInput);

    /**
     * Returns bank holder's personal information class
     * @return Person class object
     */
    public Person getPerson();
    
    /**
     * Returns bank holder's pin
     * @return pin
     */
    public int getPin();

    /**
     * Returns true if account number matches
     * @param accountNumber
     * @return boolean match
     */
    public boolean matchAccountNumber(int accountNumber);

    public int getAccountNumber();
    
}
