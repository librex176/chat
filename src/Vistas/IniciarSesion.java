package Vistas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class IniciarSesion extends JFrame {
    private JTextField textField1 = new JTextField(10);
    
    private JTextField textField2 = new JTextField(10);
    private JButton IniciarSesion = new JButton("Iniciar Sesion");
    private JButton RestablecerPass = new JButton("Olvide la contraseña");
    private JButton Registrar = new JButton("Registrarme");
    JLabel etiqueta1 = new JLabel("Usuario:");
    JLabel etiqueta2 = new JLabel("Contraseña");

    //Funcionalidades de los botones
    public IniciarSesion() {
        
        
        super("Iniciar Sesión");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
    
        GroupLayout orden = new GroupLayout(this.getContentPane());
        orden.setAutoCreateContainerGaps(true);
        orden.setAutoCreateGaps(true);

        // Definición de grupos secuenciales y paralelos para disposición vertical
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

        // Definición de grupos secuenciales y paralelos para disposición horizontal
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
        
        Registrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Instanciar y mostrar la ventana RegistrarUsuario
                RegistrarUsuario ventanaRegistrar = new RegistrarUsuario();
                ventanaRegistrar.setVisible(true);
            }
        });
    }
}
// despues poner un metodo aqui que cuando inicie sesion le mande a ListasMenu guiMenu = new ListasMenu(userId) -> userid del usuario que se acaba de iniciar sesion