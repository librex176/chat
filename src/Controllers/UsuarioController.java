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
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class UsuarioController {
    String ip;

    public UsuarioController(String ip) {
        this.ip = ip;
    }
        public String actualizarPass(int userId) {
    // Conexion al servidor, el servidor se encargara de realizar la consulta a la base de datos
    Socket socket;
    DataOutputStream out;
    BufferedReader in;
    try {
        socket = new Socket(ip, 1234); // Usa la IP de tu servidor
        out = new DataOutputStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        // Consulta al servidor con los datos requeridos
        String sql;
        // Se envia un string con el numero de la query a ejecutar en el servidor y los datos 
        // necesarios para la ejecucion de la query
        sql = "9:" + userId ;
        System.out.println(sql);
        out.writeBytes(sql + "\n");
        out.flush();
        
        // Recibir el resultado de la consulta del servidor
        String resultado = "0";
         resultado = in.readLine();
         System.out.println(resultado);
         return resultado;
        
        
        
        
        
        
    } catch (IOException e) {
        e.printStackTrace();
        System.out.println(e.getMessage());
    }
    return "aqui esta el error";
}
    public String verPass(int userId) {
    // Conexion al servidor, el servidor se encargara de realizar la consulta a la base de datos
    Socket socket;
    DataOutputStream out;
    BufferedReader in;
    try {
        socket = new Socket(ip, 1234); // Usa la IP de tu servidor
        out = new DataOutputStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        // Consulta al servidor con los datos requeridos
        String sql;
        // Se envia un string con el numero de la query a ejecutar en el servidor y los datos 
        // necesarios para la ejecucion de la query
        sql = "8:" + userId;
        System.out.println(sql);
        out.writeBytes(sql + "\n");
        out.flush();
        
        // Recibir el resultado de la consulta del servidor
        String resultado = "0";
         resultado = in.readLine();
         System.out.println(resultado);
         return resultado;
        
        
        
        
        
        
    } catch (IOException e) {
        e.printStackTrace();
        System.out.println(e.getMessage());
    }
    return "aqui esta el error";
}
    public int verificarPregunta(String nombreUsuario, String pregunta, String ip) {
    // Conexion al servidor, el servidor se encargara de realizar la consulta a la base de datos
    Socket socket;
    DataOutputStream out;
    BufferedReader in;
    try {
        socket = new Socket(ip, 1234); // Usa la IP de tu servidor
        out = new DataOutputStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        
        // Consulta al servidor con los datos requeridos
        String sql;
        // Se envia un string con el numero de la query a ejecutar en el servidor y los datos 
        // necesarios para la ejecucion de la query
        sql = "7:" + nombreUsuario + ":" + pregunta;
        System.out.println(sql);
        out.writeBytes(sql + "\n");
        out.flush();
        
        // Recibir el resultado de la consulta del servidor
        String resultado = "0";
         resultado = in.readLine();
        System.out.println(resultado);
        if(resultado.equals("0")){
            return 0;
        }else{
            return Integer.parseInt(resultado);
        }
        
        
        
        
    } catch (IOException e) {
        e.printStackTrace();
    }
    return 0;
}

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
            sql = "6:" + nombreUsuario + ":" + contraseña + ":" + cancionFavorita;
            System.out.println(sql);
            out.writeBytes(sql + "\n");
            out.flush();
            
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
    public int EncontrarUsuarios(String nombreUsuario) {
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
            sql = "75:" + nombreUsuario;
            out.writeBytes(sql + "\n");
            out.flush();
          
            // recibir el resultado de la consulta del server
            String resultado = in.readLine();
            int id = Integer.parseInt(resultado);
            System.out.println(id);
            return id;
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // Devuelve un valor que indique error
        }
    }
    
    public boolean ChangeStatus(int UserId,String ip)
    {
        try {
            Socket socket;
            DataOutputStream out;
            BufferedReader in;
            socket = new Socket(ip, 1234); // Usa la IP de tu servidor
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String sql = "99:" + UserId;
            System.out.println("Que se envia del cliente "+ sql);
            out.writeBytes(sql + "\n");
            out.flush();
            String resultado = in.readLine();
            System.out.println(resultado);
            //sql = bd.getCon().prepareStatement("UPDATE usuarios SET StatusConexion = ? WHERE UsuarioId = ?");
            return resultado.equals("true");
        } catch (IOException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public boolean cerrarSesion(int UserId,String ip){
        //BD bd = new BD();
        //PreparedStatement sql;
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try{
            socket = new Socket(ip, 1234); // Usa la IP de tu servidor
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String sql;
            //sql = bd.getCon().prepareStatement("UPDATE usuarios SET StatusConexion = ? WHERE UsuarioId = ?"); 
            //sql.setInt(1, 0);
            //sql.setInt(2, UserId);
            sql = "100:"+UserId;
            out.writeBytes(sql + "\n");
            out.flush();
            String resultado = in.readLine();
            
            //int comprobar = sql.executeUpdate();
            //return comprobar > 0;
            return resultado.equals("true");    
        } catch (IOException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}