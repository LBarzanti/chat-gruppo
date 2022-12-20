package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class login extends Thread
{
    Socket s;
    public static String utente;
    public static crittografia c;

    public login(Socket s)
    {
        this.s = s;
    }

    public void run()
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
            c = new crittografia(s);
            c.init();
            boolean x=false;
            while (!x) 
            {
                String a = c.decrypt(in.readLine());
                if (a.equals("logged")) 
                {
                    x=true;
                }
                else
                {
                    System.out.println(a);
                    String b = tastiera.readLine();
                    out.writeBytes(c.encrypt(b) + "\n");
                    utente = b;
                }
            }
            x=false;
            System.out.println("utenti connessi: ");
            while (!x) 
            {
                String a = c.decrypt(in.readLine());
                if (a.equals("fine")) 
                {
                    x=true;
                }
                else
                {
                    System.out.println(a);
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
