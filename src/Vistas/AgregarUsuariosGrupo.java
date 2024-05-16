/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;

import Controllers.GruposController;
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
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author aacar
 */
public class AgregarUsuariosGrupo extends JFrame{
    int grupoId;
    private JPanel panelPrincipal;
    private JTextField campoUsuarios;
    private JButton btnCrearGrupo;
    private JLabel campoNombreGrupo;
    int userId;
    String ip;

    public AgregarUsuariosGrupo(int grupoId, int userId, String ip) {
        super();
        this.grupoId = grupoId;
        this.userId = userId;
        this.ip = ip;
        initComponents();
        JOptionPane.showMessageDialog(AgregarUsuariosGrupo.this, "Separa los usuarios con comas para enviar las invitaciones.");
        setupLayout();
        setupListeners();
        pack();
        setLocationRelativeTo(null);
        addWindowListener();
    }
    
    private void initComponents() {
        GruposController gruposController = new GruposController(ip);
        String nombreGrupo = gruposController.selectNombreGrupo(grupoId);///////////////////////////////
        setTitle(nombreGrupo);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        campoNombreGrupo = new JLabel(nombreGrupo);
        campoUsuarios = new JTextField(30); // El campo de texto es más grande para los nombres de usuario

        btnCrearGrupo = new JButton("Agregar usuarios");
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
                
                if (nombres.length < 1) {
                    JOptionPane.showMessageDialog(AgregarUsuariosGrupo.this, "Debe ingresar al menos 1 nombre de usuario");
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
                    JOptionPane.showMessageDialog(AgregarUsuariosGrupo.this, "No se pueden repetir nombres de usuario");
                    return;
                }
                
                

                ArrayList<Integer> idsUsuarios = new ArrayList<>();
                List<Integer> idUsuariosExistentes = obtenerIdUsuariosPertenecientes();
                for (String nombre : nombresList) {
                    int idUsuario = obtenerIdUsuario(nombre);
                    if (idUsuario == -1) {
                        JOptionPane.showMessageDialog(AgregarUsuariosGrupo.this, "El usuario '" + nombre + "' no existe");
                        return;
                    } else if(idUsuariosExistentes.contains(idUsuario))
                    {
                        JOptionPane.showMessageDialog(AgregarUsuariosGrupo.this, nombre + " ya es parte del grupo o su invitación ya esta enviada");
                        return;
                    }
                    idsUsuarios.add(idUsuario);
                }
                
                // Verificar que el usuario no esté enviando una solicitud a sí mismo
                int idUsuarioActual = userId;
                if (idsUsuarios.contains(idUsuarioActual)) {
                    JOptionPane.showMessageDialog(AgregarUsuariosGrupo.this, "No puedes enviarte una solicitud a ti mismo");
                    return;
                }

                boolean solicitudEnviada = enviarSolicitudGrupo(grupoId, idsUsuarios);
                if (solicitudEnviada) {
                    JOptionPane.showMessageDialog(AgregarUsuariosGrupo.this, "Solicitud de grupo enviada correctamente");
                    dispose();
                    ListaGrupos listasGrupos = new ListaGrupos(userId, ip);
                    listasGrupos.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(AgregarUsuariosGrupo.this, "Error al enviar la solicitud de grupo");
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
    
    private List<Integer> obtenerIdUsuariosPertenecientes()
    {
        GruposController grupoController = new GruposController(ip);
        //System.out.println("groupId " + grupoId);
        return grupoController.selectMiembrosGruposInvitaciones(grupoId);
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
                AjustesGrupos listasGrupos = new AjustesGrupos(userId, grupoId, ip);
                listasGrupos.setVisible(true);
                dispose();
            }
        };
        this.addWindowListener(windowListener);
    }
    
}
