import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private byte[] IP;
    private int port = 0;
    int max_num_connections = 1;

    Server(byte[] IP, int port, int max_num_connections)
    {
        set_IP(IP);
        set_port(port);
        set_max_number_of_connection(max_num_connections);
    }

    void set_port(int port) {
        this.port = port;
    }

    void set_IP(byte[] IP) {
        this.IP = IP;
    }

    void set_max_number_of_connection(int max_num_connections) {
        this.max_num_connections = max_num_connections;
    }

    void initiate() throws IOException {
        InetAddress address = InetAddress.getByAddress(this.IP);

        try (ServerSocket server = new ServerSocket(this.port, this.max_num_connections, address)) {
            while (true)
            {
                // Listen
                Socket clientSocket = server.accept();

                if (server.isBound())
                {
                    Communication communication = new Communication(clientSocket);
                    communication.start_communication();
                }
            }
        }
    }
}


class Communication implements Runnable
{
    Socket client_socket = null;
    Thread thread = null;

    Communication(Socket client_socket)
    {
        this.client_socket = client_socket;
        this.thread = new Thread(this);
    }

    void start_communication()
    {
        thread.start();
    }

    @Override
    public void run()
    {
        PrintWriter out = null;
        try {
            out = new PrintWriter(this.client_socket.getOutputStream(), true);
            out.println("This a welcome message.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(this.client_socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String str = "";

        while (( !str.equals("EOL") || !this.client_socket.isClosed() ) && in != null)
        {
            try
            {
                str = in.readLine();
                System.out.println(str);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        try
        {
            if (in != null && out != null )
            {
                in.close();
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try
        {
            client_socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}