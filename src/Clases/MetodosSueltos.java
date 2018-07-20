package Clases;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MetodosSueltos {

    public static void centrarDialogo(JFrame frame, JDialog dialogo) {

        int x = (frame.getWidth() - dialogo.getWidth()) / 2;

        int y = (frame.getHeight() - dialogo.getHeight()) / 2;
        dialogo.setLocation(x, y);

    }

    public static void disenoGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            System.out.println("Error setting Java LAF: " + e);
        }
    }

    /**
     * Valida si una cadena es un email
     *
     * @param email String que contiene el valor a validar
     * @return True = es un email válido
     */
    public static boolean validar_Mail_Exp(String email) {

        return email.matches("^([\\w-]+\\.)*?[\\w-]+@[\\w-]+\\.([\\w-]+\\.)*?[\\w]+$");

    }

    /**
     * Valida si una cadena es un numero real (positivo o negativo) con un
     * numero de decimales
     *
     * @param texto String que contiene el valor a validar
     * @param num_decimales numero de decimales maximo, no puede ser menor que
     * cero
     * @return True = es un numero real
     */
    public static boolean validaNumeroRealPositivo_Exp(String texto, int num_decimales) {
        if (num_decimales > 0) {
            return texto.matches("^[0-9]+([\\.,][0-9]{1," + num_decimales + "})?$");
        } else {
            return false;
        }

    }
}
