/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




public class Mazzo {
    private Carta[] carte = new Carta[52];
    
    public Mazzo(){
        riempi();
    }
    
    public final void riempi(){
        int i = 0;
        for (Carta.Seme seme : Carta.Seme.values())
        {
            for (Carta.Grado grado : Carta.Grado.values())
            {
                carte[i++] = new Carta(grado, seme);
            }
        }
    }
    
    public Carta distribuisci(){
        Carta carta = null;
        while (carta == null)
        {
            int indice = (int)(Math.random()*carte.length);
            carta = carte[indice];
            carte[indice] = null;  
        }
        return carta;
    }
}
