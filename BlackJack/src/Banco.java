/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Davide
 */
public class Banco {
    public Mano manoB;
    
    public Banco(){
    }
    
    public void Aggiungi(Carta b){
        this.manoB.aggiungiCarta(b, true);
    };
    
    public void mostra(Carta c2){
        manoB.mostra(c2);
    }
}
