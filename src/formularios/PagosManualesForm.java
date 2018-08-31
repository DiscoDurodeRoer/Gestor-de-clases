/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import clases.MetodosSueltos;
import clases.VariablesGlobales;
import es.discoduroderoer.expresiones_regulares.ExpresionesRegulares;
import es.discoduroderoer.swing.MiSwing;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

/**
 *
 * @author Fernando
 */
public class PagosManualesForm extends javax.swing.JDialog {

    /**
     * Creates new form PagosManualesForm
     */
    public PagosManualesForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.buttonGroup1.add(this.rdbDomicilio);
        this.buttonGroup1.add(this.rdbEspecificar);
        this.buttonGroup1.add(this.rdbPresencial);

        MetodosSueltos.rellenarComboAlumno(cmbAlumno);
        this.setLocationRelativeTo(parent);

    }

    public boolean actualizarPagos() {

        String errores = "";

        String precioText = this.txtPago.getText();
        double pago = 0;
        int codigoAlumno = -1;
        String formatoFechaClase = null;

        if (this.cmbAlumno.getSelectedIndex() == 0) {
            errores = "- Debes elegir el alumno \n";
        } else {
            String[] filaCombobox = (String[]) (this.cmbAlumno.getSelectedItem());
            codigoAlumno = Integer.parseInt(filaCombobox[0]);
        }

        if (!ExpresionesRegulares.validaNumeroRealPositivo_Exp(precioText, 2)) {
            errores = "- El formato del precio es incorrecto \n";
        } else {
            pago = Double.parseDouble(precioText);
        }

        /*System.out.println(this.dtpFechaPago.isValid());
        System.out.println(this.dtpFechaPago.getDate());
        if (!this.dtpFechaPago.isValid()) {
            errores += "- La fecha no es válida \n";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            formatoFechaClase = sdf.format(this.dtpFechaPago.getDate());
        }*/
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        formatoFechaClase = sdf.format(this.dtpFechaPago.getDate());

        if (errores.equals("")) {

            String sql = "select p.id_pago,"
                    + "c.precio as precio_clase, p.pagado "
                    + "from clases c, pagos p, alumnos a "
                    + "where c.id_clase = p.id_clase and "
                    + "a.id = c.id_Alumno "
                    + "and a.id = " + codigoAlumno
                    + " and c.precio>p.pagado "
                    + "order by c.fecha";
            System.out.println(sql);
            try {
                VariablesGlobales.conexion.ejecutarConsulta(sql);

                ResultSet rs = VariablesGlobales.conexion.getResultSet();

                double precioClase, pagado, diferencia = 0, pagoClase;
                int idPago;

                while (rs.next() && pago > 0) {

                    idPago = rs.getInt("id_pago");

                    precioClase = rs.getDouble("precio_clase");
                    pagado = rs.getDouble("pagado");

                    diferencia = precioClase - pagado;

                    if ((pago - diferencia) >= 0) {
                        pagoClase = precioClase;
                    } else {
                        pagoClase = pago;
                    }

                    sql = "update pagos "
                            + "set fecha = '" + formatoFechaClase + "', "
                            + "pagado = " + pagoClase + " "
                            + "where id_pago=" + idPago;

                    System.out.println(sql);

                    VariablesGlobales.conexion.ejecutarInstruccion(sql);

                    pago = pago - diferencia;
                }

                if (pago > 0) {

                    double precioPorClase = 0;

                    if (this.rdbPresencial.isSelected()) {
                        precioPorClase = Double.parseDouble(this.txtPrecioPresencial.getText());
                    } else if (this.rdbDomicilio.isSelected()) {
                        precioPorClase = Double.parseDouble(this.txtPrecioDomicilio.getText());
                    } else {
                        precioPorClase = Double.parseDouble(this.txtEspecificarPrecio.getText());
                    }

                    while (pago > 0) {

                        if ((pago - precioPorClase) >= 0) {
                            diferencia = precioPorClase;
                        } else {
                            diferencia = pago;
                        }

                        sql = "insert into clases "
                                + "(id_alumno, precio) values "
                                + "(" + codigoAlumno + ", " + precioPorClase + ")";

                        VariablesGlobales.conexion.ejecutarInstruccion(sql);

                        int idClase = VariablesGlobales.conexion.ultimoID("id_clase", "clases");

                        sql = "insert into pagos "
                                + "(fecha, id_clase, pagado) values "
                                + "('" + formatoFechaClase + "', " + idClase + ", " + diferencia + ")";

                        VariablesGlobales.conexion.ejecutarInstruccion(sql);

                        pago = pago - diferencia;

                    }

                }

                return true;

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    errores,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        btnGuardar = new javax.swing.JButton();
        btnguardarNuevo = new javax.swing.JButton();
        dtpFechaPago = new com.toedter.calendar.JDateChooser();
        cmbAlumno = new javax.swing.JComboBox<>();
        txtPago = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();
        rdbEspecificar = new javax.swing.JRadioButton();
        rdbPresencial = new javax.swing.JRadioButton();
        txtEspecificarPrecio = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        rdbDomicilio = new javax.swing.JRadioButton();
        txtPrecioDomicilio = new javax.swing.JTextField();
        txtPrecioPresencial = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        getContentPane().add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, -1, -1));

        btnguardarNuevo.setText("Guardar y nuevo");
        btnguardarNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarNuevoActionPerformed(evt);
            }
        });
        getContentPane().add(btnguardarNuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 330, -1, -1));
        getContentPane().add(dtpFechaPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(89, 84, 174, -1));

        cmbAlumno.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbAlumnoItemStateChanged(evt);
            }
        });
        getContentPane().add(cmbAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(89, 27, 174, -1));

        txtPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPagoActionPerformed(evt);
            }
        });
        getContentPane().add(txtPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(89, 139, 174, -1));

        jLabel1.setText("Alumno");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 30, -1, -1));

        jLabel2.setText("Fecha");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 84, -1, -1));

        jLabel3.setText("Pago");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 142, -1, -1));

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        getContentPane().add(btnSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 330, -1, -1));

        rdbEspecificar.setText("Especificar");
        rdbEspecificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbEspecificarActionPerformed(evt);
            }
        });
        getContentPane().add(rdbEspecificar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 270, -1, -1));

        rdbPresencial.setSelected(true);
        rdbPresencial.setText("Presencial");
        rdbPresencial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbPresencialActionPerformed(evt);
            }
        });
        getContentPane().add(rdbPresencial, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 190, -1, -1));

        txtEspecificarPrecio.setEditable(false);
        txtEspecificarPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEspecificarPrecioKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtEspecificarPrecioKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtEspecificarPrecioKeyTyped(evt);
            }
        });
        getContentPane().add(txtEspecificarPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 270, 50, 20));

        jLabel4.setText("Precio por clase");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, -1, -1));

        rdbDomicilio.setText("Domicilio");
        rdbDomicilio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbDomicilioActionPerformed(evt);
            }
        });
        getContentPane().add(rdbDomicilio, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 230, -1, -1));

        txtPrecioDomicilio.setEditable(false);
        txtPrecioDomicilio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioDomicilioActionPerformed(evt);
            }
        });
        getContentPane().add(txtPrecioDomicilio, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 230, 50, 20));

        txtPrecioPresencial.setEditable(false);
        getContentPane().add(txtPrecioPresencial, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 190, 50, 20));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

        if (this.actualizarPagos()) {
            this.dispose();
        }


    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnguardarNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarNuevoActionPerformed
        //this.actualizarPagos();

        MiSwing.limpiarFormulario(this.getContentPane().getComponents());

    }//GEN-LAST:event_btnguardarNuevoActionPerformed

    private void txtPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPagoActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void rdbEspecificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbEspecificarActionPerformed
        this.txtEspecificarPrecio.setEditable(true);

    }//GEN-LAST:event_rdbEspecificarActionPerformed

    private void rdbPresencialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbPresencialActionPerformed
        this.txtEspecificarPrecio.setEditable(false);

    }//GEN-LAST:event_rdbPresencialActionPerformed

    private void txtEspecificarPrecioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEspecificarPrecioKeyPressed

    }//GEN-LAST:event_txtEspecificarPrecioKeyPressed

    private void txtEspecificarPrecioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEspecificarPrecioKeyReleased

    }//GEN-LAST:event_txtEspecificarPrecioKeyReleased

    private void txtEspecificarPrecioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEspecificarPrecioKeyTyped

    }//GEN-LAST:event_txtEspecificarPrecioKeyTyped

    private void rdbDomicilioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbDomicilioActionPerformed

    }//GEN-LAST:event_rdbDomicilioActionPerformed

    private void txtPrecioDomicilioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioDomicilioActionPerformed

    }//GEN-LAST:event_txtPrecioDomicilioActionPerformed

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
    }//GEN-LAST:event_cmbAlumnoItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnguardarNuevo;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbAlumno;
    private com.toedter.calendar.JDateChooser dtpFechaPago;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JRadioButton rdbDomicilio;
    private javax.swing.JRadioButton rdbEspecificar;
    private javax.swing.JRadioButton rdbPresencial;
    private javax.swing.JTextField txtEspecificarPrecio;
    private javax.swing.JTextField txtPago;
    private javax.swing.JTextField txtPrecioDomicilio;
    private javax.swing.JTextField txtPrecioPresencial;
    // End of variables declaration//GEN-END:variables
}
