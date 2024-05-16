package Vistas;

import Controllers.UsuarioController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Clase para la ventana de recuperación de contraseña
 * 
 * author david
 */
public class RecuperarContraseña extends JFrame {
    
    private JButton botonVer = new JButton("Ver contraseña");
    
    public RecuperarContraseña(String Ip, int UserId){
        
        super("Restablecer Contraseña");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        UsuarioController control = new UsuarioController(Ip);
        
        GroupLayout orden = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(orden);
        orden.setAutoCreateContainerGaps(true);
        orden.setAutoCreateGaps(true);
        
        orden.setHorizontalGroup(orden.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addComponent(botonVer)
        );

        orden.setVerticalGroup(orden.createSequentialGroup()
            .addGroup(orden.createSequentialGroup()
                .addComponent(botonVer))
        );
        
        this.setSize(300, 200); 
        
        botonVer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String contraseña = control.verPass(UserId);
                JOptionPane.showMessageDialog(RecuperarContraseña.this, contraseña);
                System.out.println(UserId);
            }
        });
    }
}
