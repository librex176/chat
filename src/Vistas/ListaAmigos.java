/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;
import Controllers.AmigosController;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListaAmigos extends JFrame {
    JLabel a;
    JButton enviarSolicitud;
    JButton btnActualizar; // Nuevo botón para actualizar la lista
    int userId;
    String ip;
    DefaultListModel<String> modeloLista; // Declaración de modeloLista fuera del método init()
    ArrayList<String[]> amigosServer; // Declaración de amigosServer

    public ListaAmigos(int userId, String ip) {
        super();
        this.userId = userId;
        this.ip = ip;
        init();
        addWindowListener();
    }

    private void init() {
        setTitle("Lista de mis amigos");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        // Crear groupLayout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        // Establecer el auto ajuste de gaps
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        // Inicializar elementos
        // Label de amigos
        a = new JLabel("Mis Amigos");
        Font font = new Font("Arial", Font.BOLD, 30);
        a.setFont(font);
        enviarSolicitud = new JButton("Enviar Solicitud");

        // Crear el botón "Actualizar"
        btnActualizar = new JButton("Actualizar");

        // Agregar un ActionListener al botón "Actualizar"
        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Llamar al método para actualizar la lista de amigos
                actualizarListaAmigos();
            }
        });

        // Crear un contenedor para el JList
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Configurar el diseño horizontal
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(a, 20, 100, 300)
                .addComponent(panel)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(enviarSolicitud, 20, 200, 400)
                    .addComponent(btnActualizar, 20, 200, 400))
        );

        // Configurar el diseño vertical
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(a)
                .addComponent(panel)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(enviarSolicitud)
                    .addComponent(btnActualizar))
        );

        // Crear un modelo de lista vacío
        modeloLista = new DefaultListModel<>();

        // Crear el JList con el modelo de lista
        JList<String> listaAmigos = new JList<>(modeloLista);

        // Agregar el JList a un JScrollPane y luego al panel
        panel.add(new JScrollPane(listaAmigos), BorderLayout.CENTER);

        // Eventos
        // Seleccionar nombre en la lista
        listaAmigos.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int index = listaAmigos.getSelectedIndex();
                    if (index != -1) {
                        String nombreSeleccionado = modeloLista.getElementAt(index);
                        int userFriendId = Integer.parseInt(amigosServer.get(index)[0]);
                        int amigosId = Integer.parseInt(amigosServer.get(index)[2]);
                        System.out.println("entra en value changed ");
                        System.out.println("userFriendId: " + userFriendId);
                        System.out.println("nombre seleccionado: " + nombreSeleccionado);
                        // Mostrar un JOptionPane para preguntar al usuario si quiere ir al chat o eliminar al amigo
                        int opcion = JOptionPane.showOptionDialog(ListaAmigos.this,
                                "¿Qué desea hacer con " + nombreSeleccionado + "?",
                                "Acciones disponibles",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                new String[]{"Ir al chat", "Eliminar"},
                                "Ir al chat");

                        // Según la opción seleccionada por el usuario
                        if (opcion == JOptionPane.YES_OPTION) {
                            // Iniciar el chat
                            ChatIndividual chat = new ChatIndividual(userId, userFriendId, nombreSeleccionado);
                            chat.setVisible(true);
                            dispose();
                        } else if (opcion == JOptionPane.NO_OPTION) {
                            // Eliminar al amigo
                                // Crear un JPanel de confirmación
                            JPanel panelConfirmacion = new JPanel();
                            panelConfirmacion.add(new JLabel("¿Seguro que desea eliminar a "+nombreSeleccionado+" como amigo?"));

                            // Mostrar un JOptionPane con el panel de confirmación
                            int opcionEliminar = JOptionPane.showOptionDialog(ListaAmigos.this,
                                    panelConfirmacion,
                                    "Confirmar eliminación",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE,
                                    null,
                                    new String[]{"Eliminar", "Cancelar"},
                                    "Eliminar");

                            // Según la opción seleccionada por el usuario
                            if (opcionEliminar == JOptionPane.YES_OPTION) {
                                eliminarAmigo(amigosId);
                            } else if (opcionEliminar == JOptionPane.NO_OPTION || opcionEliminar == JOptionPane.CLOSED_OPTION) {
                                // El usuario canceló la operación de eliminación
                                System.out.println("Eliminación cancelada");
                            }
                        }
                    }
                }
            }
        });

        // Redirigir a SendRequestForm y pasar userId
        enviarSolicitud.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SendRequestForm sendRequestForm = new SendRequestForm(userId, ip);
                sendRequestForm.setVisible(true);
                dispose();
            }
        });

        // Empaquetar y mostrar la ventana
        pack();
        setLocationRelativeTo(null);

        // Llamar al método para actualizar la lista de amigos al inicio
        actualizarListaAmigos();
    }

    private void actualizarListaAmigos() {
        AmigosController amigosController = new AmigosController();
       // String nombre = amigosController.selectNameByUserId(userId);
       // System.out.println("nombre : " + nombre);
        amigosServer = amigosController.selectMisAmigosByUserIdServer(userId, ip); // No necesitas crear una nueva lista aquí
        if (amigosServer != null) {
            modeloLista.clear();
            for (String[] a : amigosServer) {
                modeloLista.addElement(a[1]);
            }
        }
    }
    private void eliminarAmigo(int amigosId) {
        
        System.out.println("Amigo eliminado, el amigos Id es:  " + amigosId);
        AmigosController amigosController = new AmigosController();
        boolean res = amigosController.deleteAmigoServer(amigosId, ip);
        if(res == true){System.out.println("amistad eliminada!");}
        else{System.out.println("No se pudo eliminar la amistad!");}
        actualizarListaAmigos();
    }

    private void addWindowListener() {
        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ListasMenu listasMenu = new ListasMenu(userId);
                listasMenu.setVisible(true);
            }
        };
        this.addWindowListener(windowListener);
    }
}
