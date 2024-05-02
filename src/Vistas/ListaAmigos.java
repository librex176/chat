/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;

import Controllers.AmigosController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import models.Amigos;

/**
 *
 * @author Valeria
 */
public class ListaAmigos extends JFrame{
    JLabel a;
    int userId;
    
    public ListaAmigos(int userId) {
        super();
        this.userId = userId;
        init();
        addWindowListener();
    }
    
    private void init() {
        setTitle("Lista de mis amigos");
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
        a = new JLabel("Mis Amigos");
        Font font = new Font("Arial", Font.BOLD, 30);
        a.setFont(font);
        
        // creando array list con los usernames
        
        AmigosController amigosController = new AmigosController();
        String nombre = amigosController.selectNameByUserId(userId);
        System.out.println("nombre : "+nombre);
        ArrayList<Amigos> amigos = amigosController.selectMisAmigosUsuarios(userId);
        
        // Crear un modelo de lista y agregar los datos
        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        for (Amigos a : amigos) {
            modeloLista.addElement(a.nombreUsuario);
        }

        // Crear el JList con el modelo de lista
        JList<String> listaAmigos = new JList<>(modeloLista);

        // Crear un contenedor para el JList
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(new JScrollPane(listaAmigos), BorderLayout.CENTER);

        
        // configurar el diseño horizontal
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(a, 20, 100, 300)
                .addComponent(panel)
        );
        
        // configurar el diseño vertical
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(a)
                .addComponent(panel)
        );
        
        
     
        // eventos
        // seleccionar nombre en la lista
        // Agregar un ListSelectionListener para detectar la selección de elementos
        listaAmigos.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // Asegura que solo se maneje un solo evento de selección
                    // Obtener el índice seleccionado
                    int index = listaAmigos.getSelectedIndex();
                    if (index != -1) { // Asegura que se haya seleccionado un elemento
                        // Obtener el nombre seleccionado
                        String nombreSeleccionado = modeloLista.getElementAt(index);
                        int userFriendId = amigos.get(index).usuarioId;
                        
                        // Mostrar un mensaje con el nombre seleccionado
                        JOptionPane.showMessageDialog(null, "Has seleccionado: " + nombreSeleccionado+ " tiene el userId: "+userFriendId+" y tu tienes el userId: "+userId);
                    }
                }
            }
        });
        
        // empaquetar y mostrar la ventana
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