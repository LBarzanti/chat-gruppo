package it.fi.meucci;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class crittografia 
{
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private String extPublicString;
    private PublicKey extPublicKey;
    private Socket s;
    private DataOutputStream out;
    private BufferedReader in;
    
    public crittografia(Socket s)
    {
        this.s = s;
    }

    public void init()
    {
        try 
        {
            out = new DataOutputStream(s.getOutputStream());
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048);
            KeyPair pair = generator.generateKeyPair();
            privateKey = pair.getPrivate();
            publicKey = pair.getPublic();
            out.writeBytes(encode(publicKey.getEncoded()) + "\n");
            extPublicString = in.readLine();
            X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(decode(extPublicString));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            extPublicKey = keyFactory.generatePublic(keySpecPublic);
        } 
        catch (Exception e) 
        {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public String encrypt(String m)
    {
        try
        {
            byte[] mBytes = m.getBytes();
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, extPublicKey);
            byte[] encrypted = cipher.doFinal(mBytes);
            return encode(encrypted);
        }
        catch(Exception e)
        {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    private String encode(byte[] m)
    {
        return Base64.getEncoder().encodeToString(m); 
    }

    public String decrypt(String encrypted)
    {
        try
        {
            byte[] eBytes = decode(encrypted);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] mBytes = cipher.doFinal(eBytes);
            return new String(mBytes, "UTF8");
        }
        catch(Exception e)
        {
            System.out.println(e.getLocalizedMessage());
            return null;
        }
    }

    private byte[] decode(String s)
    {
        return  Base64.getDecoder().decode(s);
    }

    public void printKeys()
    {
        System.out.println("Public key \n" + encode(publicKey.getEncoded()));
        System.out.println("Private key \n" + encode(privateKey.getEncoded()));
    }
}
