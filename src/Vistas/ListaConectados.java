/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import Controllers.UsuarioController;
import javax.swing.Timer;

public class ListaConectados extends JFrame {
    JLabel a, b;
    int userId;
    String ip;
    ArrayList<String[]> conectados;
    Timer timer;
    DefaultListModel<String> modeloListaConectados;
    DefaultListModel<String> modeloListaDesconectados;

    public ListaConectados(int userId, String ip) {
        super();
        this.userId = userId;
        this.ip = ip;
        init();
        addWindowListener();
        iniciarTimer();
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
        UsuarioController usuariosController = new UsuarioController(ip);
        // creando paneles de lista de conectados
        JPanel panelConectados = new JPanel();
        panelConectados.setLayout(new BorderLayout());

        // Crear un modelo de lista vacío para los conectados
        modeloListaConectados = new DefaultListModel<>();
        // Crear el JList con el modelo de lista para los conectados
        JList<String> listaConectados = new JList<>(modeloListaConectados);
        // Agregar el JList a un JScrollPane y luego al panel para los conectados
        panelConectados.add(new JScrollPane(listaConectados), BorderLayout.CENTER);

        // creando paneles de lista de desconectados
        JPanel panelDesconectados = new JPanel();
        panelDesconectados.setLayout(new BorderLayout());

        // Crear un modelo de lista vacío para los desconectados
        modeloListaDesconectados = new DefaultListModel<>();
        // Crear el JList con el modelo de lista para los desconectados
        JList<String> listaDesconectados = new JList<>(modeloListaDesconectados);
        // Agregar el JList a un JScrollPane y luego al panel para los desconectados
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

        listaConectados.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int index = listaConectados.getSelectedIndex();
                    if (index != -1) {
                        String nombreSeleccionado = modeloListaConectados.getElementAt(index);
                        int userConectadoId = Integer.parseInt(conectados.get(index)[0]);
                        //int amigosId = Integer.parseInt(conectados.get(index)[2]);
                        System.out.println("entra en value changed ");
                        System.out.println("userConectadoId: " + userConectadoId);
                        System.out.println("nombre seleccionado: " + nombreSeleccionado);
                        // Mostrar un JOptionPane para preguntar al usuario si quiere ir al chat o eliminar al amigo
                        JPanel panelConfirmacion = new JPanel();
                        panelConfirmacion.add(new JLabel("¿Quiere ir al chat de "+nombreSeleccionado+"?"));

                        // Mostrar un JOptionPane con el panel de confirmación
                        int opcion = JOptionPane.showOptionDialog(ListaConectados.this,
                                panelConfirmacion,
                                "Confirmar Redireccionamiento",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                new String[]{"Ir", "Cancelar"},
                                "Ir");

                        // Según la opción seleccionada por el usuario
                        if (opcion == JOptionPane.YES_OPTION) {
                            // Iniciar el chat
                            timer.stop();
                            ChatIndividual chat = new ChatIndividual(userId, userConectadoId, nombreSeleccionado, ip);
                            chat.setVisible(true);
                            dispose();
                            
                        } else if (opcion == JOptionPane.NO_OPTION || opcion == JOptionPane.CLOSED_OPTION) {
                            // El usuario canceló la operación de eliminación
                            System.out.println("No se abrió el chat de "+nombreSeleccionado);
                        }
                    }
                }
            }
        });

        // Llamar al método para actualizar la lista de conectados al inicio
        actualizarListaConectados(modeloListaConectados);
        // Llamar al método para actualizar la lista de desconectados al inicio
        actualizarListaDesconectados(modeloListaDesconectados);

        pack(); // ajustar el tamaño de la ventana según el contenido
        setLocationRelativeTo(null); // centrar la ventana en la pantalla
    }

    private void actualizarListaConectados(DefaultListModel<String> modeloListaConectados) {
        UsuarioController usuariosController = new UsuarioController(ip);
        conectados = usuariosController.usuariosPorConexionServer(1, userId, ip);
        modeloListaConectados.clear();
        if(conectados != null){
            // Limpiar el modelo de lista de conectados
            // Agregar los conectados al modelo de lista de conectados
            for (String[] u : conectados) {
                modeloListaConectados.addElement(u[1]);
            }
        }
    }

    private void actualizarListaDesconectados(DefaultListModel<String> modeloListaDesconectados) {
        UsuarioController usuariosController = new UsuarioController(ip);
        ArrayList<String[]> desconectados = usuariosController.usuariosPorConexionServer(0, userId, ip);
        modeloListaDesconectados.clear();
        if(desconectados != null){
            // Limpiar el modelo de lista de desconectados
            // Agregar los desconectados al modelo de lista de desconectados
            for (String[] u : desconectados) {
                modeloListaDesconectados.addElement(u[1]);
            }
        }
    }

    private void addWindowListener() {
        // Crear una instancia del WindowListener
        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                timer.stop();
                // Aquí puedes redirigir al usuario a la ventana ListasMenu y pasar el parámetro userId
                ListasMenu listasMenu = new ListasMenu(userId, ip);
                listasMenu.setVisible(true);
            }
        };

        this.addWindowListener(windowListener);
    }
    private void iniciarTimer() {
        // Crear el timer que se ejecutará cada 4 segundos
        timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Llamar al método para actualizar la lista de conectados
                actualizarListaConectados(modeloListaConectados);
                actualizarListaDesconectados(modeloListaDesconectados);
                //actualizarListaConectados((DefaultListModel<String>) ((JList<?>) ((JScrollPane) ((JPanel) getContentPane().getComponent(1)).getComponent(0)).getViewport().getView()).getModel());
                // Llamar al método para actualizar la lista de desconectados
               // actualizarListaDesconectados((DefaultListModel<String>) ((JList<?>) ((JScrollPane) ((JPanel) getContentPane().getComponent(2)).getComponent(0)).getViewport().getView()).getModel());
            }
        });
        // Iniciar el timer
        timer.start();
    }
}
