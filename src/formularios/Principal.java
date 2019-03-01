package formularios;

import clases.Constantes;
import clases.ConsultasSQL;
import clases.FondoSwing;
import clases.MetodosSueltos;
import clases.RendererClases;
import clases.VariablesGlobales;
import es.discoduroderoer.swing.LAF;
import es.discoduroderoer.swing.Limpiar;
import es.discoduroderoer.swing.MiSwing;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Principal extends javax.swing.JFrame {

    private DefaultTableModel modelo;

    private ArrayList datosFiltro;

    public Principal() throws IOException {

        ServerSocket s = new ServerSocket(6200);
        datosFiltro = new ArrayList();
        try {
            LAF.disenoGUI();
            initComponents();

            this.dtpFI.setDate(MetodosSueltos.inicioMes());
            this.dtpFF.setDate(MetodosSueltos.finMes());

            filtro();

            this.setLocationRelativeTo(null);
            FondoSwing f;

            jMenuItem7.setIcon((new ImageIcon(Constantes.ICON_EXIT)));
            jMenuItem8.setIcon((new ImageIcon(Constantes.ICON_VIEW)));
            jMenuItem2.setIcon((new ImageIcon(Constantes.ICON_MANUAL_PAY)));
            jMenuItem5.setIcon((new ImageIcon(Constantes.ICON_NEW)));
            mitAluCrear.setIcon((new ImageIcon(Constantes.ICON_NEW)));
            jMenuItem9.setIcon((new ImageIcon(Constantes.ICON_VIEW)));

            f = new FondoSwing(Constantes.BG_PRINCIPAL);
            f.setBorder(this);

            MiSwing.iconoJFrame(this, Constantes.ICON_APP);

            MetodosSueltos.rellenarComboAlumno(cmbAlumno);
            this.cuentaActivos();
            this.buttonGroup1.add(this.rdbEstadoTodas);
            this.buttonGroup1.add(this.rdbPendientes);
            this.buttonGroup1.add(this.rdbRealizadas);

            this.buttonGroup2.add(this.rdbOrigenTodos);
            this.buttonGroup2.add(this.rdbPresencial);
            this.buttonGroup2.add(this.rdbClassgap);
            this.buttonGroup2.add(this.rdbOnline);

        } catch (IOException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void cuentaActivos() {

        try {

            VariablesGlobales.conexion.ejecutarConsulta(ConsultasSQL.ALUMNOS_ACTIVOS);

            ResultSet rs = VariablesGlobales.conexion.getResultSet();
            rs.next();
            this.txtActivos.setText(rs.getInt(1) + "");
        } catch (SQLException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void rellenarClases(String sqlAdicional) {

        String sql = ConsultasSQL.VER_CLASES + sqlAdicional + ConsultasSQL.VER_CLASES_ORDENAR;

        String sqlGanado = ConsultasSQL.GANADO_CLASES + sqlAdicional;

        modelo = MetodosSueltos.crearTableModelNoEditable();

        this.tblClases.setModel(modelo);

        this.tblClases.setDefaultRenderer(Object.class, new RendererClases());

        try {

            VariablesGlobales.conexion.ejecutarConsultaPreparada(sql, datosFiltro);
            VariablesGlobales.conexion.rellenaJTableBD(modelo);
            MiSwing.ocultarColumnaJTable(tblClases, 0);

            VariablesGlobales.conexion.ejecutarConsultaPreparada(sqlGanado, datosFiltro);
            ResultSet rs = VariablesGlobales.conexion.getResultSet();
            rs.next();
            this.txtGanado.setText(rs.getDouble(1) + " €");

            datosFiltro.clear();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void filtro() {
        String sqlAdicional = "";
        SimpleDateFormat sdf = new SimpleDateFormat(Constantes.FF_YYYY_MM_dd);
        String formatoFechaClase;

        String parentesisApertura = "";
        String parentesisCierre = "";

        if (this.dtpFF.getDate() != null && this.dtpFI.getDate() != null) {
            parentesisApertura = "(";
            parentesisCierre = ")";
        }

        boolean pendientesAct = false, inicioAct = false;

        if (this.cmbAlumno.getSelectedIndex() != 0) {
            int codigoAlumno = MiSwing.devolverCodigoComboBox(cmbAlumno);
            sqlAdicional += " and a.id = ? ";
            datosFiltro.add(codigoAlumno);

        } else {

            if (!this.rdbOrigenTodos.isSelected()) {

                sqlAdicional += " and o.nombre = ? ";
                if (this.rdbOnline.isSelected()) {
                    datosFiltro.add("Online");
                } else if (this.rdbClassgap.isSelected()) {
                    datosFiltro.add("Classgap");
                } else {
                    datosFiltro.add("Presencial");
                }

            }

        }

        if (!this.rdbEstadoTodas.isSelected()) {

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
                sqlAdicional += " or " + parentesisApertura + " c.fecha >= ? ";
            } else {
                sqlAdicional += " and c.fecha >=  ? ";
            }

            datosFiltro.add(formatoFechaClase);
            inicioAct = true;
        }

        if (this.dtpFF.getDate() != null) {
            formatoFechaClase = sdf.format(this.dtpFF.getDate());

            if (pendientesAct) {
                if (inicioAct) {
                    sqlAdicional += " and c.fecha <= ? " + parentesisCierre;
                } else {
                    sqlAdicional += " or c.fecha <= ? " + parentesisCierre;
                }

            } else {
                sqlAdicional += " and c.fecha <= ? ";
            }

            datosFiltro.add(formatoFechaClase);

        }

        if (pendientesAct) {
            sqlAdicional += ")";
        }

        rellenarClases(sqlAdicional);
    }

    private void salirApp() {
        int eleccion = JOptionPane.showConfirmDialog(this,
                "¿Estas seguro de cerrar el programa?",
                Constantes.MSG_CONFIRMAR,
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
        buttonGroup2 = new javax.swing.ButtonGroup();
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
        rdbEstadoTodas = new javax.swing.JRadioButton();
        txtGanado = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtActivos = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        rdbPresencial = new javax.swing.JRadioButton();
        rdbClassgap = new javax.swing.JRadioButton();
        rdbOrigenTodos = new javax.swing.JRadioButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        rdbOnline = new javax.swing.JRadioButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuAlu = new javax.swing.JMenu();
        mitAluCrear = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
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
        getContentPane().add(cmbAlumno, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 220, -1));
        getContentPane().add(dtpFI, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, -1, -1));
        getContentPane().add(dtpFF, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 100, -1, 20));

        btnFiltrare.setText("Filtrar");
        btnFiltrare.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrareActionPerformed(evt);
            }
        });
        getContentPane().add(btnFiltrare, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 40, 107, -1));

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Inicio");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, -1, -1));

        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Fin");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, -1, -1));

        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Alumno");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, -1, -1));

        jButton2.setText("Dar por pagada");
        getContentPane().add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 110, 110, -1));

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        getContentPane().add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 80, 110, -1));

        rdbPendientes.setText("Pendientes");
        getContentPane().add(rdbPendientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 110, -1, -1));

        rdbRealizadas.setText("Realizadas");
        getContentPane().add(rdbRealizadas, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 80, -1, -1));

        rdbEstadoTodas.setSelected(true);
        rdbEstadoTodas.setText("Todas");
        getContentPane().add(rdbEstadoTodas, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, -1, -1));

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

        rdbPresencial.setText("Presencial");
        getContentPane().add(rdbPresencial, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 110, -1, -1));

        rdbClassgap.setText("Classgap");
        getContentPane().add(rdbClassgap, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 70, -1, -1));

        rdbOrigenTodos.setSelected(true);
        rdbOrigenTodos.setText("Todos");
        getContentPane().add(rdbOrigenTodos, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 50, -1, -1));

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Estado");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 30, -1, -1));

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Origen");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 30, -1, -1));

        rdbOnline.setText("Online");
        getContentPane().add(rdbOnline, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 90, -1, -1));

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

        jMenuItem4.setText("Editar...");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

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

        jMenu1.setText("Ayuda");

        jMenuItem3.setText("Acerca de");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

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

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        AcercaDeForm ventana = new AcercaDeForm(this, true);
        ventana.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed

        Limpiar.limpiarFormulario(this.getContentPane().getComponents());

        this.dtpFI.setDate(MetodosSueltos.inicioMes());
        this.dtpFF.setDate(MetodosSueltos.finMes());

        filtro();

    }//GEN-LAST:event_btnLimpiarActionPerformed

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

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed

        if (this.tblClases.getSelectedRow() != -1) {

            int fila = this.tblClases.getSelectedRow();

            int idClase = (int) this.tblClases.getValueAt(fila, 0);

            ClasesForm ventana = new ClasesForm(this, true, idClase);
            ventana.setVisible(true);

            filtro();
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    Constantes.MSG_NO_FILA_SELECCIONADA,
                    Constantes.MSG_ERROR,
                    JOptionPane.ERROR_MESSAGE
            );

        }

    }//GEN-LAST:event_jMenuItem4ActionPerformed

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
                try {
                    new Principal().setVisible(true);
                } catch (IOException ex) {
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFiltrare;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox<String> cmbAlumno;
    private com.toedter.calendar.JDateChooser dtpFF;
    private com.toedter.calendar.JDateChooser dtpFI;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenu menuAlu;
    private javax.swing.JMenuItem mitAluCrear;
    private javax.swing.JRadioButton rdbClassgap;
    private javax.swing.JRadioButton rdbEstadoTodas;
    private javax.swing.JRadioButton rdbOnline;
    private javax.swing.JRadioButton rdbOrigenTodos;
    private javax.swing.JRadioButton rdbPendientes;
    private javax.swing.JRadioButton rdbPresencial;
    private javax.swing.JRadioButton rdbRealizadas;
    private javax.swing.JTable tblClases;
    private javax.swing.JTextField txtActivos;
    private javax.swing.JTextField txtGanado;
    // End of variables declaration//GEN-END:variables
}
