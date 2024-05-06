
package Controllers;

import bd.BD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Message;

public class MessagesController extends BD {
    
    public boolean SendMessageToBD(String Message, int chatId, int userId){
        BD bd = new BD();
        PreparedStatement sql;
        try {
            sql = bd.getCon().prepareStatement("INSERT INTO mensajes (contenido, fecha, chat_id, UsuarioId) VALUES (?, (SELECT CURRENT_TIMESTAMP), ?, ?)");
            sql.setString(1, Message);
            sql.setInt(2, chatId);
            sql.setInt(3, userId);
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
    
    public List<Message> GetMessages(int chatId)
    {
        List<Message> mensajesChat = new ArrayList<Message>();
        Message mensaje;
        BD bd = new BD();
        PreparedStatement sql;
        ResultSet res;
        try {
            sql = bd.getCon().prepareStatement("SELECT contenido, usuarioId FROM mensajes WHERE chat_Id = ?");
            sql.setInt(1, chatId);
            res = sql.executeQuery();
            
            while (res.next()) {
                mensaje = new Message();
                mensaje.setMessageContent(res.getString("contenido"));
                mensaje.setUserId(res.getInt("UsuarioId"));
                mensajesChat.add(mensaje);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MessagesController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bd.closeConnection();
        }
        return mensajesChat;
    }
}