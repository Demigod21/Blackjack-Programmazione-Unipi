/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.thoughtworks.xstream.XStream;
import javafx.application.Application;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.stage.WindowEvent;

/**
 *
 * @author Davide
 */
public class BlackJack extends Application {
    
    private ConfigXML conf;
    private Mazzo mazzo = new Mazzo();
    private Banco bancoC;
    private Utente utenteC, utenteD;
    private int Puntata;
    private int maxPuntata;
    private DatabaseBlackjack db = new DatabaseBlackjack();
    private Text u1 = new Text("");
    private TextField consiglio = new TextField("Costo di 10 crediti");
    private TextField txtBlc = new TextField("");
    private TextField txtUte = new TextField("UTENTE");
    private TextField txtPnt;
    private GestoreCache gc;
    private SimpleBooleanProperty playable = new SimpleBooleanProperty(false); //variabile playable che mi aiuta a gestire l'attivazione dei bottoni
    private HBox carteBanco = new HBox(2);  //area per carte banco
    private HBox carteUtente = new HBox(2); //area per carte utente(1 mano)
    private HBox carteDividi = new HBox(2); //area per carte utente(2 mano)
    private EventiXML exml;
    private int numeroMano;
    private Button btnDividere = new Button("DIVIDERE");
    private Button btnConsiglio = new Button("GIOCATA CONSIGLIATA");
    private Button btnLogin = new Button("LOGIN");
    private Button btnPnt = new Button("PUNTARE");
    private Button btnPescare = new Button("PESCARE");
    private Button btnStare = new Button("STARE");
    


    private Parent createContent(int p23) {
        //creo le mani di utente e banco e sistemo l'area per le carte
        utenteC = new Utente();
        utenteC.manoU = new Mano(carteUtente.getChildren());
        carteBanco.setAlignment(Pos.CENTER);
        carteUtente.setAlignment(Pos.CENTER);
        bancoC = new Banco();
        bancoC.manoB = new Mano(carteBanco.getChildren());
        carteDividi.setAlignment(Pos.CENTER);
        utenteD = new Utente();
        utenteD.manoU = new Mano(carteDividi.getChildren());
        //inizio a creare lo stage
        Pane root = new Pane();
        root.setPrefSize(800, 600);
        Region background = new Region();
        background.setPrefSize(800, 600);
        background.setStyle("-fx-background-color: rgba(0, 0, 0, 1)");

        HBox rootLayout = new HBox(5);
        rootLayout.setPadding(new Insets(5, 5, 5, 5));
        //creo i 3 rettangoli principali verticali
        Rectangle leftBG = new Rectangle(200, 560);
        Rectangle centroBG = new Rectangle(375, 560);
        Rectangle rightBG = new Rectangle(200, 560);
        disponiRettangoli(leftBG, centroBG, rightBG);
        //SINISTRA
        VBox leftVBox = new VBox(20);
        conf = new ConfigXML();
        maxPuntata = conf.pcc.maxPuntata;
        leftVBox.setAlignment(Pos.TOP_CENTER);
        ObservableList<PieChart.Data> datiGrafico = db.caricaPartite(conf.pcc.gGiorni);
        GraficoPartite gr = new GraficoPartite(datiGrafico);
        Text titoloGrafo = new Text("Grafico Partite Ultimi "+conf.pcc.gGiorni+" giorni" );
        titoloGrafo.setStyle("-fx-font-size: 12 px;");
        TabellaMani table = new TabellaMani();
        leftVBox.getChildren().addAll(titoloGrafo);
        // CENTRO
        VBox centroVBox = new VBox(20);
        centroVBox.setAlignment(Pos.TOP_CENTER);
       
        HBox btnHBox = new HBox(10, btnPescare, btnStare, btnDividere);
        txtBlc = new TextField("BILANCIO : " + utenteC.getSaldo());
        txtPnt = new TextField(""+p23);

        HBox sottoHBox = new HBox(10, txtBlc, txtPnt, btnPnt);
        sottoHBox.setAlignment(Pos.CENTER);
        btnHBox.setAlignment(Pos.CENTER);
        HBox centroalto = new HBox(20); //parte centrale-alta, dove c'è utente e login
        centroalto.getChildren().addAll(txtUte, btnLogin);
        VBox centrobasso = new VBox(15); //parte centrale-bassa dove ci sono i bottoni e la puntata
        centrobasso.getChildren().addAll(btnHBox, sottoHBox);
        VBox centromedio = new VBox(15); //parte centrale-centrale dove ci sono le carte
        centromedio.getChildren().addAll(carteBanco, carteUtente, carteDividi);
        sistemaCentro(centrobasso, centromedio, centroalto); //chiamo sistemacentro che aggiusta la posizione degli elementi che compongono la parte centrale
        centroVBox.getChildren().addAll(centroalto, centromedio, centrobasso);
        // DESTRA
        VBox rightVBox = new VBox(20);
        rightVBox.setAlignment(Pos.CENTER);
        rightVBox.getChildren().addAll(btnConsiglio, consiglio, u1);
        // INSERISCO SX, CENTRO E DX AL ROOT
        rootLayout.getChildren().addAll(new StackPane(leftBG, leftVBox), new StackPane(centroBG, centroVBox), new StackPane(rightBG, rightVBox));
        root.getChildren().addAll(background, rootLayout ,gr, table);

         db = new DatabaseBlackjack();

        gestisciBottoni(); //riguarda la gestione dei bottoni

        return root;
    }

