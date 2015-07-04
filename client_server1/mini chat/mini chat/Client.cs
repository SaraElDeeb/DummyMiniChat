using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Sockets;

namespace mini_chat
{
    class Client
    {
        string ip;
        int port;
        TcpClient client;

        public Client()
        {
            client = new TcpClient();
        }

        public void connect(string ip, int port)
        {
            this.ip = ip;
            this.port = port;

            try
            {

                client.Connect(this.ip, this.port);
                Console.WriteLine("connected");

                NetworkStream stream = client.GetStream();
                byte[] readMessege = new byte[client.ReceiveBufferSize];
                int messegeByte = stream.Read(readMessege, 0, client.ReceiveBufferSize);
                Console.WriteLine("Received messege: " + Encoding.ASCII.GetString(readMessege, 0, messegeByte));
                client.Close();

            }

            catch (Exception ex)
            {
                Console.WriteLine("error.." + ex.StackTrace);
                
            }
            
            


        }
    }
}
