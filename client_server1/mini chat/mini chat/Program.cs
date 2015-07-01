using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace mini_chat
{
    class Program
    {
        static void Main(string[] args)
        {
            string ip = "127.0.0.1";
            int port = 12345;
            Client clnt = new Client();

            bool result = clnt.connect(ip, port);

            if (result)
                Console.WriteLine("connected");
             
        }
    }
}
