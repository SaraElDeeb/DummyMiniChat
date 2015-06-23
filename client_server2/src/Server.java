import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private byte[] IP;
    private int port = 0;
    Server(byte[] IP, int port )
    {
        set_IP(IP);
        set_port(port);
    }

    void set_port(int port)
    {
        this.port = port;
    }

    void set_IP(byte[] IP)
    {
        this.IP = IP;
    }

    boolean initiate() throws IOException
    {
        ServerSocket server = new ServerSocket(this.port);

        try
        {
            while (true)
            {
                // Listen
                Socket clientSocket = server.accept();
                // Read Stream
                BufferedReader in = null;
                // Write Stream
                PrintWriter out = null;

                if(server.isBound())
                {
                    try
                    {
                        System.out.println("IP address: " + server.getLocalSocketAddress());
                        out = new PrintWriter(clientSocket.getOutputStream(), true);
                        out.println("This a welcome message.");

                        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    } finally {
                        clientSocket.close();
                        in.close();
                        out.close();
                    }
                }
            }
        } finally {
            server.close();
        }
    }
}