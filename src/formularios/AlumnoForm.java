/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import clases.FondoSwing;
import clases.MetodosSueltos;
import clases.VariablesGlobales;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import es.discoduroderoer.expresiones_regulares.ExpresionesRegulares;
import es.discoduroderoer.swing.MiSwing;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Fernando
 */
public class AlumnoForm extends javax.swing.JDialog {

    private java.awt.Frame parent;
    private int idModificar;

    private String telefonoOriginal;
    private String emailOriginal;

    //Creacion de alumno
    public AlumnoForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.parent = parent;
        this.idModificar = -1;
        inicializar();
        this.setLocationRelativeTo(null);

        FondoSwing f;
        try {
            f = new FondoSwing("img/textura-alumno.jpg");
            f.setBorder(this);
        } catch (IOException ex) {
            Logger.getLogger(AlumnoForm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //Modificacion de alumno
    public AlumnoForm(java.awt.Frame parent, boolean modal, int id) {
        super(parent, modal);
        initComponents();
        this.idModificar = id;
        btnGuadarEnviar.setVisible(false);
        inicializar();
        this.setLocationRelativeTo(null);

    }

    private void inicializar() {
        buttonGroup1.add(rdbActSi);
        buttonGroup1.add(rdbActNo);

        String sql = "select id, nombre "
                + "from Origen";

        VariablesGlobales.conexion.rellenaComboBox2Columnas(this.cmbOrigen,
                sql,
                "--Selecciona un origen",
                "id",
                "nombre");

        this.setLocationRelativeTo(parent);

        this.getRootPane().setDefaultButton(this.btnGuardar);

        if (estaModificando()) {
            sql = "select * from alumnos where id=" + this.idModificar;

            try {
                VariablesGlobales.conexion.ejecutarConsulta(sql);

                ResultSet rs = VariablesGlobales.conexion.getResultSet();

                this.txtNombre.setText(rs.getString("Nombre"));
                this.txtApellidos.setText(rs.getString("apellidos"));
                this.txtEmail.setText(rs.getString("email"));
                this.txtTelefono.setText(rs.getString("telefono"));
                this.txtPrecioBase.setText(rs.getString("precio_base"));
                this.txtPrecioDomicilio.setText(rs.getString("precio_domicilio"));

                telefonoOriginal = this.txtTelefono.getText();
                emailOriginal = this.txtEmail.getText();

                MiSwing.asignarItemCmb2Columnas(cmbOrigen, rs.getInt("Origen"));
                if (rs.getInt("activado") == 1) {
                    this.rdbActSi.setSelected(true);
                } else {
                    this.rdbActNo.setSelected(true);
                }

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }

        }

    }

    public boolean estaModificando() {
        return this.idModificar != -1;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        rdbActSi = new javax.swing.JRadioButton();
        rdbActNo = new javax.swing.JRadioButton();
        jLabel5 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnGuadarEnviar = new javax.swing.JButton();
        cmbOrigen = new javax.swing.JComboBox<>();
        txtPrecioBase = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPrecioDomicilio = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtApellidos = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Crear alumno");

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Nombre:");

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Email:");

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Telefono:");

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Origen:");

        rdbActSi.setSelected(true);
        rdbActSi.setText("Si");

        rdbActNo.setText("No");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Activado");

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnGuadarEnviar.setText("Guardar y nuevo");
        btnGuadarEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuadarEnviarActionPerformed(evt);
            }
        });

        cmbOrigen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtPrecioBase.setText("8");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Precio base:");

