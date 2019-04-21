/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Davide
 */
public class GestoreCache implements Serializable {
     boolean inter;
     int puntata;
     
    public void salvaPuntata(int p){
         try (FileOutputStream fout = new FileOutputStream("./myfiles/partitaInterrotta.bin");
            ObjectOutputStream oout = new ObjectOutputStream(fout);) {
            oout.writeObject(p);    
            System.out.println("Salvata partita interrotta");
            inter = true;
            } catch (IOException ex) {
                System.out.println("Errore: partita non salvata");
            }
     }
    
    
    public int continuaPartita() {
        try (FileInputStream fin = new FileInputStream("./myfiles/partitaInterrotta.bin");
            ObjectInputStream oin = new ObjectInputStream(fin);) {
            int p1 = (int) oin.readObject();
            inter = false;
            return p1;
            
            
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println("Errore: partita non ricaricata");
        }
        try {
            Files.delete(Paths.get("./myfiles/partitaInterrotta.bin"));         
        } catch (IOException ex) {
            System.out.println("Errore: partitaInterrotta.bin non cancellato");
        }
        
        return 0;
    }
    
}
