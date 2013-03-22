package storePackage.Boundary;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

//http://stackoverflow.com/questions/818287/changing-jtable-cell-color
public class CustomRendererOrderStatus extends DefaultTableCellRenderer  {
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
    {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if(table.getModel().getValueAt(row, column).toString() == "Received & Updated"){
        	c.setBackground(Color.YELLOW);
        }
        else{
        	c.setBackground(Color.WHITE);
        }
        
        return c;
    }

}
