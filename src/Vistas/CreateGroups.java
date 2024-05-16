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
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Samantha
 */
public class CreateGroups extends JFrame {
    private JPanel panelPrincipal;
    private JTextField campoUsuarios;
    private JButton btnCrearGrupo;
    private JTextField campoNombreGrupo;
    int userId;
    String ip;
    String Nombre;

    public CreateGroups(int userId, String ip) {
        this.userId  = userId;
        this.ip = ip;
        initComponents();
        JOptionPane.showMessageDialog(CreateGroups.this, "Separa los usuarios con comas para enviar las invitaciones.");
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
        campoNombreGrupo = new JTextField(15);
        campoUsuarios = new JTextField(30); // El campo de texto es más grande para los nombres de usuario

        btnCrearGrupo = new JButton("Crear Grupo");
    }

    private void setupLayout() {
        JPanel panelNombreGrupo = new JPanel();
        panelNombreGrupo.setLayout(new FlowLayout());
        panelNombreGrupo.add(new JLabel("Nombre del grupo:"));
        panelNombreGrupo.add(campoNombreGrupo);
        
        panelPrincipal.add(panelNombreGrupo, BorderLayout.NORTH);
        
        panelPrincipal.add(new JLabel("Nombres de usuario (separados por coma):"), BorderLayout.CENTER);
        panelPrincipal.add(campoUsuarios, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCrearGrupo);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        add(panelPrincipal);
    }

    private void setupListeners() {
        btnCrearGrupo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usuariosInput = campoUsuarios.getText();
                String[] nombres = usuariosInput.split(",");
                
                if (nombres.length < 2) {
                    JOptionPane.showMessageDialog(CreateGroups.this, "Debe ingresar al menos 2 nombres de usuario");
                    return;
                }

                // Validar que todos los nombres de usuario sean diferentes
                ArrayList<String> nombresList = new ArrayList<>();
                boolean nombresRepetidos = false;
                for (String nombre : nombres) {
                    nombre = nombre.trim(); // Eliminar espacios en blanco al principio y al final
                    if (nombresList.contains(nombre)) {
                        nombresRepetidos = true;
                        break;
                    }
                    nombresList.add(nombre);
                }

                if (nombresRepetidos) {
                    JOptionPane.showMessageDialog(CreateGroups.this, "No se pueden repetir nombres de usuario");
                    return;
                }

                ArrayList<Integer> idsUsuarios = new ArrayList<>();
                for (String nombre : nombresList) {
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
                
                int groupId = InsertarGrupoId();

                boolean solicitudEnviada = enviarSolicitudGrupo(groupId, idsUsuarios);

                if (solicitudEnviada) {
                    JOptionPane.showMessageDialog(CreateGroups.this, "Solicitud de grupo enviada correctamente");
                    dispose();
                    ListaGrupos listasGrupos = new ListaGrupos(userId, ip);
                    listasGrupos.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(CreateGroups.this, "Error al enviar la solicitud de grupo");
                }
            }
        });
        
        pack();
        setLocationRelativeTo(null);
    }

    private int obtenerIdUsuario(String nombreUsuario) {
        UsuarioController usuarioController = new UsuarioController(ip);
        int usuarioRecibeId = usuarioController.EncontrarUsuarios(nombreUsuario);
        return usuarioRecibeId;
    }

    private int InsertarGrupoId() {
        Nombre = campoNombreGrupo.getText();
        RequestsController requestController = new RequestsController(ip);
        int groupId = requestController.InsertarGrupo(userId, Nombre);
        return groupId;
    }

    private boolean enviarSolicitudGrupo(int groupId, ArrayList<Integer> idsUsuarios) {
        for(var usuario : idsUsuarios){
            RequestsController requestController = new RequestsController(ip);
            boolean res = requestController.enviarSolicitudGrupos(groupId, usuario, 1);
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
                ListaGrupos listasGrupos = new ListaGrupos(userId, ip);
                listasGrupos.setVisible(true);
                dispose();
            }
        };

        this.addWindowListener(windowListener);
    }
}