    ;
    
    public void gestisciBottoni() {
        btnDividere.setOnAction(event -> {
            EventiXML.send("Dividere");
            numeroMano = 1;
            dividere();
        });
        
        btnDividere.setDisable(true);
        btnPescare.disableProperty().bind(playable.not());
        btnStare.disableProperty().bind(playable.not());

        btnConsiglio.setOnAction(event -> {
            EventiXML.send("GIOCATA CONSIGLIATA");
            consigliata(utenteC.manoU.getCarta(0), utenteC.manoU.getCarta(1), bancoC.manoB.getCarta(0));
            utenteC.aggiornaSaldo(-10);
            txtBlc.setText("BILANCIO : " + utenteC.getSaldo());
            btnConsiglio.setDisable(true);
        });
        
  
        db = new DatabaseBlackjack();
        
        btnLogin.setOnAction(event -> {
           EventiXML.send("LOGIN");
            String utente = txtUte.getText();
            int saldo3 = db.crcSaldo(utente);
            utenteC.setSaldo(saldo3);
            utenteC.setNomeU(txtUte.getText());
            txtBlc.setText("BILANCIO : " + utenteC.getSaldo());
            
        });

        btnPnt.setOnAction(event -> {
            EventiXML.send("PUNTARE");
            txtBlc.setText("BILANCIO : " + utenteC.getSaldo());
            
            Puntata = Integer.parseInt(txtPnt.getText());
            if (Puntata>maxPuntata)
                Puntata=maxPuntata;
            txtPnt.setText(""+Puntata);
            btnConsiglio.setDisable(false);
            startNewGame();
            btnPnt.setDisable(true);
            if (utenteC.manoU.getCarta(0).ix == utenteC.manoU.getCarta(1).ix) {
                    btnDividere.setDisable(false);
        }
        });

        btnStare.setOnAction(event -> {
            EventiXML.send("STARE");
            if (numeroMano == 1) {
               numeroMano = 2;
               
            } else {
                bancoC.manoB.mostra(bancoC.manoB.getCarta(1));
                while (bancoC.manoB.getTotale() < 17) {
                    bancoC.manoB.aggiungiCarta(mazzo.distribuisci(), true);
                }
                endgame(txtUte.getText());
            }

        });

        btnPescare.setOnAction(event -> {
            EventiXML.send("PESCARE");
            if (numeroMano == 1) {
                utenteD.manoU.aggiungiCarta(mazzo.distribuisci(), true);
                if (utenteD.manoU.getTotale() > 21) {
                    numeroMano = 2;
                    
                }
            } else {
                utenteC.manoU.aggiungiCarta(mazzo.distribuisci(), true);
              
                if (utenteC.manoU.getTotale() > 21) {
                    bancoC.manoB.mostra(bancoC.manoB.getCarta(1));
                    
                    endgame(txtUte.getText());
                }
            }

        });
        

    }

    ;
    
    
    public void dividere() {
        if (utenteC.manoU.getCarta(0).ix == utenteC.manoU.getCarta(1).ix) {
            utenteC.manoU.setTotale(utenteC.manoU.getTotale() - utenteC.manoU.getCarta(1).valore);
            utenteD.manoU.aggiungiCarta(utenteC.manoU.getCarta(1), true);
            utenteC.manoU.rimuovi(utenteC.manoU.getCarta(1));
            btnDividere.setDisable(true);

        } else {

        }
    }

    ;
    
    
    
