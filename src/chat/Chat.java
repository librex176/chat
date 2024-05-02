
package chat;
import Vistas.IniciarSesion;

import javax.swing.JFrame;

/**
 *
 * @author david
 */
public class Chat {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        IniciarSesion gui = new IniciarSesion();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Definir la operación de cierre
        gui.setVisible(true); // Hacer visible la ventana JFrame
    }
    
}
