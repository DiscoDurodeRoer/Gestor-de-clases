/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import es.discoduroderoer.fechas.Dias;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

public class MetodosSueltos {

    public static void rellenarComboAlumno(JComboBox cmbAlumno) {

        String sql = "Select id, nombre || ' ' || apellidos as nombreC "
                + "from alumnos "
                + "where activado = 1 "
                + "order by nombreC";
        VariablesGlobales.conexion.rellenaComboBox2Columnas(cmbAlumno,
                sql,
                "--Selecciona un alumno",
                "id",
                "nombreC");

    }

    public static void enlace(String enlaceAAceder) {
        Desktop enlace = Desktop.getDesktop();
        try {
            enlace.browse(new URI(enlaceAAceder));
        } catch (IOException | URISyntaxException e) {
        }
    }

    public static Date inicioMes() {

        Date d = new Date();

        return new Date(d.getYear(), d.getMonth(), 1);

    }

    public static Date finMes() {

        Date d = new Date();

        int dias = Dias.numeroDeDiasMes(d.getMonth() + 1);

        return new Date(d.getYear(), d.getMonth(), dias);
    }

    public static boolean validarTelefono(String texto) {
        return texto.matches("[0-9]{9}");
    }

    public static DefaultTableModel crearTableModelNoEditable() {
        return new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

    }


}
