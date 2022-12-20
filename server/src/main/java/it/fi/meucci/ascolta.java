package it.fi.meucci;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ascolta extends Thread
{
    Socket s;
    crittografia c;

    public ascolta(Socket s)
    {
        this.s = s;
        c=login.chiavi.get(login.connessioni.indexOf(s));
    }

    public void run()
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            invia i = new invia();
            for(;;)
            {
                Messaggio m = deserializza(c.decrypt(in.readLine()));
                for (int j = 0; j < login.utenti.size(); j++) 
                {
                    if(m.getDestinatario().equals(login.utenti.get(j)))
                    {
                        i.inoltra(login.connessioni.get(j), m, login.chiavi.get(j));
                        break;
                    }
                    else if(m.getDestinatario().equals("all"))
                    {
                        i.broad(s, m);
                        break;
                    }
                    else if(m.getDestinatario().equals("lista") || m.getDestinatario().equals("LISTA"))
                    {
                        i.list(s,c);
                        break;
                    }
                    else if(m.getDestinatario().equals("fine") || m.getDestinatario().equals("FINE"))
                    {
                        login.logout(m.getMittente());
                    }
                }   
            }

        }
        catch(Exception e)
        {
            System.out.print("ascolta: ");
            System.out.print(e.getMessage() + "\n");
            login.logout(s);
        }
    }

    public Messaggio deserializza(String mess) throws Exception
    {
        XmlMapper xmlmapper2 = new XmlMapper();
        Messaggio m2 = xmlmapper2.readValue(mess, Messaggio.class);
        return m2;
    }
}
