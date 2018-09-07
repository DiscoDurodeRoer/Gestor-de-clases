/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import clases.FondoSwing;
import clases.MetodosSueltos;
import clases.RendererClases;
import clases.VariablesGlobales;
import es.discoduroderoer.swing.LAF;
import es.discoduroderoer.swing.MiSwing;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Principal extends javax.swing.JFrame {

    private DefaultTableModel modelo;

    public Principal() {
        try {
            LAF.disenoGUI();
            initComponents();
            rellenarClases("");
            this.setLocationRelativeTo(null);
            FondoSwing f;

            f = new FondoSwing("img/textura-principal.jpg");
            f.setBorder(this);

            MetodosSueltos.rellenarComboAlumno(cmbAlumno);
            this.cuentaActivos();
            this.buttonGroup1.add(this.rdbTodas);
            this.buttonGroup1.add(this.rdbPendientes);
            this.buttonGroup1.add(this.rdbRealizadas);

        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void cuentaActivos() {

        try {
            String sqlActivos = "select count(*) as num_alumnos "
                    + "from alumnos "
                    + "where activado = 1";
            VariablesGlobales.conexion.ejecutarConsulta(sqlActivos);

            ResultSet rs = VariablesGlobales.conexion.getResultSet();
            rs.next();
            this.txtActivos.setText(rs.getInt(1) + "");
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void rellenarClases(String sqlAdicional) {

        String sqlBase = "select id_clase, nombre || ' ' || apellidos as Alumno, "
                + " ifnull(strftime('%d/%m/%Y', fecha), 'Pendiente de realizar') as 'Fecha clase', "
                + "hora_inicio as 'H. inicio', "
                + "hora_fin as 'H. fin', Precio "
                + "from clases c, alumnos a "
                + "where c.id_alumno = a.id";

        String sql = sqlBase + sqlAdicional + " order by fecha, hora_inicio,hora_fin ";

        String sqlGanado = "select sum(p.pagado) "
                + " from clases c, pagos p, alumnos a "
                + " where c.id_clase = p.id_clase and a.id = c.id_alumno ";

        sqlGanado = sqlGanado + sqlAdicional;

        modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.tblClases.setModel(modelo);

        this.tblClases.setDefaultRenderer(Object.class, new RendererClases());

        try {
            VariablesGlobales.conexion.ejecutarConsulta(sql);
            VariablesGlobales.conexion.rellenaJTableBD(modelo);
            MiSwing.ocultarColumnaJTable(tblClases, 0);

            VariablesGlobales.conexion.ejecutarConsulta(sqlGanado);
            ResultSet rs = VariablesGlobales.conexion.getResultSet();
            rs.next();
            this.txtGanado.setText(rs.getDouble(1) + " €");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void filtro() {
        String sqlAdicional = "";
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
        String formatoFechaClase;

        String parentesisApertura = "";
        String parentesisCierre = "";

        if (this.dtpFF.getDate() != null && this.dtpFI.getDate() != null) {
            parentesisApertura = "(";
            parentesisCierre = ")";
        }

        boolean pendientesAct = false, inicioAct = false;

        if (this.cmbAlumno.getSelectedIndex() != 0) {
            String[] filaCombobox = (String[]) (this.cmbAlumno.getSelectedItem());
            int codigoAlumno = Integer.parseInt(filaCombobox[0]);
            sqlAdicional += " and a.id = " + codigoAlumno;

        }

        if (!this.rdbTodas.isSelected()) {

            if (this.rdbRealizadas.isSelected()) {
                sqlAdicional += " and c.fecha is not null";
            } else {
                sqlAdicional += " and (c.fecha is null";
                pendientesAct = true;
            }

        }

        if (this.dtpFI.getDate() != null) {

            formatoFechaClase = sdf.format(this.dtpFI.getDate());

            if (pendientesAct) {
                sqlAdicional += " or " + parentesisApertura + " c.fecha >=  '" + formatoFechaClase + "'";
            } else {
                sqlAdicional += " and c.fecha >=  '" + formatoFechaClase + "'";
            }
            inicioAct = true;
        }

        if (this.dtpFF.getDate() != null) {
            formatoFechaClase = sdf.format(this.dtpFF.getDate());

            if (pendientesAct) {
                if (inicioAct) {
                    sqlAdicional += " and c.fecha <='" + formatoFechaClase + "' " + parentesisCierre;
                } else {
                    sqlAdicional += " or c.fecha <='" + formatoFechaClase + "' " + parentesisCierre;
                }

            } else {
                sqlAdicional += " and c.fecha <='" + formatoFechaClase + "'";
            }

        }

        if (pendientesAct) {
            sqlAdicional += ")";
        }

        rellenarClases(sqlAdicional);
    }

    private void salirApp() {
        int eleccion = JOptionPane.showConfirmDialog(this,
                "¿Estas seguro de cerrar el programa?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (eleccion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblClases = new javax.swing.JTable();
        cmbAlumno = new javax.swing.JComboBox<>();
        dtpFI = new com.toedter.calendar.JDateChooser();
        dtpFF = new com.toedter.calendar.JDateChooser();
        btnFiltrare = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        rdbPendientes = new javax.swing.JRadioButton();
        rdbRealizadas = new javax.swing.JRadioButton();
        rdbTodas = new javax.swing.JRadioButton();
        txtGanado = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtActivos = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuAlu = new javax.swing.JMenu();
        mitAluCrear = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        jMenuItem6.setText("jMenuItem6");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Gestor de clases");
        setMinimumSize(new java.awt.Dimension(600, 550));
        setPreferredSize(new java.awt.Dimension(700, 600));
        setResizable(false);
        setSize(new java.awt.Dimension(700, 600));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tblClases.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblClases);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 140, 633, 274));

        cmbAlumno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbAlumno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbAlumnoActionPerformed(evt);
            }
        });
        getContentPane().add(cmbAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 220, -1));
        getContentPane().add(dtpFI, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, -1, -1));
        getContentPane().add(dtpFF, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 100, -1, 20));

        btnFiltrare.setText("Filtrar");
        btnFiltrare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrareActionPerformed(evt);
            }
        });
        getContentPane().add(btnFiltrare, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 40, 107, -1));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Inicio");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 100, -1, -1));

        jLabel2.setText("Fin");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, -1, -1));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Alumno");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 60, -1, -1));

        jButton2.setText("Dar por pagada");
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 110, 110, -1));

        btnLimpiar.setText("Limpiar");
        getContentPane().add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 80, 110, -1));

        rdbPendientes.setText("Pendientes");
        getContentPane().add(rdbPendientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 100, -1, -1));

        rdbRealizadas.setText("Realizadas");
        getContentPane().add(rdbRealizadas, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 70, -1, -1));

        rdbTodas.setSelected(true);
        rdbTodas.setText("Todas");
        getContentPane().add(rdbTodas, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 40, -1, -1));

        txtGanado.setEditable(false);
        txtGanado.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        getContentPane().add(txtGanado, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 430, 380, 30));

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Ganado");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 50, 30));

        txtActivos.setEditable(false);
        txtActivos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        getContentPane().add(txtActivos, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 430, 70, 30));

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Alumnos activos");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 430, 90, 30));

        menuAlu.setText("Alumno");

        mitAluCrear.setText("Crear...");
        mitAluCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mitAluCrearActionPerformed(evt);
            }
        });
        menuAlu.add(mitAluCrear);

        jMenuItem9.setText("Ver...");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        menuAlu.add(jMenuItem9);

        jMenuBar1.add(menuAlu);

        jMenu2.setText("Clases");

        jMenuItem5.setText("Nuevo...");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Pagos");

        jMenuItem8.setText("Ver pagos...");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem8);

        jMenuItem2.setText("Pago manual...");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuBar1.add(jMenu3);

        jMenu4.setText("Salir");

        jMenuItem7.setText("Salir del sistema");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mitAluCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mitAluCrearActionPerformed

        AlumnoForm ventana = new AlumnoForm(this, true);
        ventana.setVisible(true);

        MetodosSueltos.rellenarComboAlumno(cmbAlumno);

        filtro();

        cuentaActivos();

    }//GEN-LAST:event_mitAluCrearActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        salirApp();
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        VerAlumnosForm ventana = new VerAlumnosForm(this, true);
        ventana.setVisible(true);

        MetodosSueltos.rellenarComboAlumno(cmbAlumno);

        filtro();

        cuentaActivos();

    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed

        ClasesForm ventana = new ClasesForm(this, true);
        ventana.setVisible(true);

        filtro();


    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void cmbAlumnoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbAlumnoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbAlumnoActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        PagosForm ventana = new PagosForm(this, true);
        ventana.setVisible(true);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        PagosManualesForm ventana = new PagosManualesForm(this, true);
        ventana.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void btnFiltrareActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrareActionPerformed

        filtro();


    }//GEN-LAST:event_btnFiltrareActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing

        salirApp();

    }//GEN-LAST:event_formWindowClosing

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Principal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFiltrare;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbAlumno;
    private com.toedter.calendar.JDateChooser dtpFF;
    private com.toedter.calendar.JDateChooser dtpFI;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenu menuAlu;
    private javax.swing.JMenuItem mitAluCrear;
    private javax.swing.JRadioButton rdbPendientes;
    private javax.swing.JRadioButton rdbRealizadas;
    private javax.swing.JRadioButton rdbTodas;
    private javax.swing.JTable tblClases;
    private javax.swing.JTextField txtActivos;
    private javax.swing.JTextField txtGanado;
    // End of variables declaration//GEN-END:variables
}
