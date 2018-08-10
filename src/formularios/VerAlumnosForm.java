/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import clases.VariablesGlobales;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import es.discoduroderoer.swing.MiSwing;

/**
 *
 * @author Fernando
 */
public class VerAlumnosForm extends javax.swing.JDialog {

    private DefaultTableModel modeloTabla;
    private java.awt.Frame parent;

    private final String SQL_VER_ALUMNOS = "select a.id, a.Nombre,a.apellidos, a.email,a.telefono,o.nombre as origen "
            + "from Alumnos a, Origen o "
            + "where a.origen = o.id and a.activado = ";

    public VerAlumnosForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.parent = parent;

        rellenarAlumnos();

        this.setLocationRelativeTo(parent);

    }

    private void rellenarAlumnos() {
        String sql;
        if (chkActivo.isSelected()) {
            sql = SQL_VER_ALUMNOS + "1";
        } else {
            sql = SQL_VER_ALUMNOS + "0";
        }

        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.tblAlumnos.setModel(modeloTabla);

        try {
            VariablesGlobales.conexion.ejecutarConsulta(sql);
            VariablesGlobales.conexion.rellenaJTableBD(modeloTabla);
            MiSwing.ocultarColumnaJTable(tblAlumnos, 0);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
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

        jScrollPane1 = new javax.swing.JScrollPane();
        tblAlumnos = new javax.swing.JTable();
        btnCrear = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnActivarDesactivar = new javax.swing.JButton();
        chkActivo = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ver alumnos");

        tblAlumnos.setModel(new javax.swing.table.DefaultTableModel(
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
        tblAlumnos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tblAlumnos);

        btnCrear.setText("Crear");
        btnCrear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearActionPerformed(evt);
            }
        });

        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnActivarDesactivar.setText("Activar / desactivar");
        btnActivarDesactivar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActivarDesactivarActionPerformed(evt);
            }
        });

        chkActivo.setSelected(true);
        chkActivo.setText("Activo");
        chkActivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkActivoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkActivo)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 516, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnActivarDesactivar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCrear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(24, 24, 24))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(29, Short.MAX_VALUE)
                .addComponent(chkActivo)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCrear, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(btnActivarDesactivar, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCrearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearActionPerformed

        AlumnoForm ventana = new AlumnoForm(parent, true);
        ventana.setVisible(true);

        this.rellenarAlumnos();

    }//GEN-LAST:event_btnCrearActionPerformed

    private void btnActivarDesactivarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActivarDesactivarActionPerformed

        if (this.tblAlumnos.getRowCount() != 0) {
            if (this.tblAlumnos.getSelectedRow() != -1) {

                int activo = 1;
                if (chkActivo.isSelected()) {
                    activo = 0;
                }

                int eleccion;
                if (activo == 1) {
                    eleccion = JOptionPane.showConfirmDialog(this,
                            "¿Estas seguro de activar este alumno?",
                            "Confirmar",
                            JOptionPane.YES_NO_OPTION);
                } else {
                    eleccion = JOptionPane.showConfirmDialog(this,
                            "¿Estas seguro de desactivar este alumno?",
                            "Confirmar",
                            JOptionPane.YES_NO_OPTION);
                }

                if (eleccion == JOptionPane.YES_OPTION) {
                    int filaSeleccionada = this.tblAlumnos.getSelectedRow();

                    int idAlumno = (Integer) this.tblAlumnos.getValueAt(filaSeleccionada, 0);

                    try {
                        String sql = "update alumnos "
                                + "set activado = " + activo + " "
                                + "where id = " + idAlumno;

                        VariablesGlobales.conexion.ejecutarInstruccion(sql);

                        if (activo == 0) {
                            JOptionPane.showMessageDialog(this,
                                    "Se ha desactivado el alumno",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(this,
                                    "Se ha activado el alumno",
                                    "Éxito",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                        rellenarAlumnos();
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                    }
                }

            } else {
                JOptionPane.showMessageDialog(this,
                        "No has seleccionado la fila",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "No hay filas",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_btnActivarDesactivarActionPerformed

    private void chkActivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkActivoActionPerformed
        rellenarAlumnos();
    }//GEN-LAST:event_chkActivoActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed

        if (this.tblAlumnos.getRowCount() != 0) {
            if (this.tblAlumnos.getSelectedRow() != -1) {
                int filaSeleccionada = this.tblAlumnos.getSelectedRow();

                int idAlumno = (Integer) this.tblAlumnos.getValueAt(filaSeleccionada, 0);
                AlumnoForm form = new AlumnoForm(this.parent, true, idAlumno);
                form.setVisible(true);
                
            } else {
                JOptionPane.showMessageDialog(this,
                        "No has seleccionado la fila",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "No hay filas",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_btnModificarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActivarDesactivar;
    private javax.swing.JButton btnCrear;
    private javax.swing.JButton btnModificar;
    private javax.swing.JCheckBox chkActivo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAlumnos;
    // End of variables declaration//GEN-END:variables
}
