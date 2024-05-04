/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Valeria
 */
public class ListasMenu extends JFrame{
    JButton amigos, conectados, grupos, verSolicitudes;
    int userId;
    
    public ListasMenu() {
        super();
        init();
    }
    
    public ListasMenu(int userId) {
        super();
        this.userId  = userId;
        init();
    }
    
    private void init() {
        setTitle("Mis listas");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        
// crear groupLayout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        // establecer el auto ajuste de gaps
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        
// inicializar elementos
        // label de amigos
        amigos = new JButton("Mis Amigos");
        conectados = new JButton("Ver todos");
        grupos = new JButton("Mis grupos");
        verSolicitudes = new JButton("Ver Solicitudes");
        
// group layouts
        
        // configurar el diseño horizontal
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(amigos, 20, 200, 400)
                .addComponent(conectados, 20, 200, 400)
                .addComponent(grupos, 20, 200, 400)
                .addComponent(verSolicitudes, 20, 200, 400)
        );
        
        // configurar el diseño vertical
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(amigos)
                .addComponent(conectados)
                .addComponent(grupos)
                .addComponent(verSolicitudes)
        );
        
// eventos
        
        // agregar ActionListener al botón "Aceptar"
        amigos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // mandar el userId que está iniciado sesion
                ListaAmigos listAmigosView = new ListaAmigos(userId);
                listAmigosView.setVisible(true);
                dispose();
            }
        });
        
        // agregar ActionListener al botón "Aceptar"
        conectados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // mandar el userId que está iniciado sesion
                ListaConectados listAmigosView = new ListaConectados(userId);
                listAmigosView.setVisible(true);
                dispose();
            }
        });
        
        // agregar ActionListener al botón "Aceptar"
        grupos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // mandar el userId que está iniciado sesion
                ListaGrupos listAmigosView = new ListaGrupos(userId);
                listAmigosView.setVisible(true);
                dispose();
            }
        });
        
        verSolicitudes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // mandar el userId que está iniciado sesion
                RequestsMenu View = new RequestsMenu(userId);
                View.setVisible(true);
                dispose();
            }
        });
        
        pack(); // ajustar el tamaño de la ventana según el contenido
        setLocationRelativeTo(null); // centrar la ventana en la pantalla
    
    }
}
