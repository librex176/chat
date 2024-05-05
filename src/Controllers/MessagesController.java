
package Controllers;

import bd.BD;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessagesController extends BD {
    
    public boolean SendMessageToBD(String Message, int chatId){
        BD bd = new BD();
        PreparedStatement sql;
        try {
            sql = bd.getCon().prepareStatement("INSERT INTO mensajes (contenido, fecha, chat_id) VALUES (?, (SELECT CURRENT_TIMESTAMP), ?)");
            sql.setString(1, Message);
            sql.setInt(2, chatId);
            int comprobar = sql.executeUpdate();
            bd.closeConnection();
           
            return comprobar > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            return false; 
        } finally
        {
            bd.closeConnection();
        }
    }
}