/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Davide
 */
public class Utente {
    
     Mano manoU;
    private String nomeU;
    private int saldo;
    

    public String getNomeU() {
        return nomeU;
    }

    public void setNomeU(String nomeU) {
        this.nomeU = nomeU;
    }
    

    public Utente(){
        this.nomeU="";
        this.saldo = 0;
    }
    public Mano getManoU() {
        return manoU;
    }

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }
    
    public void aggiornaSaldo(int s){
        this.saldo = saldo + s;
    }
    
    
}
