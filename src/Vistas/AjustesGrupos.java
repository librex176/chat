/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;

import Controllers.GruposController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
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
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.event.ListSelectionEvent;
import models.Grupos;

/**
 *
 * @author aacar
 */
public class AjustesGrupos extends JFrame{
    int usuarioId;
    int grupoId;
    String ip;

    public AjustesGrupos(int usuarioId, int grupoId, String ip){
        this.usuarioId = usuarioId;
        this.grupoId = grupoId;
        this.ip = ip;
        init();
        addWindowListener();
    }
    
    private void init()
    {
        GruposController gruposController = new GruposController(ip);
        String nombreGrupo = gruposController.selectNombreGrupo(grupoId);
        setTitle("Ajustes de " + nombreGrupo);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        
        // Crear el layout principal
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        // Panel para las solicitudes de grupos
        JPanel usuarioPanel = new JPanel(new BorderLayout());
        usuarioPanel.setBorder(BorderFactory.createTitledBorder("Usuarios pertenecientes al grupo"));
        DefaultListModel<String> gruposListModel = new DefaultListModel<>();
        JList<String> gruposList = new JList<>(gruposListModel);
        JScrollPane gruposScrollPane = new JScrollPane(gruposList);
        usuarioPanel.add(gruposScrollPane, BorderLayout.CENTER);

        // Botones de aceptar y rechazar para las solicitudes de grupos
        JPanel gruposButtonPanel = new JPanel();
        usuarioPanel.add(gruposButtonPanel, BorderLayout.SOUTH);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(usuarioPanel)
                .addComponent(gruposButtonPanel)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(usuarioPanel)
                .addComponent(gruposButtonPanel)
        );
    }
    
    private void addWindowListener() {
        // Crear una instancia del WindowListener
        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                
            }
        };

        this.addWindowListener(windowListener);
    }
    
}
