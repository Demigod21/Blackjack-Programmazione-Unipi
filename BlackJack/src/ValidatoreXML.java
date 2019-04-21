

/**
 *
 * @author Davide
 */


import javax.xml.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;
import org.xml.sax.*;
import javax.xml.validation.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

public class ValidatoreXML {                                    
                                                               
  public void valida(String nomeFile) {
    try {  
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Document d = db.parse(new File(nomeFile + ".xml"));
        Schema s = sf.newSchema(new StreamSource(new File(nomeFile + ".xsd")));
        s.newValidator().validate(new DOMSource(d));
    } catch (Exception e) {
    if (e instanceof SAXException) 
        System.out.println("Errore di validazione: " + e.getMessage());
    else
        System.out.println(e.getMessage());    
    }
  }
}