package clases;

public class ConsultasSQL {

    // ALUMNOS 
    public static String ANIADIR_ALUMNO = "insert into Alumnos"
            + "(Nombre, apellidos, email, telefono, "
            + "origen,precio_base,precio_domicilio, activado) values "
            + "(?, ?, ?, ?, ?, ?, ?, ?);";

    public static String MODIFICAR_ALUMNO = "update Alumnos set Nombre=?, "
            + "apellidos = ?, "
            + "email = ?, "
            + "telefono = ?, "
            + "origen= ?, "
            + "precio_base = ?, "
            + "precio_domicilio = ?, "
            + "activado = ? where id= ?";

    public static String NUM_ALUMNOS_MISMO_TEL = "select count(*) "
            + "from alumnos "
            + "where telefono = ?";

    public static String NUM_ALUMNOS_MISMO_TEL_MOD = "select count(*) "
            + "from alumnos "
            + "where telefono = ? and "
            + "telefono<> ?";

    public static String NUM_ALUMNOS_MISMO_EMAIL = "select count(*) "
            + "from alumnos "
            + "where email=?";

    public static String NUM_ALUMNOS_MISMO_EMAIL_MOD = "select count(*) "
            + "from alumnos "
            + "where email=? and "
            + "email<>?";

    // CLASES
    public static String ANIADIR_CLASE = "insert into clases "
            + "(fecha, hora_inicio, hora_fin, id_alumno, precio) values "
            + "(?,?,?,?,?)";

    public static String MODIFICAR_CLASE = "update clases "
            + "set fecha= ? "
            + ", hora_inicio = ? "
            + ", hora_fin = ? "
            + ", id_alumno = ? "
            + ", precio = ? "
            + "where id_clase = ? ";

    public static String ANIADIR_CLASE_PAGOS = "insert into clases "
            + "(id_alumno, precio) values (?, ?)";

    public static String CONSULTAR_CLASE = "select * from clases where id_clase = ?";

    // PAGOS
    public static String PAGOS_ALUMNO = "select p.id_pago,"
            + "c.precio as precio_clase, p.pagado "
            + "from clases c, pagos p, alumnos a "
            + "where c.id_clase = p.id_clase and "
            + "a.id = c.id_Alumno "
            + "and a.id = ? "
            + " and c.precio>p.pagado "
            + "order by c.fecha";

    public static String ACTUALIZAR_PAGOS = "update pagos "
            + "set fecha = ?, "
            + "pagado = ? "
            + "where id_pago=?";

    public static String ANIADIR_PAGOS = "insert into pagos "
            + "(fecha, id_clase, pagado) values (?, ?, ?)";

}
