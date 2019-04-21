
/**
 *
 * @author Davide
 */
import com.thoughtworks.xstream.*;
import java.io.*;
import static java.lang.System.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.xml.*;
import javax.xml.parsers.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.*;
import org.w3c.dom.*;
import org.xml.sax.*;

class ParametriConfigurazione{
     String nomeApp;
     String ipClient;
     String ipServer;
     int portaServer;
     int xUtenti;
     int gGiorni;
     int maxPuntata;
    
    ParametriConfigurazione(String na,String ipc, String ips, int ps, int x, int g, int mp){
        this.nomeApp=na;
        this.ipClient = ipc;
        this.ipServer = ips;
        this.portaServer = ps;
        this.xUtenti = x;
        this.gGiorni = g;
        this.maxPuntata = mp; 
    }
}

public class ConfigXML {
    ParametriConfigurazione pcc;
    ConfigXML() {                                            
         XStream xs = new XStream();
        try {
            ValidatoreXML v = new ValidatoreXML();
            v.valida("paramConfig");
            FileReader fileReader = new FileReader("paramConfig.xml");
            xs.alias("ParametriConfigurazione", ParametriConfigurazione.class);
            pcc = (ParametriConfigurazione) xs.fromXML(fileReader);
            
            
        } catch (Exception e) {System.out.println("Errore: impossibile caricare la configurazione dal file XML");}
    }
    

}
