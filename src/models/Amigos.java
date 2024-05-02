/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Valeria
 */
public class Amigos {
    public int usuarioDuenoId;
    public int usuarioId;
    public String nombreUsuario;
    public String nombreUsuarioDueno;

    public int getUsuarioDuenoId() {
        return usuarioDuenoId;
    }

    public void setUsuarioDuenoId(int usuarioDuenoId) {
        this.usuarioDuenoId = usuarioDuenoId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getNombreUsuarioDueno() {
        return nombreUsuarioDueno;
    }

    public void setNombreUsuarioDueno(String nombreUsuarioDueno) {
        this.nombreUsuarioDueno = nombreUsuarioDueno;
    }
    
}
