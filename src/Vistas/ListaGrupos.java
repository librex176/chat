/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;

import Controllers.GruposController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
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
 * @author Valeria
 */
public class ListaGrupos extends JFrame {
    JButton enviarSolicitud;
    int userId;
    String ip;
    JLabel a;
    
    public ListaGrupos(int userId, String ip)
    {
        super();
        this.userId = userId;
        this.ip = ip;
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
        
        a = new JLabel("Mis Grupos");
        Font font = new Font("Arial", Font.BOLD, 30);
        a.setFont(font);
        enviarSolicitud = new JButton("Crear Grupo");
        
        // creando array list con los usernames
        GruposController gruposController = new GruposController(ip);
        String nombre = gruposController.selectNameByUserId(userId);//
        System.out.println("nombre : "+nombre);
        ArrayList<Grupos> grupos = gruposController.selectMisGrupos(userId);
        // Crear un modelo de lista y agregar los datos
        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        for (Grupos g : grupos) {
            System.out.println("g:" + g.nombre);
            modeloLista.addElement(g.nombre);
        }
        // Crear el JList con el modelo de lista
        JList<String> listaGrupos = new JList<>(modeloLista);

        // Crear un contenedor para el JList
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(listaGrupos), BorderLayout.CENTER);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(a, 20, 100, 300)
                .addComponent(panel)
                .addComponent(enviarSolicitud, 20, 200, 400)
        );
        
        // configurar el diseño vertical
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(a)
                .addComponent(panel)
                .addComponent(enviarSolicitud)
        );
        
        // eventos
        // seleccionar nombre en la lista
        // Agregar un ListSelectionListener para detectar la selección de elementos
        listaGrupos.addListSelectionListener((ListSelectionEvent e) -> {
            if (!e.getValueIsAdjusting()) { // Asegura que solo se maneje un solo evento de selección
                // Obtener el índice seleccionado
                int index = listaGrupos.getSelectedIndex();
                if (index != -1) { // Asegura que se haya seleccionado un elemento
                    // Obtener el nombre seleccionado
                    int grupoId = grupos.get(index).grupoId;
                    System.out.println("el grupoId es: "+ grupoId);
                    ChatGrupal chat = new ChatGrupal(userId, grupoId, ip);
                    chat.setVisible(true);
                    dispose();
                }
            }
        });
        
        
        enviarSolicitud.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirigir a SendRequestForm y pasar userId
                CreateGroups createGroups = new CreateGroups(userId, ip);
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
