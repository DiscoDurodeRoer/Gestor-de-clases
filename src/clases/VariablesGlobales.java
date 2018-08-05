package clases;

import conexiondb.ConexionSQLite;
import java.io.File;

public class VariablesGlobales {
    public static ConexionSQLite conexion = 
            new ConexionSQLite("bd"+File.separator+"clases.db");
}
