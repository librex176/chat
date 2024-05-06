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
import bd.BD;

/**
 *
 * @author Valeria
 */
public class AmigosController extends BD{
        
    public String selectNameByUserId(int usuarioId)
    {
        BD bd = new BD();
        PreparedStatement sql;
        String nombre = null;
        ResultSet r;
        try
        {
            sql = bd.getCon().prepareStatement("SELECT NombreUsuario FROM usuarios WHERE UsuarioId=?");
            
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
    
    public boolean SearchFriends(int usuarioId1, int usuarioId2)
    {
        BD bd = new BD();
        PreparedStatement sql;
        ResultSet res;
        
        try
        {
            sql = bd.getCon().prepareStatement("SELECT amigosId FROM listaamigos WHERE UsuarioDuenoId =? AND UsuarioId = ?");
            sql.setInt(1, usuarioId1);
            sql.setInt(2, usuarioId2);
            res = sql.executeQuery();
            
            if(res.next())
            {
                bd.closeConnection();
                return true;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AmigosController.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            bd.closeConnection();
        }
        return false;
    }
}
