package storePackage.Boundary;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomRendererCheckInventory extends DefaultTableCellRenderer {
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if(column == 5 && Integer.parseInt(table.getModel().getValueAt(row, 2).toString()) < 5){
        	c.setBackground(Color.YELLOW);
        }
        else{
        	c.setBackground(Color.WHITE);
        }
        
        return c;
    }

}
