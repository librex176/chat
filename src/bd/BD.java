package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BD {
    
    private Connection con;
    
    public BD() {
        try {
            // Cargar el controlador JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establecer la conexión a la base de datos
            con = DriverManager.getConnection("jdbc:mysql://localhost/chat", "root", "");
            
            //System.out.println("Conexión establecida correctamente.");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error: No se ha encontrado el controlador de la base de datos.");
        } catch (SQLException ex) {
            Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error al conectar a la base de datos: " + ex.getMessage());
        }
    }
    
    public Connection getCon() {
        return con;
    }
    
    public void closeConnection() {
        if (con != null) {
            try {
                con.close();
                //System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException ex) {
                Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
                System.err.println("Error al cerrar la conexión: " + ex.getMessage());
            }
        }
    }
}
