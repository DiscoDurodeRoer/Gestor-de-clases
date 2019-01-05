/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import clases.Constantes;
import clases.FondoSwing;
import clases.MetodosSueltos;
import clases.VariablesGlobales;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import es.discoduroderoer.expresiones_regulares.ExpresionesRegulares;
import es.discoduroderoer.fechas.Horas;
import es.discoduroderoer.swing.MiSwing;
import java.io.IOException;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClasesForm extends javax.swing.JDialog {

    private int idClase;

    public ClasesForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.buttonGroup1.add(this.rdbPresencial);
        this.buttonGroup1.add(this.rdbDomicilio);
        this.buttonGroup1.add(this.rdbEspecificar);

        this.buttonGroup2.add(this.rdbPagado);
        this.buttonGroup2.add(this.rdbNoPagado);

        this.setLocationRelativeTo(null);

        MetodosSueltos.rellenarComboAlumno(cmbAlumno);

        this.dtpFecha.setDate(new Date());

        this.idClase = -1;

        try {
            FondoSwing f = new FondoSwing("img/classroom.jpg");
            f.setBorder(this);
        } catch (IOException ex) {
            Logger.getLogger(AlumnoForm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Creates new form ClasesForm1
     */
    public ClasesForm(java.awt.Frame parent, boolean modal, int idClase) {
        super(parent, modal);
        initComponents();
        this.buttonGroup1.add(this.rdbPresencial);
        this.buttonGroup1.add(this.rdbDomicilio);
        this.buttonGroup1.add(this.rdbEspecificar);

        this.buttonGroup2.add(this.rdbPagado);
        this.buttonGroup2.add(this.rdbNoPagado);

        this.setLocationRelativeTo(null);

        MetodosSueltos.rellenarComboAlumno(cmbAlumno);

        this.dtpFecha.setDate(new Date());
        this.idClase = idClase;
        rellenarDatos();

        try {
            FondoSwing f = new FondoSwing("img/classroom.jpg");
            f.setBorder(this);
        } catch (IOException ex) {
            Logger.getLogger(AlumnoForm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void rellenarDatos() {

        String sql = "select * from clases where id_clase = " + idClase;

        try {
            VariablesGlobales.conexion.ejecutarConsulta(sql);
            ResultSet rs = VariablesGlobales.conexion.getResultSet();

            int idAlumno;
            String fecha = null, hi, hf;
            double precio;

            if (rs.next()) {

                idAlumno = rs.getInt("id_alumno");
                fecha = rs.getString("fecha");
                hi = rs.getString("hora_inicio");
                hf = rs.getString("hora_fin");
                precio = rs.getDouble("precio");

                Date fechaClase = new SimpleDateFormat(Constantes.FF_yyyy_mm_dd).parse(fecha);
                this.dtpFecha.setDate(fechaClase);

                this.cmbHoraInicio.setSelectedItem(hi);
                this.cmbHoraFin.setSelectedItem(hf);

                MiSwing.asignarItemCmb2Columnas(this.cmbAlumno, idAlumno);

                this.rdbEspecificar.setSelected(true);
                this.txtEspecificarPrecio.setText(precio + "");

                this.calcularPrecioFinal();
            }

        } catch (SQLException | ParseException ex) {
            Logger.getLogger(ClasesForm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void calcularPrecioFinal() {

        double precioFinal = 0;

        if (this.cmbAlumno.getSelectedIndex() != 0
                && this.cmbHoraInicio.getSelectedIndex() < this.cmbHoraFin.getSelectedIndex()) {

            String horaInicio = this.cmbHoraInicio.getSelectedItem().toString();
            String horaFin = this.cmbHoraFin.getSelectedItem().toString();

            double horasRealizadas = Horas.numeroHoras(horaInicio, horaFin);

            double precio = 0;
            if (this.rdbPresencial.isSelected()) {
                precio = Double.parseDouble(this.txtPrecioPresencial.getText());
                precioFinal = precio * horasRealizadas;
            } else if (this.rdbDomicilio.isSelected()) {
                precio = Double.parseDouble(this.txtPrecioDomicilio.getText());
                precioFinal = precio * horasRealizadas;
            } else {
                String precioEspecial = this.txtEspecificarPrecio.getText();

                if (precioEspecial.isEmpty()
                        || !ExpresionesRegulares.validaNumeroRealPositivo_Exp(precioEspecial, 2)) {
                    precio = 0;
                } else {
                    precio = Double.parseDouble(this.txtEspecificarPrecio.getText());
                }

                precioFinal = precio;

            }

        }

        this.txtPrecioFinal.setText(precioFinal + "");

    }

    private boolean guardar() {
        String errores = "";

        int codigoAlumno = 0;
        String horaInicio = null, horaFin = null;
        double precioFinal = 0;
        String formatoFechaClase = null;

        precioFinal = Double.parseDouble(this.txtPrecioFinal.getText());

        if (this.cmbAlumno.getSelectedIndex() == 0) {
            errores += "- Debes seleccionar un alumno \n";
        } else {
            String[] filaCombobox = (String[]) (this.cmbAlumno.getSelectedItem());
            codigoAlumno = Integer.parseInt(filaCombobox[0]);
        }

        if (this.dtpFecha.getDate() == null) {
            errores += "- La fecha no es válida \n";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat(Constantes.FF_YYYY_MM_dd);
            formatoFechaClase = sdf.format(this.dtpFecha.getDate());
        }

        if (this.cmbHoraInicio.getSelectedIndex() > this.cmbHoraFin.getSelectedIndex()) {
            errores += "- Las horas no estan correctas \n";
        } else {
            horaInicio = this.cmbHoraInicio.getSelectedItem().toString();
            horaFin = this.cmbHoraFin.getSelectedItem().toString();
        }

        if (precioFinal == 0) {
            errores += "- El precio final no puede ser 0. \n";
        }

        if (errores.isEmpty()) {

            if (idClase == Constantes.ANIADIR) {

                try {

                    String sql = "insert into clases "
                            + "(fecha, hora_inicio, hora_fin, "
                            + "id_alumno, precio) values "
                            + "('" + formatoFechaClase + "', '" + horaInicio + "',"
                            + "'" + horaFin + "', " + codigoAlumno + ", " + precioFinal + ")";

                    VariablesGlobales.conexion.ejecutarInstruccion(sql);

                    int idClase = VariablesGlobales.conexion.ultimoID("id_clase", "clases");

                    double pagado = 0;
                    if (this.rdbPagado.isSelected()) {
                        pagado = precioFinal;
                        sql = "insert into pagos "
                                + "(fecha, id_clase, pagado) values "
                                + "('" + formatoFechaClase + "', " + idClase + ", " + pagado + ")";
                    } else {

                        formatoFechaClase = null;
                        sql = "insert into pagos "
                                + "(id_clase, pagado) values "
                                + "(" + idClase + ", " + pagado + ")";
                    }

                    VariablesGlobales.conexion.ejecutarInstruccion(sql);

                    JOptionPane.showMessageDialog(this,
                            "Se ha insertado correctamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);

                    return true;

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "No se ha insertado correctamente:" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {

                try {
                    String sql = "update clases "
                            + "set fecha= '" + formatoFechaClase + "'"
                            + ", hora_inicio = '" + horaInicio + "'"
                            + ", hora_fin = '" + horaFin + "'"
                            + ", id_alumno = " + codigoAlumno
                            + ", precio = " + precioFinal + " "
                            + "where id_clase = " + this.idClase;

                    VariablesGlobales.conexion.ejecutarInstruccion(sql);

                    // De momento, no se edita el pago
                    
                    JOptionPane.showMessageDialog(this,
                            "Se ha actualizado correctamente",
                            "Éxito",
                            JOptionPane.INFORMATION_MESSAGE);

                    return true;

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this,
                            "No se ha insertado correctamente:" + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            }

        } else {
            JOptionPane.showMessageDialog(this,
                    errores,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        rdbEspecificar = new javax.swing.JRadioButton();
        btnGuardar = new javax.swing.JButton();
        cmbHoraInicio = new javax.swing.JComboBox<>();
        rdbPresencial = new javax.swing.JRadioButton();
        cmbHoraFin = new javax.swing.JComboBox<>();
        txtEspecificarPrecio = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnGuardarNuevo = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        dtpFecha = new com.toedter.calendar.JDateChooser();
        jLabel4 = new javax.swing.JLabel();
        cmbAlumno = new javax.swing.JComboBox<>();
        rdbDomicilio = new javax.swing.JRadioButton();
        txtPrecioFinal = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtPrecioDomicilio = new javax.swing.JTextField();
        txtPrecioPresencial = new javax.swing.JTextField();
        rdbNoPagado = new javax.swing.JRadioButton();
        rdbPagado = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Insertar clase");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Alumno");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, -1, -1));

        rdbEspecificar.setText("Especificar");
        rdbEspecificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbEspecificarActionPerformed(evt);
            }
        });
        getContentPane().add(rdbEspecificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 270, -1, -1));

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        getContentPane().add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 360, -1, -1));

        cmbHoraInicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00" }));
        cmbHoraInicio.setSelectedItem("17:00");
        cmbHoraInicio.setToolTipText("");
        cmbHoraInicio.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbHoraInicioItemStateChanged(evt);
            }
        });
        getContentPane().add(cmbHoraInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(147, 140, 101, -1));

        rdbPresencial.setSelected(true);
        rdbPresencial.setText("Presencial");
        rdbPresencial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbPresencialActionPerformed(evt);
            }
        });
        getContentPane().add(rdbPresencial, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 190, -1, -1));

        cmbHoraFin.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00" }));
        cmbHoraFin.setSelectedIndex(2);
        cmbHoraFin.setSelectedItem("19:00");
        cmbHoraFin.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbHoraFinItemStateChanged(evt);
            }
        });
        getContentPane().add(cmbHoraFin, new org.netbeans.lib.awtextra.AbsoluteConstraints(322, 140, 101, -1));

        txtEspecificarPrecio.setEditable(false);
        txtEspecificarPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEspecificarPrecioKeyReleased(evt);
            }
        });
        getContentPane().add(txtEspecificarPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 270, 50, 20));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Hora fin");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(266, 143, -1, -1));

        btnGuardarNuevo.setText("Guardar y nuevo");
        btnGuardarNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarNuevoActionPerformed(evt);
            }
        });
        getContentPane().add(btnGuardarNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 360, -1, -1));

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 360, 83, -1));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Fecha");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 100, -1, -1));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Hora inicio");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 143, -1, -1));
        getContentPane().add(dtpFecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 112, -1));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Precio");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 196, -1, -1));

        cmbAlumno.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbAlumnoItemStateChanged(evt);
            }
        });
        cmbAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAlumnoActionPerformed(evt);
            }
        });
        getContentPane().add(cmbAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(147, 47, 254, -1));

        rdbDomicilio.setText("Domicilio");
        rdbDomicilio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbDomicilioActionPerformed(evt);
            }
        });
        getContentPane().add(rdbDomicilio, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 230, -1, -1));

        txtPrecioFinal.setEditable(false);
        txtPrecioFinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioFinalActionPerformed(evt);
            }
        });
        getContentPane().add(txtPrecioFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 230, 115, 20));

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Precio final");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 190, 110, 20));

        txtPrecioDomicilio.setEditable(false);
        getContentPane().add(txtPrecioDomicilio, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 230, 50, 20));

        txtPrecioPresencial.setEditable(false);
        getContentPane().add(txtPrecioPresencial, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 190, 50, 20));

        rdbNoPagado.setSelected(true);
        rdbNoPagado.setText("No pagado");
        getContentPane().add(rdbNoPagado, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 310, -1, -1));

        rdbPagado.setText("Pagado");
        getContentPane().add(rdbPagado, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 310, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

        if (guardar()) {
            this.dispose();
        }

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void cmbAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAlumnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbAlumnoActionPerformed

    private void txtPrecioFinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioFinalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPrecioFinalActionPerformed

    private void rdbDomicilioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbDomicilioActionPerformed
        this.txtEspecificarPrecio.setEditable(false);
        this.calcularPrecioFinal();
    }//GEN-LAST:event_rdbDomicilioActionPerformed

    private void cmbAlumnoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbAlumnoItemStateChanged

        String[] filaCombobox = (String[]) (this.cmbAlumno.getSelectedItem());
        int codigoAlumno = Integer.parseInt(filaCombobox[0]);

        double precioPresencial = VariablesGlobales.conexion.devolverValorDouble("precio_base",
                "alumnos",
                "id = " + codigoAlumno);

        double precioDomicilio = VariablesGlobales.conexion.devolverValorDouble("precio_domicilio",
                "alumnos",
                "id = " + codigoAlumno);

        this.txtPrecioPresencial.setText(precioPresencial + "");
        this.txtPrecioDomicilio.setText(precioDomicilio + "");

        this.calcularPrecioFinal();

    }//GEN-LAST:event_cmbAlumnoItemStateChanged

    private void rdbEspecificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbEspecificarActionPerformed
        this.txtEspecificarPrecio.setEditable(true);
        this.calcularPrecioFinal();
    }//GEN-LAST:event_rdbEspecificarActionPerformed

    private void cmbHoraInicioItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHoraInicioItemStateChanged
        this.calcularPrecioFinal();

        int indiceActual = this.cmbHoraInicio.getSelectedIndex();
        int indiceFin;
        if ((indiceActual + 4) >= this.cmbHoraInicio.getItemCount()) {
            indiceFin = this.cmbHoraInicio.getItemCount() - 1;
        } else {
            indiceFin = indiceActual + 4;
        }

        this.cmbHoraFin.setSelectedIndex(indiceFin);


    }//GEN-LAST:event_cmbHoraInicioItemStateChanged

    private void cmbHoraFinItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbHoraFinItemStateChanged
        this.calcularPrecioFinal();
    }//GEN-LAST:event_cmbHoraFinItemStateChanged

    private void rdbPresencialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbPresencialActionPerformed
        this.txtEspecificarPrecio.setEditable(false);
        this.calcularPrecioFinal();
    }//GEN-LAST:event_rdbPresencialActionPerformed

    private void txtEspecificarPrecioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEspecificarPrecioKeyReleased
        this.calcularPrecioFinal();
    }//GEN-LAST:event_txtEspecificarPrecioKeyReleased

    private void btnGuardarNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarNuevoActionPerformed

        if (guardar()) {
            MiSwing.limpiarFormulario(this.getContentPane().getComponents());
            this.cmbHoraInicio.setSelectedIndex(13);
            this.cmbHoraInicio.setSelectedIndex(17);
        }


    }//GEN-LAST:event_btnGuardarNuevoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnGuardarNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> cmbAlumno;
    private javax.swing.JComboBox<String> cmbHoraFin;
    private javax.swing.JComboBox<String> cmbHoraInicio;
    private com.toedter.calendar.JDateChooser dtpFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JRadioButton rdbDomicilio;
    private javax.swing.JRadioButton rdbEspecificar;
    private javax.swing.JRadioButton rdbNoPagado;
    private javax.swing.JRadioButton rdbPagado;
    private javax.swing.JRadioButton rdbPresencial;
    private javax.swing.JTextField txtEspecificarPrecio;
    private javax.swing.JTextField txtPrecioDomicilio;
    private javax.swing.JTextField txtPrecioFinal;
    private javax.swing.JTextField txtPrecioPresencial;
    // End of variables declaration//GEN-END:variables
}
