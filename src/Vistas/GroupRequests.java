/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;

import Controllers.GruposController;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Samantha
 */
public class GroupRequests extends JFrame {
    private int userId;
    private String ip;

    public GroupRequests(int userId, String ip) {
        super();
        this.userId = userId;
        this.ip = ip;
        initComponents();
        addWindowListener();
    }

    private void initComponents() {
       
        RequestsController requestsController = new RequestsController(ip);

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
                GruposController gruposController = new GruposController(ip);
                String username = gruposController.selectNameByUserId(dueno);
                gruposListModel.addElement("Solicitud de grupo: " + Nombre + " Creada por: " + username);
                JButton aceptarGrupoButton = new JButton("Aceptar");
                JButton rechazarGrupoButton = new JButton("Rechazar");
                gruposButtonPanel.add(aceptarGrupoButton);
                gruposButtonPanel.add(rechazarGrupoButton);

                aceptarGrupoButton.addActionListener((ActionEvent e) -> {
                    // Obtener el índice seleccionado y el nombre del grupo
                    int selectedIndex = gruposList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        int invitacionId = Integer.parseInt(solicitudesGrupos.get(selectedIndex * 3));
                        requestsController.actualizarEstadoSolicitudGrupo(invitacionId, 2);//
                        dispose();
                        RequestsMenu view = new RequestsMenu(userId, ip);
                        view.setVisible(true);
                    }
                });
                

                rechazarGrupoButton.addActionListener((ActionEvent e) -> {
                    int selectedIndex = gruposList.getSelectedIndex();
                    if (selectedIndex != -1) {
                        int invitacionId = Integer.parseInt(solicitudesGrupos.get(selectedIndex * 3));
                        int grupoId = gruposController.selectGrupoIdInvitaciones(invitacionId);//
                        System.out.println("grupoId: "+grupoId);
                        int cantidadParticipantes = gruposController.selectCuentaParticipantes(grupoId);//
                        System.out.println("Cant participantes: "+ cantidadParticipantes);
                        if(cantidadParticipantes<=2)
                        {
                            
                            boolean borradoMensajes = gruposController.deleteMensajesGrupos(grupoId);//
                            System.out.println("Se borro mensajes: "+borradoMensajes);
                            boolean comprobar = gruposController.deleteInvitacionesGrupos(grupoId);//
                            System.out.println("Borrar invitaciones grupos: " + comprobar);
                            if(comprobar)
                            {
                                boolean verificar = gruposController.deleteGrupo(grupoId);//
                                System.out.println("Borrar grupos: " + verificar);
                            }
                        }
                        dispose();
                        RequestsMenu view = new RequestsMenu(userId, ip);
                        view.setVisible(true);
                    }
                });
            }
        } else {
             JOptionPane.showMessageDialog(GroupRequests.this, "No tienes solicitudes de grupos");
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
                RequestsMenu view = new RequestsMenu(userId, ip);
                view.setVisible(true);
            }
        };

        // Aplicar el WindowListener al JFrame
        addWindowListener(windowListener);
    }
}
