import java.io.IOException;

class Main
{
    public static void main(String[] args) throws IOException
    {
        byte[] IP = {127,0,0,1};
        int port = 12345;
        int max_num_connections = 1;

        Server server = new Server(IP, port, max_num_connections);
        server.initiate();
    }
}