package it.fi.meucci;

import java.io.DataOutputStream;
import java.net.Socket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class invia
{
    public invia() 
    {
        
    }

    public void inoltra(Socket s, Messaggio m, crittografia c)
    {
        try
        {
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            out.writeBytes(c.encrypt(serializza(m)) + "\n");
        }
        catch(Exception e)
        {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void broad(Socket s, Messaggio m)
    {
        try
        {
            for (int i = 0; i < login.utenti.size(); i++) 
            {
                if (login.connessioni.get(i) != s) 
                {
                    DataOutputStream out = new DataOutputStream(login.connessioni.get(i).getOutputStream());
                    crittografia c = login.chiavi.get(i);
                    out.writeBytes(c.encrypt(serializza(m)) + "\n");
                }
            }
        }
        catch ( Exception e )
        {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void list(Socket s, crittografia c)
    {
        try
        {
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            
            for (int i = 0; i < login.utenti.size(); i++) 
            {
                Messaggio msg = new Messaggio();
                msg.setDestinatario("lista");
                msg.setMittente(new String(i + ""));
                msg.setTesto_mess(login.utenti.get(i));
                out.writeBytes(c.encrypt(serializza(msg)) + "\n");
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getLocalizedMessage());
        }
        
    }

    public String serializza(Messaggio m) throws JsonProcessingException
    {
        XmlMapper map = new XmlMapper();
        String msg = map.writeValueAsString(m);
        return msg;
    }
}
