/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Davide
 */

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

    public class UtenteRecord {

    private final SimpleStringProperty nickUtente;
    private final SimpleIntegerProperty recordMani;

    public UtenteRecord(String n1, int r1) {
        this.nickUtente = new SimpleStringProperty(n1);
        this.recordMani = new SimpleIntegerProperty(r1);
    }

    public String getNickUtente() {
        return nickUtente.get();
    }


    public int getRecordMani() {
        return recordMani.get();
    }



   

}
  