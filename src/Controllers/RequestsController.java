/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import bd.BD;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

// del 46 al 60 en queries
public class RequestsController extends BD{
    String ip;

    public RequestsController() {
    }

    public RequestsController(String ip) {
        this.ip = ip;
    }
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
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        ArrayList<String> solicitudes = new ArrayList<>();
        try {
            socket = new Socket(ip, 1234);
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            String sql;
            sql = "46:" + UsuarioRecibeId ;
            out.writeBytes(sql + "\n");
            out.flush();
           
            String resultado;
            resultado = in.readLine();
            String[] invitacionesStr = resultado.split(";");
            for(String invitacion: invitacionesStr)
            {
                String[] invitacionInfo = invitacion.split(":");
                if(invitacionInfo.length>0)
                {
                    solicitudes.add(invitacionInfo[0]); // invitacionId
                    solicitudes.add(invitacionInfo[1]); // nombre 
                    solicitudes.add(invitacionInfo[2]); //usuarioDueñoId
                }
            }
        } catch (IOException e) {
            System.out.println("Error obteniendo solicitudes de grupos: " + e.getMessage());
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
