/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;

import Controllers.RequestsController;
import Controllers.UsuarioController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
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

        ArrayList<String> solicitudesGrupos = requestsController.obtenerSolicitudesGrupos(userId);

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
            for (int i = 0; i < solicitudesGrupos.size(); i += 3) {
                String Nombre = solicitudesGrupos.get(i + 1);
                String UsuarioDueno = solicitudesGrupos.get(i + 2);
                int dueno = Integer.parseInt(UsuarioDueno);
                UsuarioController usuarioController = new UsuarioController();
                String username = usuarioController.RetornarUsername(dueno);
                gruposListModel.addElement("Solicitud de grupo: " + Nombre + " Creada por: " + username);
                JButton aceptarGrupoButton = new JButton("Aceptar");
                JButton rechazarGrupoButton = new JButton("Rechazar");
                gruposButtonPanel.add(aceptarGrupoButton);
                gruposButtonPanel.add(rechazarGrupoButton);

                aceptarGrupoButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Obtener el índice seleccionado y el nombre del grupo
                        int selectedIndex = gruposList.getSelectedIndex();
                        if (selectedIndex != -1) {
                            int invitacionId = Integer.parseInt(solicitudesGrupos.get(selectedIndex * 3));
                            requestsController.actualizarEstadoSolicitudGrupo(invitacionId, 2);
                            dispose();
                            RequestsMenu view = new RequestsMenu(userId);
                            view.setVisible(true);
                        }
                    }
                });

                rechazarGrupoButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int selectedIndex = gruposList.getSelectedIndex();
                        if (selectedIndex != -1) {
                            int invitacionId = Integer.parseInt(solicitudesGrupos.get(selectedIndex * 3));
                            requestsController.actualizarEstadoSolicitudGrupo(invitacionId, 3);
                            dispose();
                            RequestsMenu view = new RequestsMenu(userId);
                            view.setVisible(true);
                        }
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
