/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import javax.swing.JComboBox;

public class MetodosSueltos {
 
    public static void rellenarComboAlumno(JComboBox cmbAlumno) {

        String sql = "Select id, nombre || ' ' || apellidos as nombreC "
                + "from alumnos "
                + "where activado = 1";
        VariablesGlobales.conexion.rellenaComboBox2Columnas(cmbAlumno,
                sql,
                "--Selecciona un alumno",
                "id",
                "nombreC");

    }
    
}
