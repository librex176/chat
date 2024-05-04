/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;
import Controllers.RequestsController;
import Controllers.UsuarioController;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Samantha
 */
public class CreateGroups extends JFrame {
    private JPanel panelPrincipal;
    private ArrayList<JTextField> camposUsuarios;
    private JButton btnAgregarUsuario;
    private JButton btnCrearGrupo;
    int userId;

    public CreateGroups(int userId) {
        this.userId  = userId;
        initComponents();
        setupLayout();
        setupListeners();
        pack();
        setLocationRelativeTo(null);
        addWindowListener();
    }

    private void initComponents() {
        setTitle("Crear Grupo");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());

        camposUsuarios = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            camposUsuarios.add(new JTextField(15));
        }

        btnAgregarUsuario = new JButton("+");
        btnCrearGrupo = new JButton("Crear Grupo");
    }

    private void setupLayout() {
        JPanel panelUsuarios = new JPanel();
        panelUsuarios.setLayout(new FlowLayout());

        for (JTextField campoUsuario : camposUsuarios) {
            panelUsuarios.add(campoUsuario);
        }

        panelPrincipal.add(new JLabel("Nombres de usuario:"), BorderLayout.NORTH);
        panelPrincipal.add(panelUsuarios, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregarUsuario);
        panelBotones.add(btnCrearGrupo);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    private void setupListeners() {
        btnAgregarUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField nuevoCampo = new JTextField(15);
                camposUsuarios.add(nuevoCampo);
                panelPrincipal.add(nuevoCampo, BorderLayout.CENTER);
                panelPrincipal.revalidate();
                panelPrincipal.repaint();
            }
        });

        btnCrearGrupo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (camposUsuarios.size() < 3) {
                    JOptionPane.showMessageDialog(CreateGroups.this, "Debe ingresar al menos 3 nombres de usuario");
                    return;
                }
                
                for (JTextField campo : camposUsuarios) {
                    if (campo.getText().isEmpty()) {
                        JOptionPane.showMessageDialog(CreateGroups.this, "Debe ingresar al menos 3 nombres de usuario");
                        return;
                    }
                }

                // Validar que todos los nombres de usuario sean diferentes
                ArrayList<String> nombres = new ArrayList<>();
                boolean nombresRepetidos = false;
                for (JTextField campo : camposUsuarios) {
                    String nombre = campo.getText();
                    if (nombres.contains(nombre)) {
                        nombresRepetidos = true;
                        break;
                    }
                    nombres.add(nombre);
                }

                if (nombresRepetidos) {
                    JOptionPane.showMessageDialog(CreateGroups.this, "No se pueden repetir nombres de usuario");
                    return;
                }

                ArrayList<Integer> idsUsuarios = new ArrayList<>();
                for (String nombre : nombres) {
                    int idUsuario = obtenerIdUsuario(nombre);
                    if (idUsuario == -1) {
                        JOptionPane.showMessageDialog(CreateGroups.this, "El usuario '" + nombre + "' no existe");
                        return;
                    }
                    idsUsuarios.add(idUsuario);
                }
                
                // Verificar que el usuario no esté enviando una solicitud a sí mismo
                int idUsuarioActual = userId;
                if (idsUsuarios.contains(idUsuarioActual)) {
                    JOptionPane.showMessageDialog(CreateGroups.this, "No puedes enviarte una solicitud a ti mismo");
                    return;
                }

                int groupId = obtenerMaxGroupId();

                boolean solicitudEnviada = enviarSolicitudGrupo(groupId, idsUsuarios);

                if (solicitudEnviada) {
                    JOptionPane.showMessageDialog(CreateGroups.this, "Solicitud de grupo enviada correctamente");
                } else {
                    JOptionPane.showMessageDialog(CreateGroups.this, "Error al enviar la solicitud de grupo");
                }
            }
        });
        
        pack();
        setLocationRelativeTo(null);
    }

    private int obtenerIdUsuario(String nombreUsuario) {
        UsuarioController usuarioController = new UsuarioController();
        int usuarioRecibeId = usuarioController.EncontrarUsuarios(nombreUsuario);
        return usuarioRecibeId;
    }

    private int obtenerMaxGroupId() {
        RequestsController requestController = new RequestsController();
        int groupId = requestController.obtenerMaxGroupId();
        return groupId;
    }

    private boolean enviarSolicitudGrupo(int groupId, ArrayList<Integer> idsUsuarios) {
        for(var usuarios : idsUsuarios){
            RequestsController requestController = new RequestsController();
            boolean res =requestController.enviarSolicitudGrupos(groupId, userId, usuarios);
            if(res == false){
                System.out.println("no se enviaron las solicitudes");
                return false;
            }
        }
        return true;
    }
    
    private void addWindowListener() {
        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ListaGrupos listasGrupos = new ListaGrupos(userId);
                listasGrupos.setVisible(true);
                dispose();
            }
        };

        this.addWindowListener(windowListener);
    }
}
