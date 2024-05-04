/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;

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
import models.Usuarios;
import Controllers.UsuarioController;

/**
 *
 * @author Valeria
 */
public class ListaConectados extends JFrame {
    JLabel a, b;
    int userId;
    
    public ListaConectados(int userId) {
        super();
        this.userId = userId;
        init();
        addWindowListener();
    }
    
    private void init() {
        setTitle("Lista de Conectados");
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
        Font font = new Font("Arial", Font.BOLD, 30);
        a = new JLabel("Conectados");
        a.setFont(font);
        
        b = new JLabel("Desconectados");
        b.setFont(font);
        
// inicializando controller usuarios
        UsuarioController usuariosController = new UsuarioController();
// creando paneles de lista de conectados
        ArrayList<Usuarios> conectados = usuariosController.usuariosPorConexion(1, userId);

        // Crear un modelo de lista y agregar los datos
        DefaultListModel<String> modeloListaConectados = new DefaultListModel<>();
        for (Usuarios u : conectados) {
            modeloListaConectados.addElement(u.nombreUsuario);
        }

        // Crear el JList con el modelo de lista
        JList<String> listaConectados = new JList<>(modeloListaConectados);
        
        // Crear un contenedor para el JList
        JPanel panelConectados = new JPanel();
        panelConectados.setLayout(new BorderLayout());
        panelConectados.add(new JScrollPane(listaConectados), BorderLayout.CENTER);

// creando paneles de lista de desconectados
        
        ArrayList<Usuarios> desconectados = usuariosController.usuariosPorConexion(0, userId);
       
        // Crear un modelo de lista y agregar los datos
        DefaultListModel<String> modeloListaDesconectados = new DefaultListModel<>();
        for (Usuarios u : desconectados) {
            modeloListaDesconectados.addElement(u.nombreUsuario);
        }

        // Crear el JList con el modelo de lista
        JList<String> listaDesconectados = new JList<>(modeloListaDesconectados);
        
        // Crear un contenedor para el JList
        JPanel panelDesconectados = new JPanel();
        panelDesconectados.setLayout(new BorderLayout());
        panelDesconectados.add(new JScrollPane(listaDesconectados), BorderLayout.CENTER);
        
// Configurar el diseño horizontal
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(a)  // Etiqueta para la columna 1
                    .addComponent(panelConectados)  // Panel para la columna 1
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(b)  // Etiqueta para la columna 2
                    .addComponent(panelDesconectados)  // Panel para la columna 2
                )
        );

// Configurar el diseño vertical
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(a)  // Etiqueta para la columna 1
                    .addComponent(b)  // Etiqueta para la columna 2
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(panelConectados)  // Panel para la columna 1
                    .addComponent(panelDesconectados)  // Panel para la columna 2
                )
        );
        
// eventos de nomás el de conectados, a los desconectados no le puede mandar mensaje, si quiere mandarles mensaje, que se vaya al de amigos
    listaConectados.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) { // Asegura que solo se maneje un solo evento de selección
                    // Obtener el índice seleccionado
                    int index = listaConectados.getSelectedIndex();
                    if (index != -1) { // Asegura que se haya seleccionado un elemento
                        // Obtener el nombre seleccionado
                        String nombreSeleccionado = modeloListaConectados.getElementAt(index);
                        int userConectadoId = conectados.get(index).usuarioId;
                        
                        // Mostrar un mensaje con el nombre seleccionado
                        JOptionPane.showMessageDialog(null, "Has seleccionado: " + nombreSeleccionado+ " tiene el userId: "+userConectadoId+" y tu tienes el userId: "+userId);
                        // aqui en lugar del mensaje abrir la mensajeria y mandar los dos users (userId, userConectadoId)
                        
                        ChatIndividual chat = new ChatIndividual(userId, userConectadoId, nombreSeleccionado);
                        chat.setVisible(true);
                        dispose();
                    }
                }
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
