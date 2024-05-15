
package Vistas;

import Controllers.AmigosController;
import Controllers.ChatsController;
import Controllers.MessagesController;
import Controllers.UsuarioController;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import models.IndividualChatModel;

public class ListasMenu extends JFrame{
    JButton amigos, conectados, grupos, verSolicitudes, cerrarSesion;
    int userId;
    String ip;
    
    public ListasMenu(int userId, String IP) {
        super();
        this.userId  = userId;
        this.ip = IP;
        init();
    }
    
    private void init() {
        setTitle("Mis listas");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        
// crear groupLayout
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
        // establecer el auto ajuste de gaps
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        
// inicializar elementos
        // label de amigos
        amigos = new JButton("Mis Amigos");
        conectados = new JButton("Ver todos");
        grupos = new JButton("Mis grupos");
        verSolicitudes = new JButton("Ver Solicitudes");
        cerrarSesion = new JButton("Cerrar Sesion");
        
// group layouts
        
        // configurar el diseño horizontal
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(amigos, 20, 200, 400)
                .addComponent(conectados, 20, 200, 400)
                .addComponent(grupos, 20, 200, 400)
                .addComponent(verSolicitudes, 20, 200, 400)
                .addComponent(cerrarSesion, 20, 200, 400)
        );
        
        // configurar el diseño vertical
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(amigos)
                .addComponent(conectados)
                .addComponent(grupos)
                .addComponent(verSolicitudes)
                .addComponent(cerrarSesion)
        );
        
// eventos
        
        // agregar ActionListener al botón "Aceptar"
        amigos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // mandar el userId que está iniciado sesion
                ListaAmigos listAmigosView = new ListaAmigos(userId, ip);
                listAmigosView.setVisible(true);
                dispose();
            }
        });
        
        // agregar ActionListener al botón "Aceptar"
        conectados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // mandar el userId que está iniciado sesion
                ListaConectados listAmigosView = new ListaConectados(userId, ip);
                listAmigosView.setVisible(true);
                dispose();
            }
        });
        
        // agregar ActionListener al botón "Aceptar"
        grupos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // mandar el userId que está iniciado sesion
                ListaGrupos listAmigosView = new ListaGrupos(userId, ip);
                listAmigosView.setVisible(true);
                dispose();
            }
        });
        
        verSolicitudes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // mandar el userId que está iniciado sesion
                RequestsMenu View = new RequestsMenu(userId, ip);
                View.setVisible(true);
                dispose();
            }
        });
        
        cerrarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UsuarioController usuarioController = new UsuarioController(ip);
                ChatsController chatsController = new ChatsController();
                AmigosController amigosController = new AmigosController();
                MessagesController messagesController = new MessagesController();
                boolean cerradoExitosamente = usuarioController.cerrarSesion(userId); 
                
                // delete all user chats with non-friends
                List<IndividualChatModel> chatsWithUser = chatsController.SearchChats(userId, ip);
                if(chatsWithUser != null && !chatsWithUser.isEmpty())
                {
                    // for every chat where the leaving user is involved
                    for(IndividualChatModel chat : chatsWithUser)
                    {
                        System.out.println("User 1: " + chat.getChatterId1());
                        System.out.println("User 2: " + chat.getChatterId2());
                        // checks if the users involved in chat are friends
                        if(!amigosController.SearchFriends(chat.getChatterId1(), chat.getChatterId2(), ip))
                        {
                            // deletes all messages from the chat if users arent friends
                            messagesController.DeleteMessagesFromChat(chat.getChatId(), ip);
                        }
                    }
                }
                
                if (cerradoExitosamente) {
                    IniciarSesion gui = new IniciarSesion();
                    gui.setVisible(true);
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "Error al cerrar sesión"); 
                }
            }
        });
        
        pack(); // ajustar el tamaño de la ventana según el contenido
        setLocationRelativeTo(null); // centrar la ventana en la pantalla
    
    }
}
