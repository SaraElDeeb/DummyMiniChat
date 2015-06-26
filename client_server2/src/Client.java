import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

public class Client
{

    private byte[] IP;
    private int port = 0;

    Client( byte[] IP, int port)
    {
        set_IP(IP);
        set_port(port);
    }

    void initiate()  throws IOException
    {
        InetAddress address = InetAddress.getByAddress(IP);

        try
        {
            // connect to the server
            Socket client = new Socket(address, port);
            System.out.println("connected");

            // Construct the communicator which responsible for reading and writing from specific socket
            Communication communication = new Communication();
            communication.reader(client, true);
            communication.writer(client);

        } catch (ConnectException e) {
            System.err.println( e.getMessage() + "\nThis Address/port is not available");
        }
    }

    void set_port(int port) {
        this.port = port;
    }

    void set_IP(byte[] IP) {
        this.IP = IP;
    }

    public static void main(String[] args) throws IOException
    {
        byte[] IP = {127,0,0,1};
        int port = 12345;

        Client client = new Client(IP, port);
        client.initiate();
    }
}
