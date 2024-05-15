package Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Usuarios;
import bd.BD;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class UsuarioController {
    
    //Validar inicio de sesion
    public String verificarCredenciales(String nombreUsuario, String contraseña, String ip) {
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
            sql = "1:" + nombreUsuario + ":" + contraseña;
            System.out.println(sql);
            out.writeBytes(sql + "\n");
            out.flush();
            
            // recibir el resultado de la consulta del server
            String resultado = "0";
            resultado = in.readLine();
            
            // manejar la salida entregada por el server por parte de la bd
            return resultado; // retornar de acuerdo a la consulta al servidor
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "0";
        
        //////////////////////////////////////////////////////
//        BD bd = new BD();
//        PreparedStatement sql;
//        ResultSet res;
//        try {
//            sql = bd.getCon().prepareStatement("SELECT * FROM usuarios WHERE NombreUsuario = ? AND Pass = ?");
//            sql.setString(1, nombreUsuario);
//            sql.setString(2, contraseña);
//            res = sql.exe  cuteQuery();
//            
//            
//            return res.next();
//        } catch (SQLException ex) {
//            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
//            return false; 
//        } finally {
//            
//            bd.closeConnection();
//        }
    }
    
    //Insertar un usuario
    public boolean insertarUsuario(String nombreUsuario, String contraseña, String cancionFavorita, String ip) {
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
            sql = "2:" + nombreUsuario + ":" + contraseña + ":" + cancionFavorita;
            out.writeBytes(sql);
            
            // recibir el resultado de la consulta del server
            String resultado = in.readLine();
            
            // manejar la salida entregada por el server por parte de la bd
            return resultado.equals("1"); // retornar de acuerdo a la consulta al servidor
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
///////////////////////////////////////////////////////////////////////////////////////////////////
//        BD bd = new BD();
//        PreparedStatement sql;
//        try {
//            sql = bd.getCon().prepareStatement("INSERT INTO usuarios (NombreUsuario, Pass, RespuestaPreguntaConfianza, StatusConexion) VALUES (?, ?, ?, ?)");
//            sql.setString(1, nombreUsuario);
//            sql.setString(2, contraseña);
//            sql.setString(3, cancionFavorita);
//            sql.setString(4, "0");
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
    
    // intentar agarrar lista conectados y desconectados con el server
    public ArrayList<String[]> usuariosPorConexionServer(int conexion, int userId, String ip) {
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
            sql = "4:" + userId+":"+conexion;
            out.writeBytes(sql + "\n");
            out.flush();
            
            // recibir el resultado de la consulta del server
            String resultado = in.readLine();
           // System.out.println("resultado en un mismo string: "+resultado);
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
    // borrar este
    public ArrayList<Usuarios> usuariosPorConexion(int conexion, int usuarioId)
    {
        BD bd = new BD();
        PreparedStatement sql;
        ArrayList<Usuarios> usuarios;
        ResultSet r;
        try
        {
            sql = bd.getCon().prepareStatement("SELECT NombreUsuario, UsuarioId FROM usuarios WHERE StatusConexion=? AND UsuarioId!=?");
            
            sql.setInt(1,conexion);
            sql.setInt(2,usuarioId);
            usuarios = new ArrayList<Usuarios>();
            r = sql.executeQuery();
            while(r.next())
            {
                Usuarios x = new Usuarios();
                x.nombreUsuario = r.getString("NombreUsuario");
                x.usuarioId = r.getInt("UsuarioId");
                usuarios.add(x);
            }
          
            return usuarios;
        } catch (SQLException ex) {
            Logger.getLogger(AmigosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public int RetornarId(String nombreUsuario, String contraseña) {
        BD bd = new BD();
        PreparedStatement sql;
        ResultSet res;
        int userId = -1; // Valor predeterminado si las credenciales son incorrectas
        try {
            sql = bd.getCon().prepareStatement("SELECT UsuarioId FROM usuarios WHERE NombreUsuario = ? AND Pass = ?");
            sql.setString(1, nombreUsuario);
            sql.setString(2, contraseña);
            res = sql.executeQuery();
            
            if (res.next()) {
                userId = res.getInt("UsuarioId");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bd.closeConnection();
        }
        return userId;
    }
    
    public int EncontrarUsuarios(String nombreUsuario) {
        BD bd = new BD();
        PreparedStatement sql;
        ResultSet res;
        int userId = -1; // Valor predeterminado si las credenciales son incorrectas
        try {
            sql = bd.getCon().prepareStatement("SELECT UsuarioId FROM usuarios WHERE NombreUsuario = ?");
            sql.setString(1, nombreUsuario);
            res = sql.executeQuery();
            
            if (res.next()) {
                userId = res.getInt("UsuarioId");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bd.closeConnection();
        }
        return userId;
    }
    
    public String RetornarUsername(int UsuarioId) {
        BD bd = new BD();
        PreparedStatement sql;
        ResultSet res;
        String NombreUsuario = "";
        try {
            sql = bd.getCon().prepareStatement("SELECT NombreUsuario FROM usuarios WHERE UsuarioId = ?");
            sql.setInt(1, UsuarioId);
            res = sql.executeQuery();
            
            if (res.next()) {
                NombreUsuario = res.getString("NombreUsuario");
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            bd.closeConnection();
        }
        return NombreUsuario;
    }
    
    public boolean ChangeStatus(int UserId, boolean status)
    {
        BD bd = new BD();
        PreparedStatement sql;
        try {
            sql = bd.getCon().prepareStatement("UPDATE usuarios SET StatusConexion = ? WHERE UsuarioId = ?");
            if (status)
            {
                sql.setInt(1, 1);
            }
            else
            {
                sql.setInt(1, 0);
            }   
            
            sql.setInt(2, UserId);
            int comprobar = sql.executeUpdate();
            
            bd.closeConnection();
           
            return comprobar > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            return false; 
        } finally {
            bd.closeConnection();
        }
    }
    
    public boolean cerrarSesion(int UserId){
        BD bd = new BD();
        PreparedStatement sql;
        try{
            sql = bd.getCon().prepareStatement("UPDATE usuarios SET StatusConexion = ? WHERE UsuarioId = ?"); 
            sql.setInt(1, 0);
            sql.setInt(2, UserId);

            int comprobar = sql.executeUpdate();

            return comprobar > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            bd.closeConnection();
        }
    }
}