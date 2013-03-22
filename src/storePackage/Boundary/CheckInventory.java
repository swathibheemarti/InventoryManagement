package storePackage.Boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import storePackage.Control.InventoryManager;
import storePackage.Entities.Item;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class CheckInventory implements ActionListener {

	JComponent chckInvComponent;
	DefaultTableModel model;
	JTable itemTable;
	JScrollPane scrollPane;
	JLabel lblInstruction;

	public CheckInventory() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		
		chckInvComponent = new JPanel();
		chckInvComponent.setLayout(null);
		
		InventoryManager invMgr = new InventoryManager();
				
		Object rowdata[][] = invMgr.ConvertIntoObjectArray(invMgr.CheckInventory()); 

		Object colnames[] = { "Product ID", "Description", "Quantity", "Price", "Supplier" };
		
		model = new DefaultTableModel(rowdata, colnames);
		itemTable = new JTable(model){
			public boolean isCellEditable(int row, int column){
				return false;
			}
			
			@Override
			public Class getColumnClass(int column){
				return String.class;
			}			
		};
		
		//itemTable.setDefaultRenderer(String.class, new CustomRendererCheckInventory());
		
		for(int rowCnt = 0; rowCnt < 10; rowCnt++){
			itemTable.setRowHeight(rowCnt, 50);
		}
		
		itemTable.setPreferredScrollableViewportSize(new Dimension(1000, 450));
	    scrollPane=new JScrollPane(itemTable);
	    //scrollPane.add(itemTable);
	    scrollPane.setBounds(50, 150, 1000, 450);
	    
	    chckInvComponent.add(scrollPane);
	    
	    lblInstruction = new JLabel("CHECK INVENTORY SCREEN");
	    Font f = new Font("Dialog", Font.BOLD, 30);
	    lblInstruction.setFont(f);
	    lblInstruction.setBounds(300,50,500,100);
	    lblInstruction.setForeground(Color.BLUE);
	    
	    chckInvComponent.add(lblInstruction);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

	
}
