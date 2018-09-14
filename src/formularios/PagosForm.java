/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import clases.FondoSwing;
import clases.MetodosSueltos;
import clases.RendererPagos;
import clases.VariablesGlobales;
import es.discoduroderoer.swing.MiSwing;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Fernando
 */
public class PagosForm extends javax.swing.JDialog {

    private DefaultTableModel modelo;

    public PagosForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.dtpFI.setDate(MetodosSueltos.inicioMes());
        this.dtpFF.setDate(MetodosSueltos.finMes());

        filtro();

        this.buttonGroup1.add(this.rdbTodosPagados);
        this.buttonGroup1.add(this.rdbPagado);
        this.buttonGroup1.add(this.rdbNoPagado);

        this.buttonGroup2.add(this.rdbOrigenTodos);
        this.buttonGroup2.add(this.rdbClassgap);
        this.buttonGroup2.add(this.rdbPresencial);
        this.buttonGroup2.add(this.rdbOnline);

        MetodosSueltos.rellenarComboAlumno(cmbAlumno);
        this.setLocationRelativeTo(null);

        try {
            FondoSwing f = new FondoSwing("img/pagos.jpg");
            f.setBorder(this);
        } catch (IOException ex) {
            Logger.getLogger(AlumnoForm.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void rellenarPagos(String sqlAdicional) {

        String sqlBase = "select p.id_pago, "
                + "ifnull(strftime('%d/%m/%Y', p.fecha),'Clase no pagada')  as 'Fecha pago', "
                + "ifnull(strftime('%d/%m/%Y', c.fecha),'Pendiente de realizar') as 'Fecha clase', "
                + "a.nombre || ' ' || apellidos as Alumno, "
                + "c.precio as precio_clase, p.pagado as Pagado "
                + "from clases c, pagos p, alumnos a, origen o "
                + "where a.origen = o.id and c.id_clase = p.id_clase and a.id = c.id_alumno ";

        String sql = sqlBase + sqlAdicional + " order by p.fecha, c.fecha desc";

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.tblPagos.setModel(modelo);

        this.tblPagos.setDefaultRenderer(Object.class, new RendererPagos());

        try {
            VariablesGlobales.conexion.ejecutarConsulta(sql);
            VariablesGlobales.conexion.rellenaJTableBD(modelo);
            MiSwing.ocultarColumnaJTable(tblPagos, 0);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void filtro() {
        String sqlAdicional = "";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        String formatoFechaClase;

        boolean noPagadoAct = false, inicioAct = false;

        String parentesisApertura = "";
        String parentesisCierre = "";

        if (this.dtpFF.getDate() != null && this.dtpFI.getDate() != null) {
            parentesisApertura = "(";
            parentesisCierre = ")";
        }

        if (this.cmbAlumno.getSelectedIndex() != 0) {
            String[] filaCombobox = (String[]) (this.cmbAlumno.getSelectedItem());
            int codigoAlumno = Integer.parseInt(filaCombobox[0]);
            sqlAdicional += " and a.id = " + codigoAlumno;

        } else {
            if (!this.rdbOrigenTodos.isSelected()) {

                if (this.rdbOnline.isSelected()) {
                    sqlAdicional += " and o.nombre = 'Online'";
                } else if (this.rdbClassgap.isSelected()) {
                    sqlAdicional += " and o.nombre = 'Classgap'";
                } else {
                    sqlAdicional += " and o.nombre = 'Presencial'";
                }

            }
        }

        if (!this.rdbTodosPagados.isSelected()) {

            if (this.rdbPagado.isSelected()) {
                sqlAdicional += " and precio_clase <= p.pagado";
            } else {
                sqlAdicional += " and (precio_clase > p.pagado";
                noPagadoAct = true;
            }

        }

        if (this.dtpFI.getDate() != null) {

            formatoFechaClase = sdf.format(this.dtpFI.getDate());

            if (noPagadoAct) {
                sqlAdicional += " or " + parentesisApertura + " p.fecha >= '" + formatoFechaClase + "'";
            } else {
                sqlAdicional += " and p.fecha >= '" + formatoFechaClase + "'";
            }
            inicioAct = true;

        }

        if (this.dtpFF.getDate() != null) {
            formatoFechaClase = sdf.format(this.dtpFF.getDate());

            if (noPagadoAct) {
                if (inicioAct) {
                    sqlAdicional += " and p.fecha <= '" + formatoFechaClase + "'" + parentesisCierre;
                } else {
                    sqlAdicional += " or p.fecha <= '" + formatoFechaClase + "'" + parentesisCierre;
                }

            } else {
                sqlAdicional += " and p.fecha <= '" + formatoFechaClase + "'";
            }

        }

        if (noPagadoAct) {
            sqlAdicional += ")";
        }

        rellenarPagos(sqlAdicional);
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
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPagos = new javax.swing.JTable();
        dtpFI = new com.toedter.calendar.JDateChooser();
        dtpFF = new com.toedter.calendar.JDateChooser();
        btnfiltrar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        cmbAlumno = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        rdbNoPagado = new javax.swing.JRadioButton();
        rdbPagado = new javax.swing.JRadioButton();
        rdbTodosPagados = new javax.swing.JRadioButton();
        rdbPresencial = new javax.swing.JRadioButton();
        rdbClassgap = new javax.swing.JRadioButton();
        rdbOrigenTodos = new javax.swing.JRadioButton();
        rdbOnline = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pagos");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblPagos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblPagos);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 123, 620, 400));
        getContentPane().add(dtpFI, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 80, -1, -1));
        getContentPane().add(dtpFF, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, -1, -1));

        btnfiltrar.setText("Filtrar");
        btnfiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnfiltrarActionPerformed(evt);
            }
        });
        getContentPane().add(btnfiltrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 50, 65, -1));

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        getContentPane().add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 90, -1, -1));

        cmbAlumno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAlumno.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbAlumnoItemStateChanged(evt);
            }
        });
        getContentPane().add(cmbAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 250, -1));

        jLabel1.setText("F. fin");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 80, -1, -1));

        jLabel2.setText("F. inicio");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, -1, -1));

        jLabel3.setText("Alumno");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        rdbNoPagado.setText("No pagado");
        rdbNoPagado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbNoPagadoActionPerformed(evt);
            }
        });
        getContentPane().add(rdbNoPagado, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 80, -1, 30));

        rdbPagado.setText("Pagado");
        rdbPagado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdbPagadoActionPerformed(evt);
            }
        });
        getContentPane().add(rdbPagado, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 50, 80, 30));

        rdbTodosPagados.setSelected(true);
        rdbTodosPagados.setText("Todos");
        getContentPane().add(rdbTodosPagados, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 30, 80, -1));

        rdbPresencial.setText("Presencial");
        getContentPane().add(rdbPresencial, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 90, -1, -1));

        rdbClassgap.setText("Classgap");
        getContentPane().add(rdbClassgap, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 50, -1, -1));

        rdbOrigenTodos.setSelected(true);
        rdbOrigenTodos.setText("Todos");
        getContentPane().add(rdbOrigenTodos, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 30, -1, -1));

        rdbOnline.setText("Online");
        getContentPane().add(rdbOnline, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 70, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void rdbPagadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbPagadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdbPagadoActionPerformed

    private void btnfiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnfiltrarActionPerformed

        filtro();


    }//GEN-LAST:event_btnfiltrarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed

        MiSwing.limpiarFormulario(this.getContentPane().getComponents());
        this.rdbTodosPagados.setSelected(true);
        rellenarPagos("");


    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void rdbNoPagadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdbNoPagadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdbNoPagadoActionPerformed

    private void cmbAlumnoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbAlumnoItemStateChanged

        if (this.cmbAlumno.getSelectedIndex() != 0) {
            this.rdbOrigenTodos.setEnabled(false);
            this.rdbOnline.setEnabled(false);
            this.rdbClassgap.setEnabled(false);
            this.rdbPresencial.setEnabled(false);
        } else {
            this.rdbOrigenTodos.setEnabled(true);
            this.rdbOnline.setEnabled(true);
            this.rdbClassgap.setEnabled(true);
            this.rdbPresencial.setEnabled(true);
        }
    }//GEN-LAST:event_cmbAlumnoItemStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnfiltrar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> cmbAlumno;
    private com.toedter.calendar.JDateChooser dtpFF;
    private com.toedter.calendar.JDateChooser dtpFI;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JRadioButton rdbClassgap;
    private javax.swing.JRadioButton rdbNoPagado;
    private javax.swing.JRadioButton rdbOnline;
    private javax.swing.JRadioButton rdbOrigenTodos;
    private javax.swing.JRadioButton rdbPagado;
    private javax.swing.JRadioButton rdbPresencial;
    private javax.swing.JRadioButton rdbTodosPagados;
    private javax.swing.JTable tblPagos;
    // End of variables declaration//GEN-END:variables
}
