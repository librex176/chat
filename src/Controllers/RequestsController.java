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
import java.util.ArrayList;

// del 46 al 60 en queries
public class RequestsController{
    String ip;

    public RequestsController(String ip) {
        this.ip = ip;
    }
    //Insertar un usuario
    public boolean enviarSolicitudAmigos(int UsuarioEnviaId, int UsuarioRecibeId) {
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
            sql = "52:" + UsuarioEnviaId + ":" + UsuarioRecibeId ;
            out.writeBytes(sql + "\n");
            out.flush();
            
            // recibir el resultado de la consulta del server
            String resultado = in.readLine();
            System.out.println(resultado + " de solicitud de amistad");
            
            return resultado.equals("true"); // retornar de acuerdo a la consulta al servidor
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;  
        }       
    }
    
    public int InsertarGrupo(int UsuarioDuenoId, String Nombre){
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
            sql = "53:" + UsuarioDuenoId + ":" + Nombre ;
            out.writeBytes(sql + "\n");
            out.flush();
          
            // recibir el resultado de la consulta del server
            String resultado = in.readLine();
            int id = Integer.parseInt(resultado);
            //System.out.println(resultado);
            
            return id;
        } catch (IOException e) {
            e.printStackTrace();
            return -1; // Devuelve un valor que indique error
        }
    }
    
    public boolean enviarSolicitudGrupos(int GrupoId,int UsuarioRecibeId, int Status) {
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
            sql = "54:" + GrupoId + ":" + UsuarioRecibeId + ":" + Status;
            out.writeBytes(sql + "\n");
            out.flush();
            
            // recibir el resultado de la consulta del server
            String resultado = in.readLine();
            //System.out.println(resultado);
            
            return resultado.equals("true");
        } catch (IOException ex) {
            ex.printStackTrace();
            return false; 
        }
    }
       
    public ArrayList<Integer> obtenerSolicitudesAmigos(int UsuarioRecibeId) {
        ArrayList<Integer> solicitudes = new ArrayList<>();
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
            sql = "55:" + UsuarioRecibeId;
            out.writeBytes(sql + "\n");
            out.flush();
            
            String resultado;
            resultado = in.readLine();
            if(resultado != null){
                String[] solicitudesstr = resultado.split(";");
                for(String req: solicitudesstr)
                {
                    String[] invitacionInfo = req.split(":");
                    if(invitacionInfo.length > 0)
                    {
                        solicitudes.add(Integer.parseInt(invitacionInfo[0])); // UsuarioEnviaId
                        solicitudes.add(Integer.parseInt(invitacionInfo[1])); // InvitacionId 
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
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
            if(resultado != null){
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
            }
        } catch (IOException e) {
            System.out.println("Error obteniendo solicitudes de grupos: " + e.getMessage());
        }
        return solicitudes;
    }

    public boolean AceptarSolicitudAmigos(int InvitacionId) {
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
            sql = "57:" + InvitacionId;
            out.writeBytes(sql + "\n");
            out.flush();
            
            // recibir el resultado de la consulta del server
            String resultado = in.readLine();
            //System.out.println(resultado);
            
            return resultado.equals("true");
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean EliminarSolicitudAmigos(int InvitacionId) {
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
            sql = "58:" + InvitacionId;
            out.writeBytes(sql + "\n");
            out.flush();
            
            // recibir el resultado de la consulta del server
            String resultado = in.readLine();
            //System.out.println(resultado);
            
            return resultado.equals("true");
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
    public boolean actualizarEstadoSolicitudGrupo(int invitacionId, int Status) {
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
            sql = "59:" + Status + ":" + invitacionId;
            out.writeBytes(sql + "\n");
            out.flush();
            
            // recibir el resultado de la consulta del server
            String resultado = in.readLine();
            //System.out.println(resultado);
            
            return resultado.equals("true");
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
