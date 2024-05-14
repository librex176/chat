/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import bd.BD;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

// del 46 al 60 en queries
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
    
    public int InsertarGrupo(int UsuarioDuenoId, String Nombre) {
        try {
            var sql = getCon().prepareStatement("INSERT INTO grupos (UsuarioDuenoId, Nombre) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            sql.setInt(1, UsuarioDuenoId);
            sql.setString(2, Nombre);
            int affectedRows = sql.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("No se pudo insertar el grupo.");
            }

            try (var generatedKeys = sql.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("No se pudo obtener el GrupoId generado.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            return -1; // Devuelve un valor que indique error
        }
    }
    
    public boolean enviarSolicitudGrupos(int GrupoId,int UsuarioRecibeId, int Status) {
        BD bd = new BD();
        PreparedStatement sql;
        try {
            sql = bd.getCon().prepareStatement("INSERT INTO invitacionesgrupos (GrupoId, UsuarioRecibeId, Status) VALUES (?, ?, ?)");
            sql.setInt(1, GrupoId);
            sql.setInt(2, UsuarioRecibeId);
            sql.setInt(3, Status);
            int comprobar = sql.executeUpdate();
                      
            return comprobar > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            return false; 
        }
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

    public ArrayList<String> obtenerSolicitudesGrupos(int UsuarioRecibeId) {
        ArrayList<String> solicitudes = new ArrayList<>();
        try {
            var sql = getCon().prepareStatement("SELECT ig.InvitacionId, g.Nombre, g.UsuarioDuenoId FROM invitacionesgrupos ig INNER JOIN grupos g ON ig.GrupoId = g.GrupoId WHERE ig.UsuarioRecibeId = ? AND Status = ?");
            sql.setInt(1, UsuarioRecibeId);
            sql.setInt(2, 1);
            var rs = sql.executeQuery();
            while (rs.next()) {
                var invitacionId = rs.getInt("InvitacionId");
                var nombreGrupo = rs.getString("Nombre");
                var usuarioDuenoId = rs.getInt("UsuarioDuenoId");
                solicitudes.add(String.valueOf(invitacionId));
                solicitudes.add(nombreGrupo);
                solicitudes.add(String.valueOf(usuarioDuenoId));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // closeConnection();
        }
        return solicitudes;
    }

    
    public boolean AceptarSolicitudAmigos(int InvitacionId) {
        try {
            String query = "INSERT INTO listaamigos (UsuarioDuenoId, UsuarioId) " +
                            "SELECT ia.UsuarioEnviaId, ia.UsuarioRecibeId " +
                            "FROM invitacionesamigos ia " +
                            "WHERE ia.InvitacionId = ?";
            PreparedStatement sql = getCon().prepareStatement(query);
            sql.setInt(1, InvitacionId);
            int comprobar = sql.executeUpdate();

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
    
    public boolean actualizarEstadoSolicitudGrupo(int invitacionId, int Status) {
        try {
            String query = "UPDATE invitacionesgrupos SET Status = ? WHERE InvitacionId = ?";
            PreparedStatement sql = getCon().prepareStatement(query);
            sql.setInt(1, Status);
            sql.setInt(2, invitacionId);
            int comprobar = sql.executeUpdate();

            return comprobar > 0;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
