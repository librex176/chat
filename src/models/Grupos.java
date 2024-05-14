/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author aacar
 */
public class Grupos {
    public int grupoId;
    public int usuarioDuenoId;
    public String nombre;

    public Grupos() {
    }

    public int getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(int grupoId) {
        this.grupoId = grupoId;
    }

    public int getUsuarioDuenoId() {
        return usuarioDuenoId;
    }

    public void setUsuarioDuenoId(int usuarioDuenoId) {
        this.usuarioDuenoId = usuarioDuenoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
