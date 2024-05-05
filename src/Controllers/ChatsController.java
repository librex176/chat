
package Controllers;

import bd.BD;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatsController extends BD {
    
    public boolean SendMessageToBD(String Message,int UserId,int ChatterId){
        BD bd = new BD();
        PreparedStatement sql;
        try {
            sql = bd.getCon().prepareStatement("INSERT INTO chats (Usuario1Id, Usuario2Id, Mensaje) VALUES (?, ?, ?)");
            sql.setInt(1, UserId);
            sql.setInt(2, ChatterId);
            sql.setString(3, Message);
            int comprobar = sql.executeUpdate();
            bd.closeConnection();
           
            return comprobar > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            return false; 
        }
    }
}