    @Override
    public void start(Stage primaryStage) {
        
        gc = new GestoreCache();
        int p23 = gc.continuaPartita();
        primaryStage.setScene(new Scene(createContent(p23)));
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setResizable(false);
        primaryStage.setTitle("BlackJack");
        EventiXML.send("AVVIO");
        primaryStage.show();
        
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            EventiXML.send("TERMINE");
            int Puntata2 = Integer.parseInt(txtPnt.getText());
            db.aggiornaPartita(utenteC.getNomeU());
            gc.salvaPuntata(Puntata2);
        });
    }

        /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private void startNewGame() {
        numeroMano = 0;
        bancoC.manoB.pulisci();
        utenteC.manoU.pulisci();
        utenteD.manoU.pulisci();
        u1.setText("");
        playable.set(true);
        mazzo.riempi();
        bancoC.manoB.aggiungiCarta(mazzo.distribuisci(), true);
        bancoC.manoB.aggiungiCarta(mazzo.distribuisci(), false);
        utenteC.manoU.aggiungiCarta(mazzo.distribuisci(), true);
        utenteC.manoU.aggiungiCarta(mazzo.distribuisci(), true);
        consiglio.setText("Costo di 10 crediti");
        if (utenteC.manoU.getCarta(0).ix == utenteC.manoU.getCarta(1).ix) {
            btnDividere.setDisable(false);
        }
    }
    
    
    public void endgame(String nutente) {
        playable.set(false);
        int totaleU = utenteC.manoU.getTotale();
        int totaleB = bancoC.manoB.getTotale();

        int totaleD;
        
        int maniVinte=0;
        int maniPerse=0;

        if (numeroMano == 2) {
            totaleU = utenteC.manoU.getTotale();
            totaleD = utenteD.manoU.getTotale();
            if (totaleB == 21 || totaleD > 21 || totaleB == totaleD || (totaleB < 21 && totaleB > totaleD)) {
                 maniPerse++;
                 db.partitaPersa(nutente);
                 
            } else if (totaleD == 21 || totaleB > 21 || totaleD > totaleB) {
                  maniVinte++;
                  db.partitaVinta(nutente);
            }
        }

        String winner = "ERRORE";

        if (totaleB == 21 || totaleU > 21 || totaleB == totaleU || (totaleB < 21 && totaleB > totaleU)) {
            winner = "BANCO ";
            maniPerse++;
            db.partitaPersa(nutente);
        } else if (totaleU == 21 || totaleB > 21 || totaleU > totaleB) {
            maniVinte++;
            winner = "UTENTE ";
            db.partitaVinta(nutente);
        }
        int aggiorna = Puntata*maniVinte - Puntata*maniPerse;
        utenteC.aggiornaSaldo(aggiorna);
        db.aggiornaSaldo(utenteC.getSaldo(), nutente);
        txtBlc.setText("BILANCIO : " + utenteC.getSaldo());
        u1.setText(winner + "VINCE!");
        
        btnPnt.setDisable(false);
    }



    //a seconda della mano, la funzione consigliata va a chiamare una delle 3 funzioni per la ricerca del consiglio
    public void consigliata(Carta u1, Carta u2, Carta b1) {
        String consiglio1;
        if (u1.grado.ix == u2.grado.ix) {
            consiglio1 = consigliaCoppia(u1, u2, b1);
        } else if (u1.grado.ix == 1 || u2.grado.ix == 1) {

            consiglio1 = consigliaAsso(u1, u2, b1);
        } else {
            consiglio1 = consigliaNormale(u1, u2, b1);
        }

        String consiglio2 = "";
        switch(consiglio1){
            case "H":
                consiglio2= "PESCARE";
                break;
            case "S":
                consiglio2 = "STARE";
                break;
            case "D":
                consiglio2 = "DIVIDERE";
                break;
        }
        consiglio.setText(consiglio2);

    }

    ;
    
    
    //se in una carta è presente l'asso, va a ricercare il consiglio nelle righe della matrice riguardanti l'asso
    public String consigliaAsso(Carta u1, Carta u2, Carta b1) {
        int riga = 0;
        int colonna;
        colonna = b1.valore - 2;
        if (u1.grado.ix == 1) {
            riga = u2.valore + 8;
        } else if (u2.grado.ix == 1) {
            riga = u1.valore + 8;
        }
        String cons = matriceConsiglio[riga][colonna];
        return cons;
    }

    ;
    
    
    //se le due carte sono una coppia, va a riceracre consiglio nelle righe della matrice riguardanti le coppie
    public String consigliaCoppia(Carta u1, Carta u2, Carta b1) {
        int riga;
        int colonna;
        int somma = u1.valore + u2.valore;
        riga = u1.valore + 17;
        colonna = b1.valore - 2;
        String cons = matriceConsiglio[riga][colonna];
        return cons;
    }

    ;

    //altrimenti cerca il consiglio nelle righe della matrice "normali"
    public String consigliaNormale(Carta u1, Carta u2, Carta b1) {
        int riga;
        int colonna;
        int somma = u1.valore + u2.valore;
        colonna = b1.valore - 2;
        if (somma <= 8) {
            riga = 0;
        } else if (somma >= 17) {
            riga = 9;
        } else {
            riga = somma - 8;
        }
        String cons = matriceConsiglio[riga][colonna];
        return cons;
    }
    ;
    

    
    
    private void disponiRettangoli(Rectangle lg, Rectangle cg, Rectangle rg){
        
        lg.setArcWidth(50);
        lg.setArcHeight(50);
        lg.setFill(Color.ORANGE);
        
        rg.setArcWidth(50);
        rg.setArcHeight(50);
        rg.setFill(Color.RED);
        
        cg.setArcWidth(50);
        cg.setArcHeight(50);
        cg.setFill(Color.GREEN);
        
    }
    

    public void sistemaCentro(VBox cb, VBox cm, HBox ca){
        cb.setAlignment(Pos.BOTTOM_CENTER);
        cb.setPrefHeight(35);
        ca.setAlignment(Pos.TOP_CENTER);
        cm.setAlignment(Pos.CENTER);
        cm.setPrefHeight(410);
        
                
        txtBlc.setEditable(false);
        txtBlc.setPrefWidth(100);
        txtBlc.setPrefHeight(40);
        txtBlc.setStyle("-fx-font-size: 12 px;");
        
        txtPnt.setPrefWidth(100);
        txtPnt.setPrefHeight(40);
        txtPnt.setStyle("-fx-font-size: 12 px;");
        
        btnPnt.setPrefHeight(40);
        btnPnt.setPrefWidth(100);
        btnPnt.setStyle("-fx-font-size: 12 px;");
        
        txtUte.setPrefHeight(20);
        txtUte.setPrefWidth(100);
        txtUte.setStyle("-fx-font-size: 12 px;");
        
        btnLogin.setPrefHeight(20);
        btnLogin.setPrefWidth(100);
        btnLogin.setStyle("-fx-font-size: 12 px;");   
        
        btnPescare.setPrefWidth(100);
        btnPescare.setPrefHeight(40);
        btnPescare.setStyle("-fx-font-size: 12 px;");
        
        btnDividere.setPrefWidth(100);
        btnDividere.setPrefHeight(40);
        btnDividere.setStyle("-fx-font-size: 12 px;");
        
        btnStare.setPrefWidth(100);
        btnStare.setPrefHeight(40);
        btnStare.setStyle("-fx-font-size: 12 px;");
        
        
    }
    
    private final String[][] matriceConsiglio = new String[][] {
			//                                Carta Banco
			/*Carte Utente     2    3    4    5    6    7    8    9   10    A   
                                           0    1    2    3    4    5    6    7   8     9  */
			/*<=8        0*/ {"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},
			/*9          1*/ {"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},
			/*10         2*/ {"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},
			/*11         3*/ {"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},
			/*12         4*/ {"H", "H", "S", "S", "S", "H", "H", "H", "H", "H"},
			/*13         5*/ {"S", "S", "S", "S", "S", "H", "H", "H", "H", "H"},
			/*14         6*/ {"S", "S", "S", "S", "S", "H", "H", "H", "H", "H"},
			/*15         7*/ {"S", "S", "S", "S", "S", "H", "H", "H", "H", "H"},
			/*16         8*/ {"S", "S", "S", "S", "S", "H", "H", "H", "H", "H"},
			/*17+         9*/{"S", "S", "S", "S", "S", "S", "S", "S", "S", "S"},
			/*A2         10*/{"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},
			/*A3         11*/{"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},
			/*A4         12*/{"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},
			/*A5         13*/{"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},
			/*A6         14*/{"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},
			/*A7         15*/{"S", "H", "H", "H", "H", "S", "S", "H", "H", "H"},
			/*A8         16*/{"S", "S", "S", "S", "S", "S", "S", "S", "S", "S"},
			/*A9         17*/{"S", "S", "S", "S", "S", "S", "S", "S", "S", "S"},
                        /*A10        18*/{"S", "S", "S", "S", "S", "S", "S", "S", "S", "S"},
			/*2-2        19*/{"D", "D", "D", "D", "D", "D", "H", "H", "H", "H"},
			/*3-3        20*/{"D", "D", "D", "D", "D", "D", "H", "H", "H", "H"},
			/*4-4        21*/{"H", "H", "H", "D", "D", "H", "H", "H", "H", "H"},
			/*5-5        22*/{"H", "H", "H", "H", "H", "H", "H", "H", "H", "H"},
			/*6-6        23*/{"D", "D", "D", "D", "D", "H", "H", "H", "H", "H"},
			/*7-7        24*/{"D", "D", "D", "D", "D", "D", "H", "H", "H", "H"},
			/*8-8        25*/{"D", "D", "D", "D", "D", "D", "D", "D", "D", "D"},
			/*9-9        26*/{"D", "D", "D", "D", "D", "S", "D", "D", "S", "S"},
			/*10-10      27*/{"S", "S", "S", "S", "S", "S", "S", "S", "S", "S"},
			/*A-A        28*/{"D", "D", "D", "D", "D", "D", "D", "D", "D", "D"}};
    
   
    
    
    
}
