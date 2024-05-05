/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;

import Controllers.RequestsController;
import Controllers.UsuarioController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Samantha
 */
public class FriendsRequests extends JFrame {
    private int userId;

    public FriendsRequests(int userId) {
        this.userId = userId;
        initComponents();
        addWindowListener();
    }

    private void initComponents() {
        RequestsController requestsController = new RequestsController();

        ArrayList<Integer> solicitudesAmigos = requestsController.obtenerSolicitudesAmigos(userId);

        setTitle("Lista de solicitudes de amigos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        
        // Crear el layout principal
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        // Panel para las solicitudes de amigos
        JPanel amigosPanel = new JPanel(new BorderLayout());
        amigosPanel.setBorder(BorderFactory.createTitledBorder("Solicitudes de Amigos"));
        DefaultListModel<String> amigosListModel = new DefaultListModel<>();
        JList<String> amigosList = new JList<>(amigosListModel);
        JScrollPane amigosScrollPane = new JScrollPane(amigosList);
        amigosPanel.add(amigosScrollPane, BorderLayout.CENTER);

        // Botones de aceptar y rechazar para las solicitudes de amigos
        JPanel amigosButtonPanel = new JPanel();
        amigosPanel.add(amigosButtonPanel, BorderLayout.SOUTH);

        // Mapa para vincular las solicitudes con sus botones correspondientes
        Map<Integer, JButton[]> buttonMap = new HashMap<>();

        // Agregar el panel de amigos al layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(amigosPanel)
                .addComponent(amigosButtonPanel)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(amigosPanel)
                .addComponent(amigosButtonPanel)
        );

        // Llenar las listas con las solicitudes de amigos
        if (solicitudesAmigos != null) {
            for (int i = 0; i < solicitudesAmigos.size(); i += 2) {
                int usuarioEnviaId = solicitudesAmigos.get(i);
                int invitacionId = solicitudesAmigos.get(i + 1);
                UsuarioController usuarioController = new UsuarioController();
                String username = usuarioController.RetornarUsername(usuarioEnviaId);
                amigosListModel.addElement("Solicitud de amigo de Usuario: " + username);
                JButton aceptarAmigoButton = new JButton("Aceptar");
                JButton rechazarAmigoButton = new JButton("Rechazar");
                amigosButtonPanel.add(aceptarAmigoButton);
                amigosButtonPanel.add(rechazarAmigoButton);

                // Agregar los botones al mapa
                buttonMap.put(invitacionId, new JButton[]{aceptarAmigoButton, rechazarAmigoButton});

                aceptarAmigoButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Se aceptó la solicitud de amigo con Invitacion ID: " + invitacionId);
                        requestsController.AceptarSolicitudAmigos(usuarioEnviaId, userId);
                        requestsController.EliminarSolicitudAmigos(invitacionId);
                    }
                });
                rechazarAmigoButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Se rechazó la solicitud de amigo con Invitacion ID: " + invitacionId);
                        requestsController.EliminarSolicitudAmigos(invitacionId);
                    }
                });
            }
        } else {
            amigosListModel.addElement("No hay solicitudes de amigos.");
        }

        // Empaquetar y mostrar la ventana
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void addWindowListener() {
        // Crear una instancia del WindowListener
        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                RequestsMenu view = new RequestsMenu(userId);
                view.setVisible(true);
            }
        };

        // Aplicar el WindowListener al JFrame
        addWindowListener(windowListener);
    }
}