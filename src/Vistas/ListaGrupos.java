/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Valeria
 */
public class ListaGrupos extends JFrame {
    JButton enviarSolicitud;
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
        setTitle("Lista de mis grupos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        
        // crear groupLayout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        // establecer el auto ajuste de gaps
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        
        
        enviarSolicitud = new JButton("Crear Grupo");
        
        // Crear un contenedor para el JList
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(enviarSolicitud, 20, 200, 400)
        );
        
        // configurar el diseño vertical
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(enviarSolicitud)
        );
        
        
        enviarSolicitud.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirigir a SendRequestForm y pasar userId
                CreateGroups createGroups = new CreateGroups(userId);
                createGroups.setVisible(true);
                dispose();
            }
        });
        
        pack(); // ajustar el tamaño de la ventana según el contenido
        setLocationRelativeTo(null); // centrar la ventana en la pantalla
        
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
