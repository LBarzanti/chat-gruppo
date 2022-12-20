package com.example;

import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        try
        {
            Socket s = new Socket("localhost", 25565);
            login l = new login(s);
            l.start();
            l.join();
            System.out.println("connessione effettuata");
            ascolta a = new ascolta(s);
            a.start();
            scrivi scr = new scrivi(s);
            scr.start();
            a.join();
        }
        catch(Exception e)
        {
            System.out.println(e.getLocalizedMessage());
        }
        
    }
}
