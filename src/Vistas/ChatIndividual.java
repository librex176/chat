package Vistas;

import Controllers.ChatsController;
import Controllers.MessagesController;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import models.Message;

public class ChatIndividual extends JFrame {

    int userId;
    int chatterId;
    String chatterName;
    String ip;

    private JTextArea chatArea;
    private JTextField messageField;
    private Socket socket;
    private DataOutputStream out;
    private BufferedReader in;
    private Thread listenerThread;

    MessagesController messagesController = new MessagesController();
    ChatsController chatsController = new ChatsController();

    public ChatIndividual(int userId, int chatterSeleccionadoId, String chatterSeleccionadoNombre, String ip) {
        super();
        this.userId = userId;
        this.chatterId = chatterSeleccionadoId;
        this.chatterName = chatterSeleccionadoNombre;
        this.ip = ip;

        try {
            socket = new Socket(ip, 1234); // Usa la IP de tu servidor
            out = new DataOutputStream(socket.getOutputStream());
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
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
        sendButton.addActionListener((ActionEvent e) -> sendMessage());
    }

    private void sendMessage() {
        String message = messageField.getText();
        boolean registroExitoso = false;
        if (!message.isEmpty()) {
            chatArea.append("You: " + message + "\n");
            messageField.setText("");

            int chatId = chatsController.GetChatId(userId, chatterId, ip);
            if (chatId == -1) {
                registroExitoso = chatsController.CreateChat(userId, chatterId, ip);
            }

            if (registroExitoso) {
                chatId = chatsController.GetChatId(userId, chatterId, ip);
            }

            messagesController.SendMessageToServer(message, chatId, userId, ip);
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
                    int chatId = chatsController.GetChatId(userId, chatterId, ip);
                    List<Message> mensajesChat = messagesController.GetMessages(chatId, ip);

                    // update chat area
                    SwingUtilities.invokeLater(() -> {
                        for (Message message : mensajesChat) {
                            int senderId = message.getUserId();
                            String messageContent = message.getMessageContent();

                            if (senderId == userId) {
                                chatArea.append("You: " + messageContent + "\n");
                            } else {
                                chatArea.append(messageContent + "\n");
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

    private void addWindowListener() {
        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ListaConectados listasConectados = new ListaConectados(userId, ip);
                listasConectados.setVisible(true);
                listenerThread.interrupt();
                closeResources();
            }
        };
        this.addWindowListener(windowListener);
    }

    private void closeResources() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}