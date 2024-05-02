/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bd;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Amigos;

/**
 *
 * @author Valeria
 */
public class AmigosController extends BD{
        
    public String selectNameByUserId(int usuarioId)
    {
        
        PreparedStatement sql;
        String nombre = null;
        ResultSet r;
        try
        {
            sql = getCon().prepareStatement("SELECT NombreUsuario FROM usuarios WHERE UsuarioId=?");
            
            sql.setInt(1,usuarioId);
            r = sql.executeQuery();
            while(r.next())
            {
                nombre = r.getString("NombreUsuario");
            }
            
            return nombre;
        } catch (SQLException ex) {
            Logger.getLogger(AmigosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public ArrayList<Amigos> selectMisAmigosUsuarios(int usuarioId)
    {
        PreparedStatement sql;
        ArrayList<Amigos> amigos;
        ResultSet r;
        try
        {
            sql = getCon().prepareStatement("SELECT u.NombreUsuario, a.UsuarioId FROM usuarios u INNER JOIN listaamigos a ON u.UsuarioId=a.UsuarioId WHERE a.UsuarioDuenoId=?");
            
            sql.setInt(1,usuarioId);
            amigos = new ArrayList<Amigos>();
            r = sql.executeQuery();
            while(r.next())
            {
                Amigos x = new Amigos();
                x.nombreUsuario = r.getString("NombreUsuario");
                x.usuarioId = r.getInt("UsuarioId");
                amigos.add(x);
            }
            
            amigos = selectMisAmigosDuenos(amigos, usuarioId);
            
            return amigos;
        } catch (SQLException ex) {
            Logger.getLogger(AmigosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public ArrayList<Amigos> selectMisAmigosDuenos(ArrayList<Amigos> amigos, int usuarioId)
    {
        PreparedStatement sql;
        ResultSet r;
        try
        {
            sql = getCon().prepareStatement("SELECT u.NombreUsuario, a.UsuarioDuenoId FROM usuarios u INNER JOIN listaamigos a ON u.UsuarioId=a.UsuarioDuenoId WHERE a.UsuarioId=?");
            
            sql.setInt(1,usuarioId);
            r = sql.executeQuery();
            while(r.next())
            {
                Amigos x = new Amigos();
                x.nombreUsuario = r.getString("NombreUsuario");
                x.usuarioId = r.getInt("UsuarioDuenoId");
                amigos.add(x);
            }
            
            return amigos;
        } catch (SQLException ex) {
            Logger.getLogger(AmigosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
