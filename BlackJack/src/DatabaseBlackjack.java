
import java.sql.*;
import javafx.collections.*;
import javafx.scene.chart.*;

/**
 *
 * @author Davide
 */
public class DatabaseBlackjack {

    public int crcSaldo(String ut) {
        Connection conn1 = null;
        int sd = 0;
        try {
            String url1 = "jdbc:mysql://localhost:3306/blackjack";
            String user = "root";
            String password = "";
            conn1 = DriverManager.getConnection(url1, user, password);
            PreparedStatement ps = conn1.prepareStatement("SELECT saldo FROM blackjack.utente WHERE utente.nomeUtente='" + ut + "';");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                sd = rs.getInt("saldo");
            }

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

        return sd;
    }

    ;
    
    public void aggiornaSaldo(int s1, String ut){
        Connection conn1 = null;
        int sd = 0;
        try {
            String url1 = "jdbc:mysql://localhost:3306/blackjack";
            String user = "root";
            String password = "";
            conn1 = DriverManager.getConnection(url1, user, password);
            PreparedStatement ps = conn1.prepareStatement("UPDATE utente SET saldo=" + s1 + " WHERE utente.nomeUtente='" + ut + "';");
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        
    }
    
    ;
     public void partitaVinta(String ut) {
        Connection conn1 = null;
        try {
            String url1 = "jdbc:mysql://localhost:3306/blackjack";
            String user = "root";
            String password = "";
            conn1 = DriverManager.getConnection(url1, user, password);
            PreparedStatement ps = conn1.prepareStatement("UPDATE utente SET maniGiocate=maniGiocate+1, maniVinte=maniVinte+1 WHERE utente.nomeUtente='" + ut + "';");
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }

    ;
     public void partitaPersa(String ut) {
        Connection conn1 = null;
        try {
            String url1 = "jdbc:mysql://localhost:3306/blackjack";
            String user = "root";
            String password = "";
            conn1 = DriverManager.getConnection(url1, user, password);
            PreparedStatement ps = conn1.prepareStatement("UPDATE utente SET maniGiocate=maniGiocate+1 WHERE utente.nomeUtente='" + ut + "';");
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }
;
     
     public void aggiornaPartita(String ut) {
        Connection conn1 = null;
        try {
            String url1 = "jdbc:mysql://localhost:3306/blackjack";
            String user = "root";
            String password = "";
            conn1 = DriverManager.getConnection(url1, user, password);
            PreparedStatement ps = conn1.prepareStatement("INSERT INTO partita (nUtente, datapartita)VALUES ('" + ut + "', NOW());");
            ps.executeUpdate();

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }
;
     
     
public ObservableList<PieChart.Data> caricaPartite(int g) {
        ObservableList<PieChart.Data> datiGrafico = FXCollections.observableArrayList();
        try (Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/blackjack", "root","");   
             PreparedStatement ps = co.prepareStatement(
                "SELECT nUtente, COUNT(*) as numeroPartite FROM blackjack.partita WHERE  (partita.datapartita > current_date - interval '"+g+"' day) GROUP BY nUtente;");)
            {
              ResultSet rs = ps.executeQuery();
              while (rs.next())
                datiGrafico.add(new PieChart.Data(rs.getString("nUtente") + ": " + rs.getInt("numeroPartite"), rs.getInt("numeroPartite")));
            } catch (SQLException e) {System.err.println(e.getMessage());}
        return datiGrafico;
    }
     

     public ObservableList<UtenteRecord> caricaVincenti(int x) {
        ObservableList<UtenteRecord> datiTabella = FXCollections.observableArrayList();
        try (Connection co = DriverManager.getConnection("jdbc:mysql://localhost:3306/blackjack", "root","");   
             PreparedStatement ps = co.prepareStatement("SELECT nomeUtente, maniVinte FROM blackjack.utente LIMIT ?");)
            {
              ps.setInt(1, x);
              ResultSet rs = ps.executeQuery();
              while (rs.next())
                datiTabella.add(new UtenteRecord(rs.getString("nomeUtente"), rs.getInt("maniVinte")));
            } catch (SQLException e) {System.err.println(e.getMessage());}
        return datiTabella;
    } 
    
     
     
     
}

