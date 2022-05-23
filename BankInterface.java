public interface BankInterface {

    /**
     * Returns BankHolder if matches
     * @param cardNumber
     * @param pin
     * @return Bank Holder, null if not present
     */
    public BankHolderInterface getHolder(long cardNumber, int pin);


    /**
     * Adds bank holder to register
     * Generates card number and pin
     * @param person
     * @return Bank Holder
     */
    public BankHolderInterface addBankHolder(Person person);


    /**
     * Removes matching card holder.
     * @param bankHolder
     */
    public void removeBankHolder(BankHolderInterface bankHolder);


    /**
     * Transfers funds from holder to account with account number
     * @param holder
     * @param accountNumber
     * @param amount
     * @return bool success
     */
    public boolean transfer(BankHolderInterface holder, int accountNumber, int amount);

    /**
     * Performs shutdown operations, cleanup routines
     */
    public void shutdown();


}
