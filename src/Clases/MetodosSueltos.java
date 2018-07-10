package Clases;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MetodosSueltos {
    public static void centrarDialogo(JFrame frame, JDialog dialogo){
        
        int x = (frame.getWidth() - dialogo.getWidth()) / 2;
        int y = (frame.getHeight() - dialogo.getHeight()) / 2;
        dialogo.setLocation(x, y);
        
    }
    
    public static void disenoGUI(){
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e){
            System.out.println("Error setting Java LAF: "+e);
        }
    }
}
