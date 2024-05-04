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
public class GroupRequests extends JFrame {
    private int userId;

    public GroupRequests(int userId) {
        this.userId = userId;
        initComponents();
        addWindowListener();
    }

    private void initComponents() {
        RequestsController requestsController = new RequestsController();

        ArrayList<Integer> solicitudesGrupos = requestsController.obtenerSolicitudesGrupos(userId);

        setTitle("Lista de solicitudes de grupos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        // Crear el layout principal
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        // Panel para las solicitudes de grupos
        JPanel gruposPanel = new JPanel(new BorderLayout());
        gruposPanel.setBorder(BorderFactory.createTitledBorder("Solicitudes de Grupos"));
        DefaultListModel<String> gruposListModel = new DefaultListModel<>();
        JList<String> gruposList = new JList<>(gruposListModel);
        JScrollPane gruposScrollPane = new JScrollPane(gruposList);
        gruposPanel.add(gruposScrollPane, BorderLayout.CENTER);

        // Botones de aceptar y rechazar para las solicitudes de grupos
        JPanel gruposButtonPanel = new JPanel();
        gruposPanel.add(gruposButtonPanel, BorderLayout.SOUTH);

        // Mapa para vincular las solicitudes con sus botones correspondientes
        Map<Integer, JButton[]> buttonMap = new HashMap<>();

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(gruposPanel)
                .addComponent(gruposButtonPanel)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(gruposPanel)
                .addComponent(gruposButtonPanel)
        );

        // Llenar las listas con las solicitudes de grupos
        if (solicitudesGrupos != null) {
            for (int i = 0; i < solicitudesGrupos.size(); i += 2) {
                int usuarioEnviaId = solicitudesGrupos.get(i);
                int invitacionId = solicitudesGrupos.get(i + 1);
                UsuarioController usuarioController = new UsuarioController();
                String username = usuarioController.RetornarUsername(usuarioEnviaId);
                gruposListModel.addElement("Solicitud de grupo creada por Usuario ID: " + username);
                JButton aceptarGrupoButton = new JButton("Aceptar");
                JButton rechazarGrupoButton = new JButton("Rechazar");
                gruposButtonPanel.add(aceptarGrupoButton);
                gruposButtonPanel.add(rechazarGrupoButton);

                // Agregar los botones al mapa
                buttonMap.put(invitacionId, new JButton[]{aceptarGrupoButton, rechazarGrupoButton});

                aceptarGrupoButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Se aceptó la solicitud de grupo con Invitacion ID: " + invitacionId);
                        // Aquí puedes realizar las acciones necesarias para aceptar la solicitud de grupo
                    }
                });
                rechazarGrupoButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println("Se rechazó la solicitud de grupo con Invitacion ID: " + invitacionId);
                        // Aquí puedes realizar las acciones necesarias para rechazar la solicitud de grupo
                    }
                });
            }
        } else {
            gruposListModel.addElement("No hay solicitudes de grupos.");
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