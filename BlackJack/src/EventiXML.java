/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.thoughtworks.xstream.XStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;

class Evento {

    String nomeGioco;
    String IPClient;
    String timestamp;
    String nmEvento;

    Evento(String ng, String ipc, String ne, String ts) {
        this.nomeGioco = ng;
        this.IPClient = ipc;
        this.nmEvento = ne;
        this.timestamp = ts;
    }

}

/**
 *
 * @author Davide
 */

public class EventiXML {

    public static void send(String nomeEvento) {

        XStream xs = new XStream();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String eventoXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<!-- evento.xml -->\n";
        try {
            Evento evento = new Evento("Blackjack", InetAddress.getLocalHost().getHostAddress(), nomeEvento, timestamp.toString());
            eventoXML += xs.toXML(evento);
            Files.write(Paths.get("evento.xml"), eventoXML.getBytes());
        } catch (Exception e) {
            System.out.println("Errore: impossibile creare il file evento.xml");
        }
        //Invio//
        ConfigXML conf = new ConfigXML();
        try (DataOutputStream dout = new DataOutputStream((new Socket(conf.pcc.ipServer, conf.pcc.portaServer)).getOutputStream());) {
            dout.writeUTF(eventoXML);
            System.out.println("inviato: \"" + nomeEvento + "\"");
        } catch (Exception e) {
            System.err.println(e.getMessage() + " (server non attivo)");
        }
    }

}
