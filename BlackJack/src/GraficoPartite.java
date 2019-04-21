
import com.sun.javafx.charts.Legend;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.*;
import javafx.scene.Group;
import javafx.scene.layout.Background;
 
public class GraficoPartite extends PieChart {
 
    GraficoPartite(ObservableList<PieChart.Data> datiGrafico){
        super(datiGrafico);
        setLayoutX(-145);
        setLayoutY(50);
        setMaxHeight(200);
        setLegendVisible(true);
        setLabelsVisible(false);
        Legend lg = (Legend) getLegend();
        lg.setPrefWidth(150);
        lg.setBackground(Background.EMPTY);
                
                
        
    }
 }
 
