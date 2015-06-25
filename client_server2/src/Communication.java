import java.io.*;
import java.net.Socket;

class Communication extends Thread
{
    boolean is_socket_closed = false;
//    boolean is_closeAfterSocketClosed;

    Communication( /*boolean is_closeAfterSocketClosed*/ )
    {
//        this.is_closeAfterSocketClosed = is_closeAfterSocketClosed;
    }

    private void start_communication()
    {
        // TODO: Server write once Read from multiple source
    }

    private Runnable construct_writer(final PrintWriter writer, final Socket socket)
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                String read_input;

                try {
                    while ((read_input = input.readLine()) != null && !is_socket_closed )
                    {
                        String msg;
                        msg = "<" + socket.getLocalSocketAddress().toString().substring(1) + "> " + read_input;
                        writer.println(msg);
                    }
                } catch (IOException e) {
                    e.getMessage();
                }finally {
                    writer.close();
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    PrintWriter writer(final Socket socket) throws IOException
    {
        final PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

        new Thread(construct_writer(writer, socket)).start();
        return writer;
    }

    private Runnable construct_reader(final BufferedReader reader, final String address,
                                      final boolean is_client) throws IOException
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                String msg;

                try
                {
                    while ((msg = reader.readLine()) != null && !msg.equals("EOL"))
                    {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if( is_client )
                        System.out.println("The Server has been closed");
                    else
                        System.out.println("Client " + address + " has been closed");

                    is_socket_closed = true;
                }
            }
        };
    }

    BufferedReader reader(Socket socket, boolean is_client) throws IOException
    {
        InputStreamReader stream = new InputStreamReader(socket.getInputStream());
        final BufferedReader reader = new BufferedReader(stream);

        String address = socket.getRemoteSocketAddress().toString().substring(1);

        new Thread(construct_reader(reader, address, is_client)).start();
        return reader;
    }
}