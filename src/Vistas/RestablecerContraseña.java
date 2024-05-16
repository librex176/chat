
package Vistas;

import Controllers.UsuarioController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author david
 */
public class RestablecerContraseña extends JFrame {
    
    private JLabel userLabel = new JLabel("Nombre de Usuario:");
    private JTextField user = new JTextField(10);

    private JLabel preguntaLabel = new JLabel("Cual es tu canción favorita?");
    private JTextField pregunta = new JTextField(10);

    private JButton boton = new JButton("Restablecer");

    public RestablecerContraseña(String IP, int UserId) {

        super("Restablecer Contraseña");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GroupLayout orden = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(orden);
        orden.setAutoCreateContainerGaps(true);
        orden.setAutoCreateGaps(true);

        orden.setHorizontalGroup(orden.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(orden.createSequentialGroup()
                        .addComponent(userLabel)
                        .addComponent(user))
                .addGroup(orden.createSequentialGroup()
                        .addComponent(preguntaLabel)
                        .addComponent(pregunta))
                .addComponent(boton)
        );

        orden.setVerticalGroup(orden.createSequentialGroup()
                .addGroup(orden.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(userLabel)
                        .addComponent(user))
                .addGroup(orden.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(preguntaLabel)
                        .addComponent(pregunta))
                .addComponent(boton)
        );
        this.setSize(300, 200); 
        
        boton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
           String name = user.getText();
           String preg = pregunta.getText();
           
           UsuarioController usuarioController = new UsuarioController(IP);
           
           int validar = usuarioController.verificarPregunta(name, preg, IP);
           
           if (validar > 0) {
                    
                    RecuperarContraseña ventanaRecuperarContraseña = new RecuperarContraseña(IP, validar );
                    ventanaRecuperarContraseña.setVisible(true);
                } else {
                    // Mostrar mensaje de error
                    JOptionPane.showMessageDialog(RestablecerContraseña.this, "Datos ");
                }
    }
});


    }
}
