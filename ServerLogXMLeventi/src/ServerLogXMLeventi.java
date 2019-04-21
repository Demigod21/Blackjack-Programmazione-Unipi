/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 *
 * @author Davide
 */

//@XStreamAlias("blackjack.Evento")
class Evento{
    String nomeGioco;
    String IPClient;
    String timestamp;
    String nmEvento;
    
    Evento(String ng, String ipc, String ne, String ts){
        this.nomeGioco = ng;
        this.IPClient = ipc;
        this.nmEvento = ne;
        this.timestamp = ts;
    }
        
    }
//@XStreamAlias("blackjack.Evento")
public class ServerLogXMLeventi {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try (ServerSocket servs = new ServerSocket(8080, 9)) {
            Files.write(Paths.get("logEventiSessione.txt"), "".getBytes());
            while(true) {
                Socket s = servs.accept();
                try (DataInputStream din = new DataInputStream(s.getInputStream());) {
                    XStream xs = new XStream();
                    String eventoXML = din.readUTF();
                    
                    Files.write(Paths.get("evento.xml"), eventoXML.getBytes());
                    Evento eventos = (Evento) xs.fromXML(eventoXML);
                    Files.write(Paths.get("logEventiSessione.txt"), (eventoXML + "\n\n").getBytes(), StandardOpenOption.APPEND);
                   
                    System.out.println("Ricevuto da " + eventos.IPClient + "\t" + eventos.nomeGioco + ": \"" + eventos.nmEvento +
                            "\" " + eventos.timestamp);
                } catch (Exception e) {e.printStackTrace();}
            }
        } catch (IOException e) {e.printStackTrace();}
        
    }
    
}
