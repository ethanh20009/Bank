import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerRunner {
    public static void main(String[] args)
    {
        BankServer server = new BankServer(1234);
        Thread serverThread = new Thread(server);
        serverThread.start();

        try(BufferedReader in = new BufferedReader(new InputStreamReader(System.in)))
        {
            String inputMessage;
            while (!((inputMessage = in.readLine()).equals("shutdown")))
            {
                server.handleCommand(inputMessage);
            }

        }catch(IOException e)
        {
            //Ignore
        }
        finally
        {
            server.shutdown();
        }
    }
}
