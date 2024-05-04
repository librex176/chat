/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import bd.BD;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Samantha
 */
public class RequestsController extends BD{
    //Insertar un usuario
    public boolean enviarSolicitudAmigos(int UsuarioEnviaId, int UsuarioRecibeId) {
        BD bd = new BD();
        PreparedStatement sql;
        try {
            sql = bd.getCon().prepareStatement("INSERT INTO invitacionesamigos (UsuarioEnviaId, UsuarioRecibeId) VALUES (?, ?)");
            sql.setInt(1, UsuarioEnviaId);
            sql.setInt(2, UsuarioRecibeId);
            int comprobar = sql.executeUpdate();
            
            bd.closeConnection();
           
            return comprobar > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            return false; 
        }
    }
    
    public boolean enviarSolicitudGrupos(int GrupoId, int UsuarioEnviaId, int UsuarioRecibeId) {
        BD bd = new BD();
        PreparedStatement sql;
        try {
            sql = bd.getCon().prepareStatement("INSERT INTO invitacionesgrupos (GrupoId, UsuarioEnviaId, UsuarioRecibeId) VALUES (?, ?, ?)");
            sql.setInt(1, GrupoId);
            sql.setInt(2, UsuarioEnviaId);
            sql.setInt(3, UsuarioRecibeId);
            int comprobar = sql.executeUpdate();
            
            bd.closeConnection();
           
            return comprobar > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            return false; 
        }
    }
    
    public int obtenerMaxGroupId() {
        BD bd = new BD();
        PreparedStatement sql;
        int maxGroupId = 0;
        try {
            sql = bd.getCon().prepareStatement("SELECT COALESCE(MAX(GrupoId), 0) AS MaxGroupId FROM invitacionesgrupos");
            var rs = sql.executeQuery();
            if (rs.next()) {
                maxGroupId = rs.getInt("MaxGroupId") + 1;
            }
            bd.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return maxGroupId;
    }
    
    public ArrayList<Integer> obtenerSolicitudesAmigos(int userId) {
        ArrayList<Integer> solicitudes = new ArrayList<>();
        PreparedStatement sql;
        try {
            sql = getCon().prepareStatement("SELECT UsuarioEnviaId, InvitacionId FROM invitacionesamigos WHERE UsuarioRecibeId = ?");
            sql.setInt(1, userId);
            var rs = sql.executeQuery();
            while (rs.next()) {
                int usuarioEnviaId = rs.getInt("UsuarioEnviaId");
                int invitacionId = rs.getInt("InvitacionId");
                solicitudes.add(usuarioEnviaId);
                solicitudes.add(invitacionId);
            }
            
            obtenerSolicitudesGrupos(userId);
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return solicitudes;
    }

    public ArrayList<Integer> obtenerSolicitudesGrupos(int userId) {
        ArrayList<Integer> solicitudes = new ArrayList<>();
        PreparedStatement sql;
        try {
            sql = getCon().prepareStatement("SELECT UsuarioEnviaId, InvitacionId FROM invitacionesgrupos WHERE UsuarioRecibeId = ?");
            sql.setInt(1, userId);
            var rs = sql.executeQuery();
            while (rs.next()) {
                int usuarioEnviaId = rs.getInt("UsuarioEnviaId");
                int invitacionId = rs.getInt("InvitacionId");
                solicitudes.add(usuarioEnviaId);
                solicitudes.add(invitacionId);
            }
            // No cierres la conexión aquí
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Cierra la conexión aquí después de haber obtenido todas las solicitudes
        //closeConnection();
        return solicitudes;
    }
    
    public boolean AceptarSolicitudAmigos(int UsuarioDuenoId, int UsuarioId) {
        try {
            PreparedStatement sql = getCon().prepareStatement("INSERT INTO listaamigos (UsuarioDuenoId, UsuarioId) VALUES (?, ?)");
            sql.setInt(1, UsuarioDuenoId);
            sql.setInt(2, UsuarioId);
            int comprobar = sql.executeUpdate();
            //closeConnection();
            return comprobar > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean EliminarSolicitudAmigos(int InvitacionId) {
        try {
            PreparedStatement sql = getCon().prepareStatement("DELETE FROM invitacionesamigos WHERE InvitacionId = ?");
            sql.setInt(1, InvitacionId);
            int comprobar = sql.executeUpdate();
            //closeConnection();
            return comprobar > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
