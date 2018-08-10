/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import clases.MetodosSueltos;
import clases.VariablesGlobales;
import es.discoduroderoer.expresiones_regulares.ExpresionesRegulares;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
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

        MetodosSueltos.rellenarComboAlumno(cmbAlumno);
    }

    public void actualizarPagos() {

        String errores = "";

        Date fechaPago = this.dtpFechaPago.getDate();
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

        if (!this.dtpFechaPago.isValid()) {
            errores += "- La fecha no es válida \n";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            formatoFechaClase = sdf.format(this.dtpFechaPago.getDate());
        }

        if (errores.equals("")) {

            String sql = "select p.id_pago,"
                    + "c.precio as precio_clase, p.pagado"
                    + "from clases c, pagos p "
                    + "where c.id_clase = p.id_clase and "
                    + "c.precio>p.pagado "
                    + "order by c.fecha";

            try {
                VariablesGlobales.conexion.ejecutarConsulta(sql);

                ResultSet rs = VariablesGlobales.conexion.getResultSet();

                double precioClase, pagado, diferencia;
                int idPago;

                while (rs.next() && pago < 0) {

                    idPago = rs.getInt("id_pago");

                    precioClase = rs.getDouble("precio_clase");
                    pagado = rs.getDouble("pagado");

                    diferencia = precioClase - pagado;

                    if ((pago - diferencia) > 0) {

                        sql = "update pagos "
                                + "set fecha = '" + formatoFechaClase + "', "
                                + "pagado = " + precioClase + " "
                                + "where id_pago=" + idPago;

                        System.out.println(sql);

                        VariablesGlobales.conexion.ejecutarInstruccion(sql);

                    }

                    pago = pago - diferencia;

                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(this,
                    errores,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGuardar = new javax.swing.JButton();
        btnguardarNuevo = new javax.swing.JButton();
        dtpFechaPago = new com.toedter.calendar.JDateChooser();
        cmbAlumno = new javax.swing.JComboBox<>();
        txtPago = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        btnguardarNuevo.setText("Guardar y nuevo");
        btnguardarNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnguardarNuevoActionPerformed(evt);
            }
        });

        txtPago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPagoActionPerformed(evt);
            }
        });

        jLabel1.setText("Alumno");

        jLabel2.setText("Fecha");

        jLabel3.setText("Pago");

        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(26, 26, 26)
                                .addComponent(cmbAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(32, 32, 32)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtPago)
                                    .addComponent(dtpFechaPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(88, 88, 88))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnGuardar)
                        .addGap(18, 18, 18)
                        .addComponent(btnguardarNuevo)
                        .addGap(18, 18, 18)
                        .addComponent(btnSalir)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbAlumno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dtpFechaPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnguardarNuevo)
                    .addComponent(btnSalir))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed

        
        this.actualizarPagos();
        
        this.dispose();
        

    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnguardarNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnguardarNuevoActionPerformed
        this.actualizarPagos();
        
        //limpiar();
        
    }//GEN-LAST:event_btnguardarNuevoActionPerformed

    private void txtPagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPagoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPagoActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSalirActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnguardarNuevo;
    private javax.swing.JComboBox<String> cmbAlumno;
    private com.toedter.calendar.JDateChooser dtpFechaPago;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField txtPago;
    // End of variables declaration//GEN-END:variables
}
