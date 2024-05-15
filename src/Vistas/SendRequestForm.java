/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vistas;
import Controllers.RequestsController;
import Controllers.UsuarioController;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.*;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

/**
 *
 * @author Samantha
 */
public class SendRequestForm extends JFrame{
    JLabel texto;
    JTextField entrada;
    JButton enviar;
    int userId;
    String ip;
 
    public SendRequestForm(int userId, String ip) {
        super();
        this.userId  = userId;
        this.ip = ip;
        initComponents();
        addWindowListener();
    }

    private void initComponents() {
        texto = new JLabel("Nombre de usuario:");
        Font font = new Font("Arial", Font.BOLD, 30);
        entrada = new JTextField();
        enviar = new JButton();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        enviar.setText("Enviar solicitud");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(texto)
                    .addComponent(entrada, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)
                    .addComponent(enviar))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(texto)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(entrada, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(enviar)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        
        enviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreUsuario = entrada.getText();
                UsuarioController usuarioController = new UsuarioController();
                int usuarioRecibeId = usuarioController.EncontrarUsuarios(nombreUsuario);
                
                if (usuarioRecibeId != -1) {
                    //No se puede enviar a si mismo
                    int idUsuarioActual = userId;
                    if (usuarioRecibeId == idUsuarioActual) {
                        JOptionPane.showMessageDialog(SendRequestForm.this, "No puedes enviarte una solicitud a ti mismo");
                        return;
                    }
                    // Usuario encontrado, enviar solicitud de amistad
                    RequestsController requestController = new RequestsController();
                    requestController.enviarSolicitudAmigos(userId, usuarioRecibeId);
                    JOptionPane.showMessageDialog(SendRequestForm.this, "Solicitud enviada a " + nombreUsuario);
                    dispose();
                    ListaAmigos listaAmigos = new ListaAmigos(userId, ip);
                    listaAmigos.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(SendRequestForm.this, "El usuario no existe");
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

     private void addWindowListener() {
        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ListaAmigos listaAmigos = new ListaAmigos(userId, ip);
                listaAmigos.setVisible(true);
                dispose();
            }
        };

        this.addWindowListener(windowListener);
    }   
}
