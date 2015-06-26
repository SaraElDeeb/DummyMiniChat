import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

public class Communication extends Observable implements Runnable
{
    boolean is_socket_closed = false;

    Communication() {}

    protected Runnable construct_writer(final PrintWriter writer)
    {
        final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        return new Runnable()
        {
            @Override
            public void run()
            {
                String read_input;

                try {
                    String msg;

                    while ( !writer.checkError() && (read_input = input.readLine()) != null /*&& !is_socket_closed*/ )
                    {
                        msg = read_input;
                        writer.println(msg);
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }finally {
                    writer.close();
                    try {
                        input.close();
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        };
    }

    PrintWriter writer(final Socket socket) throws IOException
    {
        final PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        new Thread(construct_writer(writer)).start();
        return writer;
    }

    protected Runnable construct_reader(final BufferedReader reader, final String address,
                                      final boolean is_client, final Socket socket) throws IOException
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                String msg;

                try
                {
                    while (socket.isConnected() && (msg = reader.readLine()) != null && !msg.equals("EOL"))
                    {
                        System.out.println("<" + socket.getRemoteSocketAddress().toString().substring(1) + "> " + msg);
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                } finally {
                    if( is_client )
                        System.out.println("Disconnected");
                    else
                        System.out.println("Client " + address + " has been closed");
                    try {
                        reader.close();
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                    is_socket_closed = true;
                    setChanged();
                    notifyObservers(socket);
                }
            }
        };
    }

    BufferedReader reader(Socket socket, boolean is_client) throws IOException
    {
        InputStreamReader stream = new InputStreamReader(socket.getInputStream());
        final BufferedReader reader = new BufferedReader(stream);

        String address = socket.getRemoteSocketAddress().toString().substring(1);

        new Thread(construct_reader(reader, address, is_client, socket)).start();
        return reader;
    }

    @Override
    public void run() {}
}


class ServerCommunication extends Communication
{
    ArrayList<Socket> list_of_sockets;

    ServerCommunication(ArrayList<Socket> list_of_sockets)
    {
        this.list_of_sockets = list_of_sockets;
    }

    public void construct_writer() throws IOException
    {
        final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        Runnable writer =  new Runnable()
        {
            @Override
            public void run()
            {
                String msg;
                try {
                    while ( (msg = input.readLine()) != null )
                    {
                        send_message(msg);
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }finally {
                    try {
                        input.close();
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                }
            }
        };

        new Thread(writer).start();
    }

    private void send_message(String msg) throws IOException
    {
        for (Socket socket : list_of_sockets)
        {
            OutputStream stream = socket.getOutputStream();
            stream.write( (msg+"\n").getBytes() );
            stream.flush();
        }
    }

    PrintWriter writer(Socket socket) throws IOException
    {
        return new PrintWriter(socket.getOutputStream(), true);
    }
}