

import javafx.collections.ObservableList;
import javafx.scene.Node;

public class Mano {

    // public ArrayList<Carta> carte = new ArrayList<Carta>();
    private ObservableList<Node> carte;
    private int totale;
    private int assi;
    
    public Carta getCarta(int i){
        return (Carta) this.carte.get(i);
    }

    public Mano(ObservableList<Node> carte) {
        this.carte = carte;
    }

    public void aggiungiCarta(Carta c, boolean aux) {
        carte.add(c);
        if (c.grado == Carta.Grado.ASSO) {
            assi++;
        }
        if (totale + c.valore > 21 && assi > 0) {
            totale = totale + c.valore - 10;
            assi--;
        } else {
            totale += c.valore;
        }
        c.creaGrafica(aux);
    }
    
    public void pulisci(){
        this.totale = 0;
        this.assi = 0;
        carte.clear();
    }
    
   
    
    public void rimuovi(Carta c1){
        if (c1.ix == 1)
        {
            this.assi--;
        }
        carte.remove(1,1);
        
    }
    
    public void mostra(Carta c1){
        c1.modificaGrafica();
    }

    public ObservableList<Node> getCarte() {
        return carte;
    }

    public void setCarte(ObservableList<Node> carte) {
        this.carte = carte;
    }

    public int getTotale() {
        return totale;
    }

    public void setTotale(int totale) {
        this.totale = totale;
    }

    public int getAssi() {
        return assi;
    }

    public void setAssi(int assi) {
        this.assi = assi;
    }
}
