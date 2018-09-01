/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class RendererPagos extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        double pago_clase = (Double) table.getValueAt(row, 4);
        double pagado = (Double) table.getValueAt(row, 5);

        if (pagado == 0) {
            setBackground(new Color(250, 88, 88));
            setForeground(Color.BLACK);
        } else if (pagado < pago_clase) {
            setBackground(new Color(243, 247, 129));
            setForeground(Color.BLACK);
        } else {
            setBackground(new Color(129, 247, 129));
            setForeground(Color.BLACK);

        }

        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); //To change body of generated methods, choose Tools | Templates.
    }

}
