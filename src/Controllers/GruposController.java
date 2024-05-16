/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

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

public class GruposController{
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
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try {
            socket = new Socket(ip, 1234);
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String sql = "87:" + grupoId ;
            out.writeBytes(sql + "\n");
            out.flush();
            String resultado = in.readLine();
            //System.out.println(resultado);
            return resultado ;
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return "0";
    }
    
    public int selectCuentaParticipantes(int grupoId) 
    {
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try {
            socket = new Socket(ip, 1234);
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String sql = "80:" + grupoId ;
            out.writeBytes(sql + "\n");
            out.flush();
            int resultado = in.read();
            //System.out.println(resultado);
            resultado =  resultado > 0 ? resultado : 0;
            return resultado ;
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }
    
    public int selectGrupoIdInvitaciones(int invitacionId)
    {
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try {
            socket = new Socket(ip, 1234);
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String sql= "79:" + invitacionId ;
            out.writeBytes(sql + "\n");
            out.flush();
            int resultado = in.read();
            //System.out.println(resultado);
            resultado =  resultado > 0 ? resultado : 0;
            return resultado ; 
        } catch (IOException e) {
            System.out.println("Error: "+ e.getMessage());
        }
        return 0;
    }
    
    public boolean deleteMensajesGrupos(int grupoId)
    {
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try {
            socket = new Socket(ip, 1234); // Usa la IP de tu servidor
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String sql = "81:" + grupoId;
            out.writeBytes(sql + "\n");
            out.flush();
            int resultado = in.read();
            return resultado > 0; 
        } catch (IOException e) {
            System.out.println("Error: "+ e.getMessage());
        }
        return false;
    }
    
    public boolean deleteInvitacionesGrupos(int grupoId)
    {
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try {
            socket = new Socket(ip, 1234); // Usa la IP de tu servidor
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String sql = "82:" + grupoId;
            out.writeBytes(sql + "\n");
            out.flush();
            int resultado = in.read();
            return resultado > 0; 
        } catch (IOException e) {
            System.out.println("Error: "+ e.getMessage());
        }
        return false;
    }
    
    public boolean deleteGrupo(int grupoId)
    {
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try {
            socket = new Socket(ip, 1234); 
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String sql = "83:" + grupoId;
            out.writeBytes(sql + "\n");
            out.flush();
            int resultado = in.read();
            return resultado > 0; 
        } catch (IOException e) {
            System.out.println("Error: "+ e.getMessage());
        }
        return false;
    }
    
    public boolean selectDuenoId(int grupoId, int usuarioId)
    {
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try {
            socket = new Socket(ip, 1234);
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String sql= "84:" + grupoId + ":" + usuarioId ;
            out.writeBytes(sql + "\n");
            out.flush();
            String resultado = in.readLine();
            boolean esVerdad = resultado.equals("true");
            return esVerdad ; 
        } catch (IOException e) {
            System.out.println("Error: "+ e.getMessage());
        }
        return false;
    }
    
    public ArrayList<String[]> selectMiembrosGrupos(int grupoId, int status)
    {
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try {
            socket = new Socket(ip, 1234);
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String sql= "85:" + grupoId + ":" + status;
            out.writeBytes(sql + "\n");
            out.flush();
            // recibir el resultado de la consulta del server
            ArrayList<String[]> miembros = new ArrayList<>();
            String resultado;
            resultado = in.readLine();
            if(resultado!=null)
            {
                String[] parts = resultado.split(";");
                for(String p : parts)
                {
                    String[] ob = p.split(":");
                    miembros.add(ob);
                }
                if(!miembros.isEmpty()){
                    return miembros;
                }
            }
        } catch (IOException e) {
            System.out.println("Error: "+ e.getMessage());
        }
        return null;
    }
    
    public boolean deleteUsuarioRecibeId(int grupoId, int usuarioId)
    {
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try {
            socket = new Socket(ip, 1234); // Usa la IP de tu servidor
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String sql = "86:" + grupoId + ":" + usuarioId;
            out.writeBytes(sql + "\n");
            out.flush();
            String resultado = in.readLine();
            //System.out.println("Resultado: "+ resultado);
            return resultado.equals("true"); 
        } catch (IOException e) {
            System.out.println("Error: "+ e.getMessage());
        }
        return false;
    }
    
    public List<Integer> selectMiembrosGruposInvitaciones(int grupoId)
    {
        Socket socket;
        DataOutputStream out;
        BufferedReader in;
        try {
            socket = new Socket(ip, 1234);
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String sql= "88:" + grupoId ;
            out.writeBytes(sql + "\n");
            out.flush();
            // recibir el resultado de la consulta del server
            List<Integer> miembros = new ArrayList<>();
            String resultado;
            resultado = in.readLine();
            if(resultado!=null)
            {
                System.out.println("resultado: " + resultado);
                String[] parts = resultado.split(":");
                for(String part : parts)
                {
                    //System.out.println("part " + part);
                    miembros.add(Integer.parseInt(part));
                }
                if(!miembros.isEmpty()){
                    return miembros;
                }
            } else
            {
                System.out.println("resultado es null");
            }
        } catch (IOException e) {
            System.out.println("Error: "+ e.getMessage());
        }
        return null;
    }
    
}
