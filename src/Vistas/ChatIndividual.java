package Vistas;

import Controllers.ChatsController;
import Controllers.MessagesController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class ChatIndividual extends JFrame {

    int userId;
    int chatterId;
    String chatterName;

    private JTextArea chatArea;
    private JTextField messageField;
    private Socket socket;
    private DataOutputStream out;
    private BufferedReader in;
    private Thread listenerThread;

    MessagesController messagesController = new MessagesController();
    ChatsController chatsController = new ChatsController();

    public ChatIndividual(int userId, int chatterSeleccionadoId, String chatterSeleccionadoNombre) {
        super();
        this.userId = userId;
        this.chatterId = chatterSeleccionadoId;
        this.chatterName = chatterSeleccionadoNombre;

        try {
            socket = new Socket("192.168.0.53", 1234); // Usa la IP de tu servidor
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.writeBytes(String.valueOf(userId) + "\n"); // Enviar el ID al servidor
        } catch (IOException e) {
            
        }

        init();
        addWindowListener();
        startMessageListener(); // Iniciar el hilo para escuchar mensajes
    }

    private void init() {
        setTitle(chatterName);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.LIGHT_GRAY);

        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        chatArea = new JTextArea(20, 30);
        chatArea.setEditable(false); // Deshabilita que el área de chat se pueda editar por el usuario
        JScrollPane scrollPane = new JScrollPane(chatArea);
        messageField = new JTextField(20);
        JButton sendButton = new JButton("Send");

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(messageField)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sendButton)
                                .addGap(10, 10, 10)
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addComponent(scrollPane)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(messageField)
                                .addComponent(sendButton)
                        )
        );

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null); // centrar la ventana en la pantalla

        // listeners
        sendButton.addActionListener((ActionEvent e) -> {
            sendMessage();
        });
    }

    private void sendMessage() {
        String message = messageField.getText();
        boolean registroExitoso = false;
        if (!message.isEmpty()) {
            chatArea.append("You: " + message + "\n");
            messageField.setText("");

            int chatId = chatsController.GetChatId(userId, chatterId);
            if (chatId == -1) {
                registroExitoso = chatsController.CreateChat(userId, chatterId);
            }

            if (registroExitoso) {
                chatId = chatsController.GetChatId(userId, chatterId);
            }

            messagesController.SendMessageToServer(message, chatId, userId);
        }
    }

    private void startMessageListener() {
        listenerThread = new Thread(() -> {
            while(true)
            {
                // wait 5 seconds between each loop
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                
                // get messages from server
                
                // update chat area
                chatArea.removeAll();
            }
//            String message;
//            try {
//                while ((message = in.readLine()) != null) {
//                    String[] parts = message.split(":", 2);
//                    String flag = parts[0];
//                    String msgContent = parts[1];
//                    if (Integer.parseInt(flag) == 1) {
//                        chatArea.append("You: " + msgContent + "\n");
//                    }
//                    else{
//                        chatArea.append(msgContent + "\n");
//                    }
//                }
//            } catch (IOException e) {
//                
//            }
        });
        listenerThread.start();
    }

    private void addWindowListener() {
        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ListaConectados listasConectados = new ListaConectados(userId);
                listasConectados.setVisible(true);
                try {
                    if (socket != null) {
                        socket.close(); // Cerrar el socket al cerrar la ventana
                    }
                } catch (IOException ex) {
                    
                }
            }
        };
        this.addWindowListener(windowListener);
    }
}
