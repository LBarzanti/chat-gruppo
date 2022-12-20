package com.example;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class scrivi extends Thread
{
    Socket s;

    public scrivi(Socket s)
    {
        this.s = s;
    }

    public void run()
    {
        try
        {
            BufferedReader tastiera = new BufferedReader(new InputStreamReader(System.in));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            for(;;)
            {
                Messaggio m = new Messaggio();
                m.setMittente(login.utente);
                System.out.println("inserire destinatario");
                m.setDestinatario(tastiera.readLine());
                System.out.println("inserire il testo del messaggio");
                m.setTesto_mess(tastiera.readLine());
                out.writeBytes(login.c.encrypt(serializza(m)) + "\n");
            }
        }
        catch (Exception e)
        {
            System.out.println("scrivi: " + e.getMessage());
        }
    }

    public String serializza(Messaggio m)
    {
        try
        {
            XmlMapper map = new XmlMapper();
            String msg = map.writeValueAsString(m);
            return msg;
        }
        catch(Exception e)
        {
            System.out.println("serializza: " + e.getMessage());
            return null;
        }
        
    }
}
