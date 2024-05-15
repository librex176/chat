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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Grupos;

public class GruposController extends BD{
    String ip;
    public GruposController(String ip) {
        this.ip = ip;
    }
    
     public String selectNameByUserId(int usuarioId)
    {
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
            sql = "76:" + usuarioId ;
            out.writeBytes(sql + "\n");
            out.flush();
            
            // recibir el resultado de la consulta del server
            String resultado = "0";
            resultado = in.readLine();
            
            //System.out.println(resultado);
            
            return resultado; // retornar de acuerdo a la consulta al servidor
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "0";
    }
    
    public ArrayList<Grupos> selectMisGrupos(int usuarioId)
    {
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try {
            socket = new Socket(ip, 1234);
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            String sql;
            sql = "77:" + usuarioId ;
            out.writeBytes(sql + "\n");
            out.flush();
            
            // recibir el resultado de la consulta del server
            ArrayList<Grupos> grupos = new ArrayList<>();
            String resultado;
            resultado = in.readLine();
            if(resultado!=null)
            {
                String[] gruposStr = resultado.split(";");
                for(String grupoStr: gruposStr)
                {
                    Grupos grupo = new Grupos();
                    String[] grupoInfoStr = grupoStr.split(":");
                    grupo.nombre = grupoInfoStr[0];
                    grupo.grupoId = Integer.parseInt(grupoInfoStr[1]);
                    grupos.add(grupo);
                }
                return grupos;
            }
            
        } catch (IOException e) {
            System.out.println("error:"+ e.getMessage());
        }
        return null;
    }
    
    public String selectNombreGrupo(int grupoId)
    {
        PreparedStatement sql;
        String nombreGrupo=null;
        ResultSet r;
        try
        {
            sql = getCon().prepareStatement("SELECT nombre FROM grupos WHERE grupoId=?");
            
            sql.setInt(1,grupoId);
            r = sql.executeQuery();
            while(r.next())
            {
                nombreGrupo = r.getString("nombre");
            }
            
            return nombreGrupo;
        } catch (SQLException ex) {
            Logger.getLogger(AmigosController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public int selectCuentaParticipantes(int grupoId) 
    {
        BD bd = new BD();
        PreparedStatement sql;
        ResultSet r;
        int cantidadInvitaciones = -1;
        try {
            sql = bd.getCon().prepareStatement("SELECT count(*) as cant from invitacionesgrupos where grupoId=? AND status<=2");
            sql.setInt(1,grupoId);
            r = sql.executeQuery();
            while(r.next())
            {
                cantidadInvitaciones = r.getInt("cant");
            }
            return cantidadInvitaciones;
        } catch (SQLException ex) {
            Logger.getLogger(GruposController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cantidadInvitaciones;
    }
    
    public int selectGrupoIdInvitaciones(int invitacionId)
    {
        BD bd = new BD();
        PreparedStatement sql;
        ResultSet r;
        int grupoId = -1;
        try {
            sql = bd.getCon().prepareStatement("Select grupoId from invitacionesgrupos where invitacionId=?");
            sql.setInt(1,invitacionId);
            r = sql.executeQuery();
            while(r.next())
            {
                grupoId = r.getInt("grupoId");
            }
            return grupoId;
        } catch (SQLException ex) {
            Logger.getLogger(GruposController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grupoId;
    }
    
    public boolean deleteMensajesGrupos(int grupoId)
    {
        BD bd = new BD();
        PreparedStatement sql;
        ResultSet r;
        int comprobar = -1;
        try {
            sql = bd.getCon().prepareStatement("DELETE FROM mensajes_grupales where grupoId=?");
            sql.setInt(1, grupoId);
            comprobar = sql.executeUpdate();
            return comprobar > -1;
        } catch (SQLException ex) {
            Logger.getLogger(GruposController.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            bd.closeConnection();
        }
        return comprobar>-1;
    }
    
    public boolean deleteInvitacionesGrupos(int grupoId)
    {
        BD bd = new BD();
        PreparedStatement sql;
        ResultSet r;
        int comprobar = 0;
        try {
            System.out.println("el grupoId es: "+ grupoId);
            sql = bd.getCon().prepareStatement("DELETE FROM invitacionesGrupos where grupoId=?");
            sql.setInt(1, grupoId);
            comprobar = sql.executeUpdate();
            return comprobar > 0;
        } catch (SQLException ex) {
            Logger.getLogger(GruposController.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            bd.closeConnection();
        }
        return comprobar>0;
    }
    
    public boolean deleteGrupo(int grupoId)
    {
        BD bd = new BD();
        PreparedStatement sql;
        ResultSet r;
        int comprobar = 0;
        try {
            sql = bd.getCon().prepareStatement("DELETE FROM grupos where grupoId=?");
            sql.setInt(1, grupoId);
            comprobar = sql.executeUpdate();
            return comprobar > 0;
        } catch (SQLException ex) {
            Logger.getLogger(GruposController.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            bd.closeConnection();
        }
        return comprobar>0;
    }
    
    public boolean selectDuenoId(int grupoId, int usuarioId)
    {
        BD bd = new BD();
        PreparedStatement sql;
        ResultSet r;
        int usuarioDuenoId = 0;
        try {
            sql = bd.getCon().prepareStatement("Select usuarioDuenoId from grupos where grupoId = ?");
            sql.setInt(1, grupoId);
            r = sql.executeQuery();
            while(r.next())
            {
                usuarioDuenoId = r.getInt("usuarioDuenoId");
            }
            return usuarioId==usuarioDuenoId;
        } catch (SQLException ex) {
            Logger.getLogger(GruposController.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            bd.closeConnection();
        }
        return usuarioId==usuarioDuenoId;
    }
    
    
    public boolean deleteUsuarioRecibeId(int grupoId, int usuarioId)
    {
        BD bd = new BD();
        PreparedStatement sql;
        int comprobar = 0;
        try {
            sql = bd.getCon().prepareStatement("DELETE FROM invitacionesGrupos where grupoId=? AND usuarioRecibeId=?");
            sql.setInt(1, grupoId);
            sql.setInt(2, usuarioId);
            comprobar = sql.executeUpdate();
            System.out.println("se borra un solo usuario: " + comprobar);
            return comprobar > 0;
        } catch (SQLException ex) {
            Logger.getLogger(GruposController.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            bd.closeConnection();
        }
        return comprobar>0;
    }
    
}
