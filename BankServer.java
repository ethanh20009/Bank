import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.ArrayList;

public class BankServer implements Runnable
{
    private ServerSocket serverSocket = null;
    protected BankInterface bank;
    private ArrayList<SocketHandler> handlers;
    public BankServer(int port)
    {
        bank = new Bank("holders.txt");
        this.handlers = new ArrayList<SocketHandler>();
        try {
            this.serverSocket = new ServerSocket(port);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override 
    public void run()
    {
        if (this.serverSocket == null) {return;}
        try{
            while (true)
            {
                Socket socket = this.serverSocket.accept();
                SocketHandler sh = new SocketHandler(socket);
                Thread newThread = new Thread(sh);
                handlers.add(sh);
                newThread.start();
            }
        }
        catch(IOException e)
        {
            System.out.println("Server Closing");
        }
    }

    public void shutdown()
    {
        for (SocketHandler handler : handlers)
        {
            handler.shutdown();
        }
        try{
            this.serverSocket.close();
        }
        catch(IOException e)
        {
            //Ignore
        }
    }

    public void removeFromList(SocketHandler handler)
    {
        this.handlers.remove(handler);
    }

    public void handleCommand(String command)
    {
        switch(command)
        {
            case ("clients"):
                System.out.println(handlers.size() + " client(s) connected");
                break;
            
            default:
                System.out.println("Unrecognised Command");
                break;
        }
    }

    public class SocketHandler implements Runnable{

        private Socket socket;
        private BufferedReader in;
        private BufferedWriter out;
        private boolean running;
        private BankHolderInterface bankHolder;

        public SocketHandler(Socket socket)
        {
            this.running = true;
            this.socket = socket;
            try {
                this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            }
            catch(IOException e)
            {
                e.printStackTrace();
                this.running = false;
            }
        }

        @Override
        public void run() {

            //Get bankHolder
            try{
                while (bankHolder == null) //Menu to get
                {
                    sendMessage("Register[1] Login[2]");
                    
                    String registerOrLogin = this.in.readLine();
                    if (registerOrLogin.equals("2")) //Login
                    {
                        long cardNumber = -1L;
                        while (cardNumber < 0)
                        {
                            sendMessage("Please enter your card number");
                            try{
                                String numberInput = in.readLine();
                                long tempCardNumber = Long.parseLong(numberInput);
                                if (tempCardNumber < 100000000000L || tempCardNumber > 999999999999L)
                                {
                                    throw new NumberFormatException();
                                }
                                cardNumber = tempCardNumber;
                            }catch(NumberFormatException e)
                            {
                                sendMessage("You must input a valid card number");
                            }
                        }

                        int pin = -1;
                        while (pin < 0)
                        {
                            sendMessage("Please enter your pin number [####]");
                            try{
                                String numberInput = in.readLine();
                                int tempPin = Integer.parseInt(numberInput);
                                if (tempPin < 1000 || tempPin > 9999)
                                {
                                    throw new NumberFormatException();
                                }
                                pin = tempPin;
                            }catch(NumberFormatException e)
                            {
                                sendMessage("You must input a valid pin number");
                            }
                        }

                        this.bankHolder = bank.getHolder(cardNumber, pin);
                        if (this.bankHolder == null)
                        {
                            sendMessage("Account details did not match");
                        }
                    }

                    else if (registerOrLogin.equals("1")) //Register
                    {
                        sendMessage("First name:");
                        String name = in.readLine();
                        sendMessage("Surname:");
                        String surname = in.readLine();
                        int age = -1;
                        while (age < 13)
                        {
                            sendMessage("Age (must be older than 13):");
                            String ageStr = in.readLine();
                            try{
                                age = Integer.parseInt(ageStr);
                            }
                            catch(NumberFormatException e)
                            {
                                sendMessage("Enter a valid age");
                            }
                        }

                        Person tempPerson = new Person(name, surname, age);
                        this.bankHolder = bank.addBankHolder(tempPerson);
                        sendMessage("Your new card number: " + this.bankHolder.getNumber());
                        sendMessage("Your new pin number: " + this.bankHolder.getPin());
                        sendMessage("Your new account number: " + this.bankHolder.getAccountNumber());
                    }
                }

                //Allow user commands

                sendMessage("Welcome " + this.bankHolder.getPerson().getFirstName());
                String command;
                while (true)
                {
                    sendCommands();
                    command = in.readLine();
                    if (!this.running) {return;}
                    
                    switch(command){
                        case ("1"): //Deposit
                            
                            int amount = -1;
                            while (amount < 0)
                            {
                                sendMessage("Amount: ");
                                try{
                                    String amountIn = in.readLine();
                                    amount = Integer.parseInt(amountIn);
                                }
                                catch(NumberFormatException e){
                                    sendMessage("Please enter a valid amount");
                                }
                            }
                            int newBalance;
                            synchronized(bankHolder)
                            {
                                newBalance = bankHolder.deposit(amount);
                            }

                            sendMessage("Your new bank balance is " + newBalance);
                            break;
                        case ("2"): //Withdraw
                            amount = -1;
                            while (amount < 0)
                            {
                                sendMessage("Amount: ");
                                try{
                                    String amountIn = in.readLine();
                                    amount = Integer.parseInt(amountIn);
                                }
                                catch(NumberFormatException e){
                                    sendMessage("Please enter a valid amount");
                                }
                            }
                            int withdrawnMoney;
                            synchronized(bankHolder)
                            {
                                withdrawnMoney = bankHolder.withdraw(amount);
                            }
                            sendMessage("Withdrawn: " + withdrawnMoney);
                            break;
                        case ("3"): //Get balance
                            int returnedBalance;
                            synchronized(bankHolder)
                            {
                                returnedBalance = bankHolder.getBalance();
                            }
                            sendMessage("Current balance: " + returnedBalance);
                            break;
                        case ("4"): //Change pin
                            int newPin = -1;
                            while (newPin < 1000 || newPin > 9999)
                            {
                                sendMessage("New Pin: ");
                                try{
                                    String amountIn = in.readLine();
                                    newPin = Integer.parseInt(amountIn);
                                }
                                catch(NumberFormatException e){
                                    sendMessage("Please enter a valid pin");
                                }
                            }
                            boolean changeSuccess = false;
                            synchronized(bankHolder)
                            {
                                changeSuccess = bankHolder.changePin(newPin);
                            }
                            sendMessage("Change " + (changeSuccess ? "Successful" : "Unsuccessful"));
                            break;
                        case("5"):
                            amount = -1;
                            while (amount < 0)
                            {
                                sendMessage("Amount: ");
                                try{
                                    String amountIn = in.readLine();
                                    amount = Integer.parseInt(amountIn);
                                }
                                catch(NumberFormatException e){
                                    sendMessage("Please enter a valid amount");
                                }
                            }
                            int accountNum = -1;
                            while (accountNum < 1000000 || accountNum > 9999999)
                            {
                                sendMessage("Account Number: ");
                                try{
                                    String amountIn = in.readLine();
                                    accountNum = Integer.parseInt(amountIn);
                                }
                                catch(NumberFormatException e){
                                    sendMessage("Please enter a valid amount");
                                }
                            }
                            boolean transferSuccess = bank.transfer(this.bankHolder, accountNum, amount);
                            sendMessage("Transfer " + (transferSuccess ? "Successful" : "Unsuccessful"));
                            break;
                        case("6"): //Logout
                            sendMessage("Logging out...");
                            removeFromList(this);
                            shutdown();
                            return;
                        default:
                            sendMessage("Invalid command");
                            break;
                    }
                }
            }catch (IOException e)
            {
                //Ignore as will reach when shutdown prematurely
            }
            catch(NullPointerException e)
            {
                //Ignore
            }
            
        }

        private void sendCommands()
        {
            sendMessage("Deposit [1]");
            sendMessage("Withdraw [2]");
            sendMessage("Get Balance [3]");
            sendMessage("Change Pin [4]");
            sendMessage("Transfer funds [5]");
            sendMessage("Log out [6]");
            sendMessage("");
        }

        public void sendMessage(String message)
        {
            try{
                this.out.write(message);
                this.out.newLine();
                this.out.flush();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        public void shutdown()
        {
            bank.shutdown();
            try{
                //send Client shutdown command
                this.running = false;
                sendMessage("/shutdown");

                in.close();
                out.close();

                

                socket.close();
            }
            catch(IOException e)
            {
                //Ignore
            }
        }
        
    }
}