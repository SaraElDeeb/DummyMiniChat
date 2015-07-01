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

        public bool connect(string ip, int port)
        {
            this.ip = ip;
            this.port = port;

            try
            {
                client.Connect(this.ip, this.port);
                return true;
            }

            catch (Exception ex)
            {
                Console.WriteLine("error.." + ex.StackTrace);
                return false;
                
            }
            
            


        }
    }
}
