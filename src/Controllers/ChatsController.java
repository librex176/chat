
package Controllers;

import bd.BD;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.IndividualChatModel;

// del 16 al 30 en queries
public class ChatsController {
    
    public int GetChatId(int chatterId1, int chatterId2, String ip)
    {
        // conexion al server, el server se encargara de realizar la consulta a la bd
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try {
            socket = new Socket(ip, 1234); // Usa la IP de tu servidor
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // consulta al server con los datos requeridos
            String sql;
            // se envia un string con el numero de la query a ejecutar en el server y los datos 
            // necesarios para la ejecucion de la query
            sql = "16:" + chatterId1 + ":" + chatterId2;
            out.writeBytes(sql + "\n");
            out.flush();
            
            // recibir el resultado de la consulta del server
            String resultado = "0";
            resultado = in.readLine();
            
            // manejar la salida entregada por el server por parte de la bd
            return Integer.parseInt(resultado);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
        //////////////////////////////////////////////////////////////////////
//        BD bd = new BD();
//        PreparedStatement sql;
//        ResultSet res;
//        int chatId = -1;
//        try {
//            sql = bd.getCon().prepareStatement("SELECT ConversacionId FROM chats WHERE (Usuario1Id = ? AND Usuario2Id= ?) OR (Usuario1Id = ? AND Usuario2Id= ?)");
//            sql.setInt(1, chatterId1);
//            sql.setInt(2, chatterId2);
//            sql.setInt(3, chatterId2);
//            sql.setInt(4, chatterId1);
//            res = sql.executeQuery();
//            
//            if (res.next()) {
//                chatId = res.getInt("ConversacionId");
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            bd.closeConnection();
//        }
//        return chatId;
    }
    
    public boolean CreateChat(int chatterId1, int chatterId2, String ip)
    {
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try {
            socket = new Socket(ip, 1234); // Usa la IP de tu servidor
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // consulta al server con los datos requeridos
            String sql;
            // se envia un string con el numero de la query a ejecutar en el server y los datos 
            // necesarios para la ejecucion de la query
            sql = "17:" + chatterId1 + ":" + chatterId2;
            out.writeBytes(sql + "\n");
            out.flush();
            
            // recibir el resultado de la consulta del server
            String resultado = "0";
            resultado = in.readLine();
            
            // manejar la salida entregada por el server por parte de la bd
            return resultado.equals("true");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
        //////////////////////////////////////
//        BD bd = new BD();
//        PreparedStatement sql;
//        try {
//            sql = bd.getCon().prepareStatement("INSERT INTO chats (Usuario1Id, Usuario2Id) VALUES (?, ?)");
//            sql.setInt(1, chatterId1);
//            sql.setInt(2, chatterId2);
//            int comprobar = sql.executeUpdate();
//            
//            bd.closeConnection();
//           
//            return comprobar > 0;
//        } catch (SQLException ex) {
//            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
//            return false; 
//        } finally
//        {
//            bd.closeConnection();
//        }
    }
    
    public List<IndividualChatModel> SearchChats(int chatterId, String ip)
    {
        List<IndividualChatModel> chats = new ArrayList<>();
        IndividualChatModel chat;
        
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try {
            socket = new Socket(ip, 1234); // Usa la IP de tu servidor
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // consulta al server con los datos requeridos
            String sql;
            // se envia un string con el numero de la query a ejecutar en el server y los datos 
            // necesarios para la ejecucion de la query
            sql = "18:" + chatterId;
            out.writeBytes(sql + "\n");
            out.flush();
            
            // recibir el resultado de la consulta del server
            String resultado = "0";
            while(true)
            {
                resultado = in.readLine();
                
                if(resultado == null)
                {
                    break;
                }
                
                System.out.println(resultado);
                String[] parts = resultado.split(":");
                
                chat = new IndividualChatModel();
                chat.setChatId(Integer.parseInt(parts[0]));
                chat.setChatterId1(Integer.parseInt(parts[1]));
                chat.setChatterId2(Integer.parseInt(parts[2]));
                System.out.println("Chatter 2 recibido: " + chat.getChatterId2());
                
                chats.add(chat);
            }
            
            // manejar la salida entregada por el server por parte de la bd
            return chats;
            
        } catch (IOException e) {
            
        }
        return chats;
        ////////////////////////////////////////////////////////
//        BD bd = new BD();
//        PreparedStatement sql;
//        ResultSet res;
//        
//        List<IndividualChatModel> chatsEncontrados = new ArrayList<>();
//        IndividualChatModel chat;
//        
//        try {
//            sql = bd.getCon().prepareStatement("SELECT ConversacionId, Usuario1Id, Usuario2Id FROM chats WHERE (Usuario1Id = ?) OR (Usuario2Id= ?)");
//            sql.setInt(1, chatterId);
//            sql.setInt(2, chatterId);
//            res = sql.executeQuery();
//            
//            while (res.next()) {
//                chat = new IndividualChatModel();
//                chat.setChatId(res.getInt("ConversacionId"));
//                chat.setChatterId1(res.getInt("Usuario1Id"));
//                chat.setChatterId2(res.getInt("Usuario2Id"));
//                chatsEncontrados.add(chat);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            bd.closeConnection();
//        }
//        return chatsEncontrados;
    }
}
