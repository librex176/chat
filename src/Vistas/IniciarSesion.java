package Vistas;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class IniciarSesion extends JFrame {
    private JTextField textField1 = new JTextField(10);
    
    private JTextField textField2 = new JTextField(10);
    private JButton button1 = new JButton("Iniciar Sesion");
    private JButton button2 = new JButton("Olvide la contraseña");
    private JButton button3 = new JButton("Registrarme");
    JLabel etiqueta1 = new JLabel("Usuario:");
    JLabel etiqueta2 = new JLabel("Contraseña");

    public IniciarSesion() {
        
        super("Iniciar Sesión");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
    
        GroupLayout orden = new GroupLayout(this.getContentPane());
        orden.setAutoCreateContainerGaps(true);
        orden.setAutoCreateGaps(true);

        // Definición de grupos secuenciales y paralelos para disposición vertical
        orden.setVerticalGroup(
            orden.createSequentialGroup()
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
                .addGroup(
                    orden.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(button1)
                        .addComponent(button2)
                )
                .addGroup(
                    orden.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(button3)
                        
                )
        );

        // Definición de grupos secuenciales y paralelos para disposición horizontal
        orden.setHorizontalGroup(
            orden.createParallelGroup()
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
                    
                .addGroup(
                    orden.createSequentialGroup()
                        .addComponent(button1)
                        .addComponent(button2)
                )
                .addGroup(
                    orden.createSequentialGroup()
                        .addComponent(button3)
                        
                )
                
        );

        setLayout(orden);
        this.pack();
    }
}
// despues poner un metodo aqui que cuando inicie sesion le mande a ListasMenu guiMenu = new ListasMenu(userId) -> userid del usuario que se acaba de iniciar sesion