/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
public class RegistrarUsuario extends JFrame {
    
    private JTextField usuario = new JTextField(10);
    private JTextField pass = new JTextField(10);
    private JTextField respuestaPregunta = new JTextField(10);
    private JButton    cancelar = new JButton("Cancelar");
    private JButton     registrar = new JButton("Registrar");
    
    JLabel usuarioLabel = new JLabel("Nombre de Usuario:");
    JLabel passLabel = new JLabel("Crear Contraseña:");
    JLabel respuestaPreguntaLabel = new JLabel("¿Cual es tu cancion favorita?:");
    
    //Funcionalidades de los botonoes
    
    public RegistrarUsuario(){
        //Parametros de iniucio del formulario
        super("Registrar Usuario");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GroupLayout orden = new GroupLayout(this.getContentPane());
        orden.setAutoCreateContainerGaps(true);
        orden.setAutoCreateGaps(true);
        
        //Orden del layout
        
        //Perspectiva vertical
        orden.setVerticalGroup(
                orden.createSequentialGroup()
                 .addComponent(usuarioLabel)
                .addComponent(usuario)
                .addComponent(passLabel)
                .addComponent(pass)
                .addComponent(respuestaPreguntaLabel)
                .addComponent(respuestaPregunta)
                
        .addGroup(
                orden.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(cancelar)
                .addComponent(registrar)
        )
        
        );
        //Perspectiva Horizontal
        orden.setHorizontalGroup(
        orden.createParallelGroup()
        .addComponent(usuarioLabel)
        .addComponent(usuario)
        .addComponent(passLabel)
        .addComponent(pass)
        .addComponent(respuestaPreguntaLabel)
        .addComponent(respuestaPregunta)
        .addGroup( // Debes usar addGroup aquí
            orden.createSequentialGroup() // Llama a createSequentialGroup antes de agregar componentes
                .addComponent(cancelar)
                .addComponent(registrar)
        )
        );
        
        setLayout(orden);
        this.pack();
        
        cancelar.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            dispose();
            }
        });
        
        registrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener los datos de los campos de texto
                String nombreUsuario = usuario.getText();
                String contraseña = pass.getText();
                String cancionFavorita = respuestaPregunta.getText();

                // Instanciar el controlador de usuarios
                UsuarioController usuarioController = new UsuarioController();

                // Llamar al método para insertar el usuario en la base de datos
                boolean registroExitoso = usuarioController.insertarUsuario(nombreUsuario, contraseña, cancionFavorita);
                
                // Verificar si el registro fue exitoso
                if (registroExitoso) {
                    // Mostrar mensaje de éxito
                    JOptionPane.showMessageDialog(RegistrarUsuario.this, "¡Usuario registrado correctamente!");
                    // Cerrar la ventana
                    dispose();
                } else {
                    // Mostrar mensaje de error
                    JOptionPane.showMessageDialog(RegistrarUsuario.this, "Error al registrar usuario. Por favor, inténtelo de nuevo.");
                }
            }
        });



        
    }
    
}
