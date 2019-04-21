
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Carta extends Parent {

    enum Seme {

        CUORI, MATTONI, FIORI, PICCHE;
    }

    enum Grado {

        DUE(2,2), TRE(3,3), QUATTRO(4,4), CINQUE(5,5), SEI(6,6), SETTE(7,7), OTTO(8,8), NOVE(9,9), DIECI(10,10),
        JACK(10,11), REGINA(10,12), RE(10,13), ASSO(11,1);

        final int valore;
        final int ix;

        Grado(int valore, int ix) {
            this.valore = valore;
            this.ix = ix;
        }

    }

    public Grado grado;
    public Seme seme;
    public int valore;
    public int ix;
    public Image fronte;
    public Image retro;
    public Text testo;
    Rectangle bg;

    public Carta(Grado gr, Seme sm) {
        this.grado = gr;
        this.seme = sm;
        this.valore = gr.valore;
        this.ix= gr.ix;

        this.retro = new Image("file:resources/CARTE/RETRO.PNG");
        String indirizzo = new String(toString());
        this.fronte = new Image("file:resources/CARTE/".concat(indirizzo).concat(".GIF"));

    }

    public String toString() {
        return grado.toString() + seme.toString();
    }


    public void creaGrafica(boolean aux) {
        bg = new Rectangle(63, 85);
        bg.setArcWidth(5);
        bg.setArcHeight(5);
        bg.setFill(Color.WHITE);

        ImageView scoperta = new ImageView(this.fronte);
        ImageView coperta = new ImageView(this.retro);

        if (aux == true)
        {
            getChildren().add(new StackPane(bg, scoperta));

        }
        else
        {
            getChildren().add(new StackPane(bg, coperta));
        }
    }
    
    public void modificaGrafica(){
        getChildren().clear();
        bg = new Rectangle(63, 85);
        bg.setArcWidth(5);
        bg.setArcHeight(5);
        bg.setFill(Color.WHITE);
        ImageView scoperta = new ImageView(this.fronte);
        getChildren().add(new StackPane(bg, scoperta));
    }
    
    public String getIndice(){
        String f1="";
        f1=Integer.toString(this.grado.ix);
        
        return f1;
    };
}
