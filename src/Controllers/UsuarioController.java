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
import models.Usuarios;
import bd.BD;


/**
 *
 * @author david
 */
public class UsuarioController {
    //Validar inicio de sesion
    public boolean verificarCredenciales(String nombreUsuario, String contraseña) {
        BD bd = new BD();
        PreparedStatement sql;
        ResultSet res;
        try {
            sql = bd.getCon().prepareStatement("SELECT * FROM usuarios WHERE NombreUsuario = ? AND Pass = ?");
            sql.setString(1, nombreUsuario);
            sql.setString(2, contraseña);
            res = sql.executeQuery();
            
            
            return res.next();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            return false; 
        } finally {
            
            bd.closeConnection();
        }
    }
    //Insertar un usuario
    public boolean insertarUsuario(String nombreUsuario, String contraseña, String cancionFavorita) {
        BD bd = new BD();
        PreparedStatement sql;
        try {
            sql = bd.getCon().prepareStatement("INSERT INTO usuarios (NombreUsuario, Pass, RespuestaPreguntaConfianza) VALUES (?, ?, ?)");
            sql.setString(1, nombreUsuario);
            sql.setString(2, contraseña);
            sql.setString(3, cancionFavorita);
            int comprobar = sql.executeUpdate();
            
            bd.closeConnection();
           
            return comprobar > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            return false; 
        }
    }
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
                x.nombreUsuario = r.getString("NombreUsuariAo");
                x.usuarioId = r.getInt("UsuarioId");
                usuarios.add(x);
            }
          
            return usuarios;
        } catch (SQLException ex) {
            Logger.getLogger(AmigosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
