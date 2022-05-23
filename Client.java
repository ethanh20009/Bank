import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private BufferedReader systemIn;
    public boolean running;
    
    public Client(int port)
    {
        this.running = true;
        try{
            Socket socket = new Socket("localhost", port);
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.systemIn = new BufferedReader(new InputStreamReader(System.in));

        }catch(IOException e)
        {
            e.printStackTrace();
            this.running = false;
        }
    }

    public void shutdown()
    {
        try{
            this.running = false;
            this.in.close();
            this.out.close();
            this.systemIn.close();
            this.socket.close();
        }
        catch(IOException e)
        {
            System.out.println("From here");
            e.printStackTrace();
        }
        

    }

    public void sendMessage(String message)
    {
        try{
            out.write(message);
            out.newLine();
            out.flush();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        Client client = new Client(1234);
        
        //Create thread to read socket stream
        Thread thread = new Thread(){
            @Override
            public void run()
            {
                try{
                    if (client.socket == null) {return;}
                    String inputString;
                    while (true)
                    {
                        inputString = client.in.readLine();
                        if (inputString.equals("/shutdown"))
                        {
                            System.out.println("Disconnecting from server");
                            client.shutdown();
                            return;
                        }
                        else{
                            System.out.println(inputString);
                        }
                    }
                }catch(IOException e)
                {
                    //Ignore as reached when shutdown
                }
            }
        };

        thread.start();

        String userInput;
        try
        {
            while (client.running)
            {
                userInput = client.systemIn.readLine();
                if (!client.socket.isClosed())
                {
                    client.sendMessage(userInput);
                }
            }
        }catch(IOException e)
        {
            //Ignore
        }
        System.out.println("Client disconnected");
    }
    

    
    
}
