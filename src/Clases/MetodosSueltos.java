package Clases;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MetodosSueltos {

    /**
     * Oculta una columna de una tabla
     *
     * @param tabla Tabla
     * @param columna Columna a ocultar
     */
    public static void ocultarColumnaJTable(JTable tabla, int columna) {

        tabla.getColumnModel().getColumn(columna).setMinWidth(0);

        tabla.getColumnModel().getColumn(columna).setMaxWidth(0);

        tabla.getColumnModel().getColumn(columna).setPreferredWidth(0);

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
    
    /**
     * Asigna el item segun el codigo
     *
     * @param cmb ComboBox
     * @param codigo Codigo, debe de existir dentro del combobox
     */
    public static void asignarItemCmb2Columnas(JComboBox cmb, int codigo) {

        boolean encontrado = false;

        for (int i = 1; i < cmb.getItemCount() && !encontrado; i++) {
            String[] seleccion = (String[]) cmb.getItemAt(i);

            if (codigo == Integer.parseInt(seleccion[0])) {
                cmb.setSelectedIndex(i);
                encontrado = true;
            }
        }

        if (!encontrado) {
            cmb.setSelectedIndex(0);
        }

    }
}
