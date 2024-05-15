
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
import models.Message;

// del 31 al 45 en queries
public class MessagesController extends BD {
    
    public boolean SendMessageToServer(String Message, int chatId, int userId, String ip){
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
            sql = "31:" + Message + ":" + chatId + ":" + userId;
            out.writeBytes(sql + "\n");
            out.flush();
            
            // recibir el resultado de la consulta del server
            String resultado = "0";
            resultado = in.readLine();
            
            System.out.println(resultado);
            
            // manejar la salida entregada por el server por parte de la bd
            return resultado.equals("true");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
        //////////////////////////////////////////////////////////////////////
//        BD bd = new BD();
//        PreparedStatement sql;
//        try {
//            sql = bd.getCon().prepareStatement("INSERT INTO mensajes (contenido, fecha, chat_id, UsuarioId) VALUES (?, (SELECT CURRENT_TIMESTAMP), ?, ?)");
//            sql.setString(1, Message);
//            sql.setInt(2, chatId);
//            sql.setInt(3, userId);
//            int comprobar = sql.executeUpdate();
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
    
    public List<Message> GetMessages(int chatId, String ip)
    {
        List<Message> mensajesChat = new ArrayList<Message>();
        Message mensaje;
        
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
            sql = "32:" + chatId;
            out.writeBytes(sql + "\n");
            out.flush();
            
            // recibir el resultado de la consulta del server
            String resultado;
            while(true)
            {
                out.writeBytes("Recibido");
                
                resultado = in.readLine();
                
                if(resultado.equals("Mensajes Terminados"))
                {
                    break;
                }
                System.out.println(resultado);
                String[] parts = resultado.split(":");
                
                mensaje = new Message();
                mensaje.setUserId(Integer.parseInt(parts[0]));
                mensaje.setMessageContent(parts[1]);
                
                mensajesChat.add(mensaje);
            }
            
            
            // manejar la salida entregada por el server por parte de la bd
            return mensajesChat;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mensajesChat;
        
        ////////////////////////////////////
//        List<Message> mensajesChat = new ArrayList<Message>();
//        Message mensaje;
//        BD bd = new BD();
//        PreparedStatement sql;
//        ResultSet res;
//        try {
//            sql = bd.getCon().prepareStatement("SELECT contenido, usuarioId FROM mensajes WHERE chat_Id = ?");
//            sql.setInt(1, chatId);
//            res = sql.executeQuery();
//            
//            while (res.next()) {
//                mensaje = new Message();
//                mensaje.setMessageContent(res.getString("contenido"));
//                mensaje.setUserId(res.getInt("UsuarioId"));
//                mensajesChat.add(mensaje);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(MessagesController.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            bd.closeConnection();
//        }
//        return mensajesChat;
    }
    
    public boolean DeleteMessagesFromChat(int chatId)
    {
        BD bd = new BD();
        PreparedStatement sql;
        try {
            sql = bd.getCon().prepareStatement("DELETE FROM mensajes WHERE chat_id = ?");
            sql.setInt(1, chatId);
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