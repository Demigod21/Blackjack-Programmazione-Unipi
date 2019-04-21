/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author Davide
 */
public class TabellaMani extends TableView {

    public TabellaMani() {
        ConfigXML cx = new ConfigXML();
        setEditable(true);

        TableColumn firstNameCol = new TableColumn("Utente");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("nickUtente"));

        TableColumn lastNameCol = new TableColumn("Record");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("recordMani"));
         DatabaseBlackjack db = new DatabaseBlackjack();
        ObservableList<UtenteRecord> data = db.caricaVincenti(cx.pcc.xUtenti);
        
        

        setItems(data);
        getColumns().addAll(firstNameCol, lastNameCol);
        
        setLayoutX(25);
        setLayoutY(300); 
        setPrefSize(150, 220);
    }

}
