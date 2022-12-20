package com.example;

public class Messaggio 
{
    private String mittente;
    private String destinatario;
    private String testo_mess;
    
    public Messaggio() {
    }

    public String getMittente() {
        return mittente;
    }

    public void setMittente(String mittente) {
        this.mittente = mittente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }


    public String getTesto_mess() {
        return testo_mess;
    }

    public void setTesto_mess(String testo_mess) {
        this.testo_mess = testo_mess;
    }

    
}
