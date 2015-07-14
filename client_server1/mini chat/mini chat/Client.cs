using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net.Sockets;
using System.IO;


namespace mini_chat
{
    class Client
    {
        string ip;
        int port;
        TcpClient client;
        private NetworkStream stream;
        
        
        
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

                stream = client.GetStream();
                


            }

            catch (Exception ex)
            {
                Console.WriteLine("error at connecting with the server: " + ex.Message);
                
            }
            
        }

        public void read()
        {
            try
            {
                byte[] readMessege = new byte[client.ReceiveBufferSize];
                int messegeByte;
                //= stream.Read(readMessege, 0, client.ReceiveBufferSize);


                while ((messegeByte = stream.Read(readMessege, 0, client.ReceiveBufferSize)) != 0)
                {
                    // TODO: cerror

                    Console.Write(this.ip +" " + this.port + ": " + Encoding.ASCII.GetString(readMessege, 0, messegeByte));
                }
            }

            catch (Exception ex)
            {
                Console.WriteLine("error at receiving message: " + ex.Message);
            }

        }

        public void send()
        {

            try
            {
                StreamWriter writer = new StreamWriter(stream);
                string sendText;

                while (true)
                {
                    
                    sendText = Console.ReadLine();
                    writer.WriteLine(sendText);
                    writer.Flush();
                    



                    //ASCIIEncoding asen = new ASCIIEncoding();
                    //byte[] message = asen.GetBytes(sendText);
                    //if (message.Length != 0)
                    //{
                    //    //byte[] message = ASCIIEncoding.ASCII.GetBytes(sendText);
                    //    Console.WriteLine("sending: " + sendText);
                    //    stream.Write(message, 0, message.Length);
                    //   // client.Client.Shutdown(SocketShutdown.Send);
                    //}




                }

            }

            catch (Exception ex)
            {
                Console.WriteLine("error at sending message: " + ex.Message);
            }
        }
    }
}
