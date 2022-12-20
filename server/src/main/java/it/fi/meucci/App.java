package it.fi.meucci;

import java.net.ServerSocket;
import java.net.Socket;

public class App
{
    public static void main( String[] args )
    {
        try
        {
            ServerSocket ss = new ServerSocket(25565);
            for (int i = 1; i > 0; i++) 
            {
                Socket s = ss.accept();
                login l = new login(s);
                l.start();
                l.join();
                System.out.println("connessione effettuata");
                ascolta a = new ascolta(s);
                a.start();
            }
            ss.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
