
package Vistas;

import Controllers.GruposController;
import Controllers.MessagesController;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import models.GroupMessage;
import models.Message;
import Controllers.AmigosController;

public class ChatGrupal extends JFrame{
    
    int userId;
    int groupId;
    String ip;
    private JTextArea chatArea;
    private JTextField messageField;
    private Thread listenerThread;
    
    MessagesController messagesController = new MessagesController();
    
    public ChatGrupal(int userId, int groupId, String ip) {
        super();
        this.userId = userId; //Quien manda el mensaje
        this.groupId = groupId;
        this.ip = ip;
        init();
        addWindowListener();
        startMessageListener();
    }
    
    private void init() {
        GruposController gruposController = new GruposController(ip);
        String nombreGrupo = gruposController.selectNombreGrupo(groupId);
        setTitle(nombreGrupo);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.LIGHT_GRAY);
        
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        
        chatArea = new JTextArea(20, 30);
        
        // Deshabilita que el area de chat se pueda editar por el usuario
        chatArea.setEditable(false); 
        
        JScrollPane scrollPane = new JScrollPane(chatArea);
        messageField = new JTextField(20);
        JButton sendButton = new JButton("Send");
        JButton exitButton = new JButton("Exit group");
        JButton actions = new JButton("i");
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(exitButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(messageField)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sendButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(actions)
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(scrollPane)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(exitButton)
                                .addComponent(messageField)
                                .addComponent(sendButton)
                                .addComponent(actions)
                        )
        );
        
        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null); // centrar la ventana en la pantalla
        
        // listeners
        sendButton.addActionListener((ActionEvent e) -> {
            sendMessage();
        });
        exitButton.addActionListener((ActionEvent e) -> {
            exitGroup(groupId, userId);
        });
        actions.addActionListener((ActionEvent e) -> {
            mandarAjustesGrupos(groupId);
        });
    }
    
    private void sendMessage() {
        String message = messageField.getText();
        System.out.println(message);
        boolean registroExitoso = false;
        if (!message.isEmpty()) {
            chatArea.append("You: " + message + "\n");
            messageField.setText("");
            
            messagesController.SendMessageToServerGroups(message, groupId, userId, ip);
        }
    }

    private void startMessageListener() {
        listenerThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(1500);

                    // clear chat area before updating it
                    SwingUtilities.invokeLater(() -> {
                        chatArea.setText("");
                    });

                    // get messages from server with query (getMessages)
                    List<GroupMessage> mensajesChatGrupales = messagesController.GetMessagesGroup(groupId, ip);

                    // update chat area
                    SwingUtilities.invokeLater(() -> {
                        for (GroupMessage groupMessage : mensajesChatGrupales) {
                            int senderId = groupMessage.getUserId();
                            String messageContent = groupMessage.getMessageContent();

                            if (senderId == userId) {
                                chatArea.append("You: " + messageContent + "\n");
                            } else {
                                AmigosController amigos = new AmigosController();
                                String Sender = amigos.selectNameByUserId(senderId,ip);
                                chatArea.append(Sender + ": " + messageContent + "\n");
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore the interrupted status
                    break; // Exit the loop if interrupted
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        listenerThread.start();
    }

    private void exitGroup(int grupoId, int userId) {
        GruposController gruposController = new GruposController(ip);
        
        boolean isOwner = gruposController.selectDuenoId(grupoId, userId);
        if (isOwner) {
            int response = JOptionPane.showConfirmDialog(
                ChatGrupal.this,
                "¿Estás seguro de que deseas salir del grupo? Borrará todo debido a que eres el dueño.",
                "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {
                gruposController.deleteMensajesGrupos(grupoId);
                boolean comprobar = gruposController.deleteInvitacionesGrupos(grupoId);
                if (comprobar) {
                    gruposController.deleteGrupo(grupoId);
                    dispose();
                    ListaGrupos listasGrupos = new ListaGrupos(userId, ip);
                    listasGrupos.setVisible(true);
                }
            }
        } else {
            int response = JOptionPane.showConfirmDialog(
                ChatGrupal.this,
                "¿Estás seguro de que deseas salir de este grupo?",
                "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (response == JOptionPane.YES_OPTION) {
                int cantidadParticipantes = gruposController.selectCuentaParticipantes(grupoId);
                System.out.println("participantes es: " + cantidadParticipantes);
                if (cantidadParticipantes <= 2) {
                    System.out.println("entra minimo participantes");
                    gruposController.deleteMensajesGrupos(grupoId);
                    boolean comprobar = gruposController.deleteInvitacionesGrupos(grupoId);
                    if (comprobar) {
                        System.out.println("comprobacion");
                        gruposController.deleteGrupo(grupoId);
                    }
                } else {
                    /*System.out.println("entra solo el solito");
                    gruposController.deleteUsuarioRecibeId(grupoId, userId);*/
                }
                dispose();
                ListaGrupos view = new ListaGrupos(userId, ip);
                view.setVisible(true);
            }
        }
    }
    
    private void mandarAjustesGrupos(int grupoId) {
        GruposController gruposController = new GruposController(ip);
        AjustesGrupos view = new AjustesGrupos(userId, groupId, ip);
                view.setVisible(true);
                dispose();
    }
    
    private void addWindowListener() {
        // Crear una instancia del WindowListener
        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Aquí puedes redirigir al usuario a la ventana ListasMenu y pasar el parámetro userId
                ListaGrupos listasConectados = new ListaGrupos(userId, ip);
                listasConectados.setVisible(true);
            }
        };

        this.addWindowListener(windowListener);
    }
}
