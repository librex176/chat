/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 *
 * @author Samantha
 */
public class RequestsMenu extends JFrame{
    JButton btnSolicitudesAmigos, btnSolicitudesGrupos;
    int userId;
    String ip;
    
    public RequestsMenu(int userId, String ip) {
        super();
        this.userId  = userId;
        this.ip = ip;
        initComponents();
        addWindowListener();
    }

    private void initComponents() {
        setTitle("Lista de solicitudes de amigos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        
        // Crear el layout principal
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        
         btnSolicitudesAmigos = new JButton("Ver Solicitudes de Amigos");
         btnSolicitudesGrupos = new JButton("Ver Solicitudes de Grupos");
        
        // Agregar el panel de amigos al layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(btnSolicitudesAmigos, 20, 200, 400)
                .addComponent(btnSolicitudesGrupos, 20, 200, 400)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(btnSolicitudesAmigos)
                .addComponent(btnSolicitudesGrupos)
        );

       
        btnSolicitudesAmigos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FriendsRequests requestsFriends = new FriendsRequests(userId, ip);
                requestsFriends.setVisible(true);
                dispose();
            }
        });

        
        btnSolicitudesGrupos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GroupRequests groupRequests = new GroupRequests(userId, ip);
                groupRequests.setVisible(true);
                dispose();
            }
        });

        add(btnSolicitudesAmigos);
        add(btnSolicitudesGrupos);

        pack();
        setLocationRelativeTo(null);
    }
    
    private void addWindowListener() {
        // Crear una instancia del WindowListener
        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Aquí puedes redirigir al usuario a la ventana ListasMenu y pasar el parámetro userId
                ListasMenu listasMenu = new ListasMenu(userId, ip);
                listasMenu.setVisible(true);
            }
        };

        this.addWindowListener(windowListener);
    }
}
