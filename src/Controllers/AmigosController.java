/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Amigos;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

// del 1 al 15 en queries
public class AmigosController{
        
    public String selectNameByUserId(int usuarioId, String ip)
    {
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        String nombre = null;
        try
        {
            socket = new Socket(ip, 1234);
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String sql = "2:" + usuarioId;
            out.writeBytes(sql + "\n");
            out.flush();
            // recibir el resultado de la consulta del server
            String resultado = in.readLine();
            return resultado;
        } catch (IOException ex) {
            Logger.getLogger(AmigosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    // intento del server con selectMisamigosUsuarios
    public ArrayList<String[]> selectMisAmigosByUserIdServer(int userId, String ip) {
        // conexion al server, el server se encargara de realizar la consulta a la bd
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try {
            socket = new Socket(ip, 1234); // Usa la IP de tu servidor Axel: 192.168.100.76
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // consulta al server con los datos requeridos
            String sql;
            // se envia un string con el numero de la query a ejecutar en el server y los datos 
            // necesarios para la ejecucion de la query
            sql = "2:" + userId;
            out.writeBytes(sql + "\n");
            out.flush();
            
            // recibir el resultado de la consulta del server
            String resultado = in.readLine();
            System.out.println("resultado en un mismo string: "+resultado);
            if(!resultado.equals("0")){
                String[] parts = resultado.split("_");
                ArrayList<String[]> amigos = new ArrayList<>();
                for(String p : parts)
                {
                    String[] ob = p.split(":");
                    amigos.add(ob);
                }
                if(!amigos.isEmpty()){
                    return amigos;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; 
    }
            
    // intento del server con delete amistad
    public boolean deleteAmigoServer(int amigosId, String ip) {
        // conexion al server, el server se encargara de realizar la consulta a la bd
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try {
            socket = new Socket(ip, 1234); // Usa la IP de tu servidor Axel: 192.168.100.76
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // consulta al server con los datos requeridos
            String sql;
            // se envia un string con el numero de la query a ejecutar en el server y los datos 
            // necesarios para la ejecucion de la query
            sql = "3:" + amigosId;
            out.writeBytes(sql + "\n");
            out.flush();
            
            // recibir el resultado de la consulta del server
            String resultado = in.readLine();
            System.out.println("resultado en un mismo string: "+resultado);
            if(resultado.contains("1"))
            {
                return true;
            }
            

        } catch (IOException e) {
        }
        return false; 
    }
    
    public boolean SearchFriends(int usuarioId1, int usuarioId2, String ip)
    {
        // conexion al server, el server se encargara de realizar la consulta a la bd
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try {
            socket = new Socket(ip, 1234); // Usa la IP de tu servidor Axel: 192.168.100.76
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // consulta al server con los datos requeridos
            String sql;
            // se envia un string con el numero de la query a ejecutar en el server y los datos 
            // necesarios para la ejecucion de la query
            sql = "5:" + usuarioId1 + ":" + usuarioId2;
            out.writeBytes(sql + "\n");
            out.flush();
            
            // recibir el resultado de la consulta del server
            String resultado = in.readLine();
            return (resultado.equals("true"));
            

        } catch (IOException e) {
        }
        return false;
        ////////////////////////////////////////////////////////
//        BD bd = new BD();
//        PreparedStatement sql;
//        ResultSet res;
//        
//        try
//        {
//            sql = bd.getCon().prepareStatement("SELECT amigosId FROM listaamigos WHERE UsuarioDuenoId =? AND UsuarioId = ?");
//            sql.setInt(1, usuarioId1);
//            sql.setInt(2, usuarioId2);
//            res = sql.executeQuery();
//            
//            if(res.next())
//            {
//                bd.closeConnection();
//                return true;
//            }
//            
//        } catch (SQLException ex) {
//            Logger.getLogger(AmigosController.class.getName()).log(Level.SEVERE, null, ex);
//        } finally
//        {
//            bd.closeConnection();
//        }
//        return false;
    }
}
