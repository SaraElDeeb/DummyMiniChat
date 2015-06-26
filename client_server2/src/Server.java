import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class Server implements Observer
{
    private byte[] IP;
    private int port = 0;
    private int max_num_connections = 1;
    private static ArrayList<Socket> list_of_sockets = null;
    private ServerCommunication communication;
    private String message_on_new_connection = null;
    private String welcome_message = null;
    private boolean use_default_message = false;

    Server(byte[] IP, int port, int max_num_connections)
    {
        set_IP(IP);
        set_port(port);
        set_max_number_of_connection(max_num_connections);

        list_of_sockets = new ArrayList<>();
        this.communication = new ServerCommunication(list_of_sockets);
    }

    void set_port(int port)
    {
        this.port = port;
    }

    void set_IP(byte[] IP)
    {
        this.IP = IP;
    }

    void set_max_number_of_connection(int max_num_connections)
    {
        this.max_num_connections = max_num_connections;
    }

    void run_server() throws IOException
    {
        InetAddress address = InetAddress.getByAddress(this.IP);
        communication.construct_writer();

        try (ServerSocket server = new ServerSocket(this.port, this.max_num_connections, address))
        {
            while (true)
            {
                // Listen
                Socket clientSocket = server.accept();
                // Add the new socket to the list of opened sockets
                list_of_sockets.add(clientSocket);
                // Add this class as an observer on ServerCommunication
                communication.addObserver(this);
                // Construct the reader
                communication.reader(clientSocket, false);

                if( this.use_default_message )
                {
                    System.out.println("There is a connection from " +
                            clientSocket.getRemoteSocketAddress().toString().substring(1));
                }

                PrintWriter writer = communication.writer(clientSocket);

                if( !this.welcome_message.equals("") && this.welcome_message != null )
                    writer.println(this.welcome_message);

                if(!this.message_on_new_connection.equals("") && this.message_on_new_connection != null)
                    writer.println(this.message_on_new_connection);

            }
        }catch (IOException e)
        {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    void set_message_on_new_connection(String message, boolean use_default_message)
    {
        this.message_on_new_connection = message;
        this.use_default_message = use_default_message;
    }

    void set_welcome_message(String message)
    {
        this.welcome_message = message;
    }

    @Override
    public void update(Observable observable, Object o)
    {
        Socket socket = (Socket)o;
        list_of_sockets.remove(socket);
    }
}