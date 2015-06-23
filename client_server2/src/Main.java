import java.io.IOException;

class Main
{
    public static void main(String[] args) throws IOException
    {
        byte[] IP = {127,0,0,1};
        int port = 12345;

        Server server = new Server(IP, port);
        server.initiate();
    }
}