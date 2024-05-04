package Vistas;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;

public class ChatIndividual extends JFrame {
    
    int userId;
    int chatterId;
    String chatterName;
    
    private JTextArea chatArea;
    private JTextField messageField;
    
    public ChatIndividual(int userId, int chatterSeleccionadoId, String chatterSeleccionadoNombre)
    {
        super();
        this.userId = userId;
        this.chatterId = chatterSeleccionadoId;
        this.chatterName = chatterSeleccionadoNombre;   
        init();
        addWindowListener();
    }
    
    private void init()
    {
        setTitle(chatterName);
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
        
        // listeners
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }
    
     private void sendMessage() {
        String message = messageField.getText();
        if (!message.isEmpty()) {
            chatArea.append("You: " + message + "\n");
            messageField.setText("");
        }
    }
    
    private void addWindowListener() {
        // Crear una instancia del WindowListener
        WindowListener windowListener = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Aquí puedes redirigir al usuario a la ventana ListasMenu y pasar el parámetro userId
                ListaConectados listasConectados = new ListaConectados(userId);
                listasConectados.setVisible(true);
            }
        };

        this.addWindowListener(windowListener);
    }
}
