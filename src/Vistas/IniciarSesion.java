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

public class IniciarSesion extends JFrame {
    
    private JTextField textField1 = new JTextField(10);
    private JTextField textField2 = new JTextField(10);
    private JButton IniciarSesion = new JButton("Iniciar Sesion");
    private JButton RestablecerPass = new JButton("Olvide la contraseña");
    private JButton Registrar = new JButton("Registrarme");
    private JLabel etiqueta1 = new JLabel("Usuario:");
    private JLabel etiqueta2 = new JLabel("Contraseña");
    
    public String IP = "192.168.100.76";

    
    public IniciarSesion() {
        
        super("Iniciar Sesión");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
    
        GroupLayout orden = new GroupLayout(this.getContentPane());
        orden.setAutoCreateContainerGaps(true);
        orden.setAutoCreateGaps(true);

        //Acomodo de los layout  PV
        orden.setVerticalGroup(orden.createSequentialGroup()
                .addGroup(
                    orden.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(etiqueta1)
                        .addComponent(textField1)
                        
                )
                .addGroup(
                     orden.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(etiqueta2)
                        .addComponent(textField2) 
                          
                )
                .addGroup(orden.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(IniciarSesion)
                        .addComponent(RestablecerPass)
                )
                .addGroup(orden.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(Registrar)
                        
                )
        );

        //PH
        orden.setHorizontalGroup(orden.createParallelGroup()
                .addGroup(
                    orden.createSequentialGroup()
                        .addComponent(etiqueta1)
                        .addComponent(textField1)
                        
                        
                )
                    .addGroup(
                    orden.createSequentialGroup()
                        .addComponent(etiqueta2)
                        .addComponent(textField2)
                )
                    
                .addGroup(orden.createSequentialGroup()
                        .addComponent(IniciarSesion)
                        .addComponent(RestablecerPass)
                )
                .addGroup(orden.createSequentialGroup()
                        .addComponent(Registrar)
                        
                )
                
        );

        setLayout(orden);
        this.pack();
        setLocationRelativeTo(null);
        
        Registrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                RegistrarUsuario ventanaRegistrar = new RegistrarUsuario(IP);
                ventanaRegistrar.setVisible(true);
            }
        });
        IniciarSesion.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String nombreUsuario = textField1.getText();
            String contraseña = textField2.getText();

            
            UsuarioController usuarioController = new UsuarioController();
            int usuarioId = Integer.parseInt(usuarioController.verificarCredenciales(nombreUsuario, contraseña, IP));
            //int usuarioId = usuarioController.RetornarId(nombreUsuario, contraseña);

            // Verificar si las credenciales son validas
            if (usuarioId != 0) {
                
                JOptionPane.showMessageDialog(IniciarSesion.this, "Inicio de sesion exitoso");
                usuarioController.ChangeStatus(usuarioId, true);
                ListasMenu gui = new ListasMenu(usuarioId, IP);
                gui.setVisible(true);
                // Hacer invisible esta ventana
                setVisible(false);
            } else {
                
                JOptionPane.showMessageDialog(IniciarSesion.this, "Datos incorrectos");
            }
         }
        
        });
        RestablecerPass.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            RestablecerContraseña ventanaRestablecerContraseña = new RestablecerContraseña();
            ventanaRestablecerContraseña.setVisible(true);
        }
        });
    }
}
