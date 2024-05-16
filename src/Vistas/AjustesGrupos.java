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
import javax.swing.JButton;
import javax.swing.Timer;

public class AjustesGrupos extends JFrame {
    JLabel a, b;
    int grupoId;
    int userId;
    String ip;
    ArrayList<String[]> conectados;
    ArrayList<String[]> desconectados;
    Timer timer;
    DefaultListModel<String> modeloListaConectados;
    DefaultListModel<String> modeloListaDesconectados;

    public AjustesGrupos(int userId, int grupoId, String ip) {
        super();
        this.userId = userId;
        this.grupoId = grupoId;
        this.ip = ip;
        init();
        addWindowListener();
        iniciarTimer();
    }

    private void init() {
        setTitle("Miembros del grupo");
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
        GruposController gruposController = new GruposController(ip);
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

        JButton addButton = new JButton("+");
        addButton.setFont(new Font("Arial", Font.BOLD, 20));
        
         // Configurar el diseño horizontal
        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(a)
                    .addComponent(panelConectados)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(b)
                    .addComponent(panelDesconectados)
                )
                .addComponent(addButton)
        );

        // Configurar el diseño vertical
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(a)
                    .addComponent(b)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(panelConectados)
                    .addComponent(panelDesconectados)
                )
                .addComponent(addButton)
        );
        
        boolean isOwner = gruposController.selectDuenoId(grupoId, userId);
        if(isOwner)
        {
            listaConectados.addListSelectionListener((ListSelectionEvent e) -> {
                if (!e.getValueIsAdjusting()) {
                    int index = listaConectados.getSelectedIndex();
                    if (index != -1) {
                        String nombreSeleccionado = modeloListaConectados.getElementAt(index);
                        int userConectadoId = Integer.parseInt(conectados.get(index)[1]);
                        JPanel panelConfirmacion = new JPanel();
                        panelConfirmacion.add(new JLabel("¿Desea eliminar a " + nombreSeleccionado + " del grupo?"));
                        // Mostrar el diálogo de confirmación
                        int opcion = JOptionPane.showConfirmDialog(
                                AjustesGrupos.this,
                                panelConfirmacion,
                                "Confirmación",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );
                        if (opcion == JOptionPane.YES_OPTION) {
                            boolean esOno = gruposController.selectDuenoId(grupoId, userConectadoId);
                            if(esOno)
                            {
                                int response = JOptionPane.showConfirmDialog(
                                    AjustesGrupos.this,
                                    "¿Estás seguro de que deseas salir del grupo? Borrará todo debido a que eres el dueño.",
                                    "Confirmación",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE
                                );
                                if (response == JOptionPane.YES_OPTION) {
                                    gruposController.deleteMensajesGrupos(grupoId);
                                    boolean comprobar = gruposController.deleteInvitacionesGrupos(grupoId);
                                    if (comprobar) {
                                        gruposController.deleteGrupo(grupoId);
                                        timer.stop();
                                        dispose();
                                        ListaGrupos listasGrupos = new ListaGrupos(userId, ip);
                                        listasGrupos.setVisible(true);
                                    }
                                }
                            }else {
                                int cantidadParticipantes = gruposController.selectCuentaParticipantes(grupoId);
                                System.out.println("participantes es: " + cantidadParticipantes);
                                if (cantidadParticipantes <= 2) {
                                    System.out.println("entra minimo participantes");
                                    gruposController.deleteMensajesGrupos(grupoId);
                                    boolean comprobar = gruposController.deleteInvitacionesGrupos(grupoId);
                                    if (comprobar) {
                                        System.out.println("comprobacion");
                                        gruposController.deleteGrupo(grupoId);
                                    }
                                } else {
                                    System.out.println("entra solo el solito");
                                    gruposController.deleteUsuarioRecibeId(grupoId, userId);
                                }
                                timer.stop();
                                dispose();
                                ChatGrupal view = new ChatGrupal(userId, grupoId, ip);
                                view.setVisible(true);
                            }
                        }
                    }
                }
            });
            addButton.addActionListener((ActionEvent e) -> {
                AgregarUsuariosGrupo agregarUsuarios = new AgregarUsuariosGrupo(grupoId, userId, ip);
                agregarUsuarios.setVisible(true);
                dispose();
                setVisible(false);
            });
            listaDesconectados.addListSelectionListener((ListSelectionEvent e) -> {
                if (!e.getValueIsAdjusting()) {
                    int index = listaDesconectados.getSelectedIndex();
                    if (index != -1) {
                        String nombreSeleccionado = modeloListaDesconectados.getElementAt(index);
                        int userConectadoId = Integer.parseInt(desconectados.get(index)[1]);
                        JPanel panelConfirmacion = new JPanel();
                        panelConfirmacion.add(new JLabel("¿Desea eliminar a " + nombreSeleccionado + " del grupo?"));
                        // Mostrar el diálogo de confirmación
                        int opcion = JOptionPane.showConfirmDialog(
                                AjustesGrupos.this,
                                panelConfirmacion,
                                "Confirmación",
                                JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE
                        );
                        if (opcion == JOptionPane.YES_OPTION) {
                            boolean esOno = gruposController.selectDuenoId(grupoId, userConectadoId);
                            if(esOno)
                            {
                                int response = JOptionPane.showConfirmDialog(
                                    AjustesGrupos.this,
                                    "¿Estás seguro de que deseas salir del grupo? Borrará todo debido a que eres el dueño.",
                                    "Confirmación",
                                    JOptionPane.YES_NO_OPTION,
                                    JOptionPane.QUESTION_MESSAGE
                                );
                                if (response == JOptionPane.YES_OPTION) {
                                    gruposController.deleteMensajesGrupos(grupoId);
                                    boolean comprobar = gruposController.deleteInvitacionesGrupos(grupoId);
                                    if (comprobar) {
                                        gruposController.deleteGrupo(grupoId);
                                        timer.stop();
                                        dispose();
                                        ListaGrupos listasGrupos = new ListaGrupos(userId, ip);
                                        listasGrupos.setVisible(true);
                                    }
                                }
                            }else {
                                int cantidadParticipantes = gruposController.selectCuentaParticipantes(grupoId);
                                System.out.println("participantes es: " + cantidadParticipantes);
                                if (cantidadParticipantes <= 2) {
                                    //System.out.println("entra minimo participantes");
                                    gruposController.deleteMensajesGrupos(grupoId);
                                    boolean comprobar = gruposController.deleteInvitacionesGrupos(grupoId);
                                    if (comprobar) {
                                        //System.out.println("comprobacion");
                                        gruposController.deleteGrupo(grupoId);
                                    }
                                } else {
                                    System.out.println("entra solo el solito");
                                    gruposController.deleteUsuarioRecibeId(grupoId, userConectadoId);
                                }
                                timer.stop();
                                dispose();
                                ChatGrupal view = new ChatGrupal(userId, grupoId, ip);
                                view.setVisible(true);
                            }
                        }
                    }
                }
            });
        }
        
        // Llamar al método para actualizar la lista de conectados al inicio
        actualizarListaConectados(modeloListaConectados);
        // Llamar al método para actualizar la lista de desconectados al inicio
        actualizarListaDesconectados(modeloListaDesconectados);

        pack(); // ajustar el tamaño de la ventana según el contenido
        setLocationRelativeTo(null); // centrar la ventana en la pantalla*/
    }

    private void actualizarListaConectados(DefaultListModel<String> modeloListaConectados) {
        GruposController gruposController = new GruposController(ip);
        conectados = gruposController.selectMiembrosGrupos(grupoId, 1);
        modeloListaConectados.clear();
        if(conectados != null){
            for (String[] u : conectados) {
                modeloListaConectados.addElement(u[0]);
            }
        }
    }

    private void actualizarListaDesconectados(DefaultListModel<String> modeloListaDesconectados) {
        GruposController gruposController = new GruposController(ip);
        desconectados = gruposController.selectMiembrosGrupos(grupoId, 0);
        modeloListaDesconectados.clear();
        if(desconectados != null){
            for (String[] u : desconectados) {
                modeloListaDesconectados.addElement(u[0]);
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
                actualizarListaConectados(modeloListaConectados);
                actualizarListaDesconectados(modeloListaDesconectados);
            }
        });
        // Iniciar el timer
        timer.start();
    }
}
