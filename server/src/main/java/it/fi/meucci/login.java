package it.fi.meucci;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class login extends Thread
{
    public static ArrayList<String> utenti = new ArrayList<>();
    public static ArrayList<Socket> connessioni = new ArrayList<>();
    public static ArrayList<crittografia> chiavi = new ArrayList<>();
    BufferedReader in;
    DataOutputStream out;
    Socket s;

    public login(Socket s)
    {
        this.s = s;
    }

    public void run()
    {
        try
        {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new DataOutputStream(s.getOutputStream());
            crittografia c1 = new crittografia(s);
            c1.init();
            chiavi.add(c1);
            boolean trovato= true;
            String nome=null;
            while (trovato) 
            {
                trovato = false;
                out.writeBytes(c1.encrypt("inserire il nome utente") + "\n");
                nome = c1.decrypt(in.readLine());
                for (int i = 0; i < utenti.size(); i++) 
                {
                    if (nome.equals(utenti.get(i))) 
                    {
                        out.writeBytes(c1.encrypt("il nome scelto è già in uso") + "\n");
                        trovato=true;
                    }
                }
            }
            utenti.add(nome);
            connessioni.add(s);
            out.writeBytes(c1.encrypt("logged") + "\n");
            for (int i = 0; i < utenti.size(); i++) 
            {
                out.writeBytes(c1.encrypt(utenti.get(i)) + "\n");
            }
            out.writeBytes(c1.encrypt("fine") + "\n");
        }
        catch(Exception e)
        {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static void logout(Socket s)
    {
        try
        {
            DataOutputStream out1 = new DataOutputStream(s.getOutputStream());
            for (int i = 0; i < connessioni.size(); i++) 
            {
                if (connessioni.get(i) == s) 
                {
                    out1.writeBytes(chiavi.get(i).encrypt("fine") + "\n");
                    chiavi.remove(i);
                    connessioni.remove(i);
                    utenti.remove(i);
                    s.close();
                }
            }
        }
        catch (Exception e) 
        {
            System.out.println("logout(s): " + e.getMessage());
        }
    }

    public static void logout(String utente)
    {
        try
        {
            for (int i = 0; i < utenti.size(); i++) 
            {
                if (utenti.get(i).equals(utente)) 
                {
                    DataOutputStream out1 = new DataOutputStream(connessioni.get(i).getOutputStream());
                    utenti.remove(i);
                    out1.writeBytes(chiavi.get(i).encrypt("fine") + "\n");
                    connessioni.get(i).close();
                    connessioni.remove(i);
                    chiavi.remove(i);
                }
            }
        }
        catch (Exception e) 
        {
            System.out.println("logout(u): " + e.getMessage());
        }
    }
}
