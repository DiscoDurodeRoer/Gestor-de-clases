package Clases;

import conexion.ConexionDB;
import java.io.File;

public class VariablesGlobales {
    public static ConexionDB conexion = new ConexionDB("SQLite", 
                                        "", "", "", 
                                        "bd"+File.separator+"clases.db");
}
