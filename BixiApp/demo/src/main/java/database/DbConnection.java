/**
 * @author inforkgodara
 */
package database;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DbConnection {
    private Connection con;
    private static DbConnection dbc;
    

    private DbConnection() {
        // try {
        // // FileInputStream fis=new
        // FileInputStream("./assets/database/connection.prop");
        // // Properties p=new Properties ();
        // // p.load(fis);
        String url = "jdbc:mysql://localhost:3306/tvm";
        Properties info = new Properties();
        info.put("user", "root");
        info.put("password", "sdm@23");
        String username = "root";
        String password = "sdm@23";

        System.out.println("Connecting database...");

        try {
            
            Connection connection = DriverManager.getConnection(url, info);
            if (connection != null) {
                System.out.println("Database connected!");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Cannot connect the database!", e);
        }
        // con=DriverManager.getConnection(
        // "jdbc:mysql://localhost:3306/tvm","root","sdm@23");
        // } catch (Exception ex) {
        // Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        // }
    }

    public static DbConnection getDatabaseConnection() {
        if (dbc == null) {
            dbc = new DbConnection();
        }
        return dbc;
    }

    public Connection getConnection() {
        return con;
    }

    public static void main(String[] args) {
        new DbConnection();
    }
}