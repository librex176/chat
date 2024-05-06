
package Controllers;

import bd.BD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.IndividualChatModel;

public class ChatsController {
    
    public int GetChatId(int chatterId1, int chatterId2)
    {
        BD bd = new BD();
        PreparedStatement sql;
        ResultSet res;
        int chatId = -1;
        try {
            sql = bd.getCon().prepareStatement("SELECT ConversacionId FROM chats WHERE (Usuario1Id = ? AND Usuario2Id= ?) OR (Usuario1Id = ? AND Usuario2Id= ?)");
            sql.setInt(1, chatterId1);
            sql.setInt(2, chatterId2);
            sql.setInt(3, chatterId2);
            sql.setInt(4, chatterId1);
            res = sql.executeQuery();
            
            if (res.next()) {
                chatId = res.getInt("ConversacionId");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bd.closeConnection();
        }
        return chatId;
    }
    
    public boolean CreateChat(int chatterId1, int chatterId2)
    {
        BD bd = new BD();
        PreparedStatement sql;
        try {
            sql = bd.getCon().prepareStatement("INSERT INTO chats (Usuario1Id, Usuario2Id) VALUES (?, ?)");
            sql.setInt(1, chatterId1);
            sql.setInt(2, chatterId2);
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
    
    public List<IndividualChatModel> SearchChats(int chatterId)
    {
        BD bd = new BD();
        PreparedStatement sql;
        ResultSet res;
        
        List<IndividualChatModel> chatsEncontrados = new ArrayList<>();
        IndividualChatModel chat;
        
        try {
            sql = bd.getCon().prepareStatement("SELECT ConversacionId, Usuario1Id, Usuario2Id FROM chats WHERE (Usuario1Id = ?) OR (Usuario2Id= ?)");
            sql.setInt(1, chatterId);
            sql.setInt(2, chatterId);
            res = sql.executeQuery();
            
            while (res.next()) {
                chat = new IndividualChatModel();
                chat.setChatId(res.getInt("ConversacionId"));
                chat.setChatterId1(res.getInt("Usuario1Id"));
                chat.setChatterId2(res.getInt("Usuario2Id"));
                chatsEncontrados.add(chat);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bd.closeConnection();
        }
        return chatsEncontrados;
    }
}
