/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;

/**
 *
 * @author Valeria
 */
public class ListaGrupos extends JFrame {
    int userId;
    public ListaGrupos(int userId)
    {
        super();
        this.userId = userId;
        init();
        addWindowListener();
    }
     
    private void init()
    {
        
    }
    private void addWindowListener() {
        // Crear una instancia del WindowListener
        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Aquí puedes redirigir al usuario a la ventana ListasMenu y pasar el parámetro userId
                ListasMenu listasMenu = new ListasMenu(userId);
                listasMenu.setVisible(true);
            }
        };

        this.addWindowListener(windowListener);
    }
}
