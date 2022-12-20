package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ascolta extends Thread
{
    Socket s;

    public ascolta(Socket s)
    {
        this.s = s;
    }

    public void run()
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            for (;;) 
            {
                String msg = login.c.decrypt(in.readLine());
                Messaggio m = deserializza(msg);
                if (m.getDestinatario().equals("lista")) 
                {
                    if (m.getMittente().equals("0")) 
                    {
                        System.out.println("utenti connessi: \n" + m.getTesto_mess());
                    }
                    else
                    {
                        System.out.println(m.getTesto_mess());
                    }
                }
                else if(m.getDestinatario().equals("fine"))
                {
                    System.out.println("disconnessione effettuata");
                    break;
                }
                else
                {
                    System.out.println(m.getMittente() + ": " + m.getTesto_mess());
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public Messaggio deserializza(String mess)
    {
        try
        {
            XmlMapper xmlmapper2 = new XmlMapper();
            Messaggio m2 = xmlmapper2.readValue(mess, Messaggio.class);
            return m2;
        }
        catch(Exception e)
        {
            System.out.println("deserializza: " + e.getMessage());
            return null;
        }
    }
}