        txtPrecioDomicilio.setText("10");

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Precio domicilio");

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Apellidos:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btnGuardar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPrecioDomicilio, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtPrecioBase)
                                    .addComponent(txtTelefono, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtNombre)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(btnGuadarEnviar, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(cmbOrigen, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(23, 23, 23))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(70, 70, 70)
                                .addComponent(rdbActSi)
                                .addGap(18, 18, 18)
                                .addComponent(rdbActNo))
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPrecioBase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(22, 22, 22)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtPrecioDomicilio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdbActSi)
                    .addComponent(rdbActNo)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnGuadarEnviar))
                .addGap(18, 18, 18)
                .addComponent(btnSalir)
                .addContainerGap(32, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public boolean insertarUsuario() {

        String errores = "";
        String sql = "";

        String nombre = this.txtNombre.getText();

        if (nombre.equals("")) {
            errores += "- El nombre es obligatorio\n";
        }

        String apellidos = this.txtApellidos.getText();

        String telefono = this.txtTelefono.getText();
        if (!telefono.isEmpty() && !MetodosSueltos.validarTelefono(telefono)) {
            errores += "- El telefono no es valido\n";
        }

        String email = this.txtEmail.getText();
        if (!email.isEmpty() && !ExpresionesRegulares.validar_Mail_Exp(email)) {
            errores += "- El correo no es valido\n";
        }

        if (!estaModificando()) {
            sql = "select count(*) "
                    + "from alumnos "
                    + "where telefono = '" + telefono + "'";
        } else {
            sql = "select count(*) "
                    + "from alumnos "
                    + "where telefono = '" + telefono + "' and "
                    + "telefono<>'" + telefonoOriginal + "'";
        }

        if (!VariablesGlobales.conexion.consultaVacia(sql)) {
            errores += "- El telefono esta repetido\n";
        }

        if (!estaModificando()) {
            sql = "select count(*) "
                    + "from alumnos "
                    + "where email='" + email + "'";
        } else {
            sql = "select count(*) "
                    + "from alumnos "
                    + "where email='" + email + "' and "
                    + "email<>'" + emailOriginal + "'";
        }

        if (!VariablesGlobales.conexion.consultaVacia(sql)) {
            errores += "- El email esta repetido\n";
        }

        double precioBase = 0;
        if (!ExpresionesRegulares.validaNumeroRealPositivo_Exp(this.txtPrecioBase.getText(), 2)) {
            errores += "- El precio base no es válido\n";
        } else {
            precioBase = Double.parseDouble(this.txtPrecioBase.getText());
        }

        double precioDomicilio = 0;

        if (!ExpresionesRegulares.validaNumeroRealPositivo_Exp(this.txtPrecioDomicilio.getText(), 2)) {
            errores += "- El precio base no es válido\n";
        } else {
            precioDomicilio = Double.parseDouble(this.txtPrecioDomicilio.getText());
        }

        String[] origen = (String[]) (this.cmbOrigen.getSelectedItem());
        int idOrigen = Integer.parseInt(origen[0]);

        if (idOrigen == -1) {
            errores += "- Debes seleccionar un origen\n";
        }

        int activo = 0;

        if (rdbActSi.isSelected()) {
            activo = 1;
        }

        if (errores.equals("")) {

            if (estaModificando()) {
                sql = "update Alumnos set Nombre='" + nombre + "', "
                        + "apellidos = '" + apellidos + "', "
                        + "email = '" + email + "', "
                        + "telefono = '" + telefono + "', "
                        + "origen=" + idOrigen + ", "
                        + "precio_base = " + precioBase + ", "
                        + "precio_domicilio = " + precioDomicilio + ", "
                        + "activado = " + activo + " where id= " + this.idModificar;
            } else {
                sql = "insert into Alumnos(Nombre, apellidos, email, telefono, "
                        + "origen,precio_base,precio_domicilio, activado) values "
                        + "('" + nombre + "', '" + apellidos + "', '" + email + "', '" + telefono + "', "
                        + idOrigen + ", " + precioBase + ", " + precioDomicilio + ",  "
                        + activo + ");";
            }

            try {
                VariablesGlobales.conexion.ejecutarInstruccion(sql);

                if (estaModificando()) {
                    JOptionPane.showMessageDialog(this,
                            "Se ha modificado el alumno con exito",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);

                } else {
                    JOptionPane.showMessageDialog(this,
                            "Se ha creado el alumno con exito",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);
                }

                return true;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return false;
            }

        } else {

            JOptionPane.showMessageDialog(this, errores, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }

    public void limpiar() {
        this.txtNombre.setText("");
        this.txtEmail.setText("");
        this.txtTelefono.setText("");
        this.txtPrecioBase.setText("");
        this.txtPrecioDomicilio.setText("");
        this.rdbActSi.setSelected(false);
        this.cmbOrigen.setSelectedIndex(0);
    }


    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

        if (insertarUsuario()) {
            this.dispose();
        }

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnGuadarEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuadarEnviarActionPerformed
        if (insertarUsuario()) {
            MiSwing.limpiarFormulario(this.getContentPane().getComponents());
        }
    }//GEN-LAST:event_btnGuadarEnviarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed

        this.dispose();

    }//GEN-LAST:event_btnSalirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuadarEnviar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> cmbOrigen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JRadioButton rdbActNo;
    private javax.swing.JRadioButton rdbActSi;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtPrecioBase;
    private javax.swing.JTextField txtPrecioDomicilio;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
