using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Threading;


namespace mini_chat
{
    class Program
    {
        static void Main(string[] args)
        {
            
            string ip = "127.0.0.1";
            int port = 12345;
            Client clnt = new Client();

            Thread thread1 = new Thread(clnt.send);
            Thread thread2 = new Thread(clnt.read);


            clnt.connect(ip, port);
            thread1.Start();
            thread2.Start();
            
            

        }
    }
}
