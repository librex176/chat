
package chat;
import Vistas.IniciarSesion;
import Vistas.ListasMenu;
import Vistas.RegistrarUsuario;
import java.sql.Connection;

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
        
        
//        ListasMenu guiMenu = new ListasMenu(1);
//        guiMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Definir la operación de cierre
//        guiMenu.setVisible(true); // Hacer visible la ventana JFrame
        
//        ListasMenu gui = new ListasMenu(1);
//        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Definir la operación de cierre
//        gui.setVisible(true); // Hacer visible la ventana JFrame

        IniciarSesion gui = new IniciarSesion();
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Definir la operación de cierre
        gui.setVisible(true); // Hacer visible la ventana JFrame

//        RegistrarUsuario gui = new RegistrarUsuario();
//        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Definir la operación de cierre
//        gui.setVisible(true); // Hacer visible la ventana JFrame

    }
    
}
