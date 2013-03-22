package storePackage.Boundary;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import storePackage.Control.InventoryManager;
import storePackage.Control.OrderManager;
import storePackage.Entities.Order;

public class Orders implements ActionListener {
	JComponent OrderInventoryComponent;
	DefaultTableModel model;
	JTable itemTable;
	JScrollPane scrollPane;
	JLabel lblSubtotal, lblTax, lblTotal, lblInstruction;

	JButton btnsubmit, btnCancel, btnUpdate;
	JTextField txtSubtotal, txtTax, txtTotal;
	ImageIcon icon1, icon2, icon3, icon4, icon5;
	double doubleSubTotal = 0;
	String vals;
	int b;

	public Orders() throws XPathExpressionException,
			ParserConfigurationException, SAXException, IOException {
		
		OrderInventoryComponent = new JPanel();
		OrderInventoryComponent.setLayout(null);
		
		// JTable Begins
		String[] itemColumnNames = { "Select", "Item ID", "Item Name", "InStock",
				                     "Price", "Supplier Name", "Supplier ID", "Quantity" };
		
		InventoryManager iMngr = new InventoryManager();
		Object[][] itemData = iMngr.ConvertIntoObjectArray(iMngr.CheckInventory());
		
		int rowLen, colLen;
		rowLen = itemData.length;
		colLen = itemData[0].length;
		
		Object[][] newItemData = new Object[rowLen][colLen + 2];
		
		for (int i = 0; i < rowLen; i++) {
			newItemData[i][0] = false;
			for (int j = 1; j <= colLen; j++) {
				newItemData[i][j] = itemData[i][j - 1];
			}
			newItemData[i][7] = "";
		}

		model = new DefaultTableModel(newItemData, itemColumnNames);
		
		itemTable = new JTable(model) {

			@Override
			public boolean isCellEditable(int row, int column) {

				Boolean result = false;
				
				if(column == 0){
					result = true;
				}
				
				if(column == 7){
					
					Object o = getValueAt(row, 3);
					
					if (o != null && Double.parseDouble(o.toString()) < 10.0){
						result = true;
					}										
				}
			
				return result;				
			}

			@Override
			public Class getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}
		};

		itemTable.setDefaultRenderer(String.class, new CustomRendererOrderInventory());

		for (int rowCnt = 0; rowCnt < 7; rowCnt++) {
			itemTable.setRowHeight(rowCnt, 75);
		}

		itemTable.setPreferredScrollableViewportSize(new Dimension(1000, 450));
		scrollPane = new JScrollPane(itemTable);
		// scrollPane.add(itemTable);
		scrollPane.setBounds(50, 50, 1000, 450);
		OrderInventoryComponent.add(scrollPane);
		itemTable.setColumnSelectionAllowed(true);

		lblInstruction = new JLabel("ORDER INVENTORY");
		Font f = new Font("Dialog", Font.BOLD, 25);
		lblInstruction.setFont(f);
		lblInstruction.setBounds(400, 10, 500, 50);
		lblInstruction.setForeground(Color.BLUE);
		OrderInventoryComponent.add(lblInstruction);

		itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// JTable Ends

		btnsubmit = new JButton("Submit Order");
		btnsubmit.addActionListener(this);
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);

		btnsubmit.setBounds(700, 650, 150, 20);
		btnCancel.setBounds(870, 650, 90, 20);
		btnsubmit.setVisible(true);
		btnCancel.setVisible(true);

		OrderInventoryComponent.add(btnsubmit);
		OrderInventoryComponent.add(btnCancel);

	}

	public void actionPerformed(ActionEvent evt) {

		if (evt.getSource() == btnsubmit) {

			int rows = itemTable.getRowCount();
			int cnt = 0;			
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Calendar cal = Calendar.getInstance();		
			
			String emptyQtyErrorMsg = "", parseErrorMsg = "", wrongQtyMsg = "";
			Boolean emptyQtyEntered = false, parseError = false, wrongQtyEntered = false;;			

			//Call update orders here
			//"Select", "Item ID", "Item Name", "Price", "InStock", "Supplier Name", "Supplier ID", "Quantity"
			List<Order> alOrders = new ArrayList<Order>();				
			Order order = null;			
			Double totalPrice = 0.0, price = 0.0; 
			Integer	orderedQty = 0, currentQty = 0;
			
			for (cnt = 0; cnt < rows; cnt++) {
				
				if (Boolean.valueOf((Boolean) itemTable.getValueAt(cnt, 0))){

					order = new Order();
																
					order.set_itemID(itemTable.getValueAt(cnt, 1).toString());
					order.set_itemName(itemTable.getValueAt(cnt, 2).toString());
					
					currentQty = Integer.parseInt(itemTable.getValueAt(cnt, 3).toString());
					
					if(itemTable.getValueAt(cnt, 7).toString() == ""){
						
						emptyQtyErrorMsg += itemTable.getValueAt(cnt, 1).toString() + "\r\n"; 
						emptyQtyEntered = true;
						continue;
						
					}
					else{
						try {
							orderedQty = Integer.parseInt(itemTable.getValueAt(cnt, 7).toString());
							
							if(orderedQty <= 0)
							{
								parseErrorMsg += itemTable.getValueAt(cnt, 1).toString() + "\r\n"; 
								parseError = true;
								continue;
							}							
						} catch (Exception e) { 							
							parseErrorMsg += itemTable.getValueAt(cnt, 1).toString() + "\r\n";
							parseError = true;
							continue;
						}						
					}
					
					
					price = Double.parseDouble(itemTable.getValueAt(cnt, 4).toString());
					totalPrice = price * orderedQty;
					
					order.set_price(totalPrice);
					order.set_quantity(orderedQty);
					order.set_supplier(itemTable.getValueAt(cnt, 5).toString());						
					order.set_orderDate(dateFormat.format(cal.getTime()));					
					order.set_orderStatus("Ordered");
					
					if(currentQty + orderedQty > 10)
					{
						wrongQtyMsg += itemTable.getValueAt(cnt, 1).toString() + "\r\n"; 		
						wrongQtyEntered = true;
					}
					else
					{
						alOrders.add(order);
					}																				
				}												
			}
			
			OrderManager ordMgr = new OrderManager();
			
			try {
				
				if(wrongQtyMsg == "" && parseErrorMsg == "" && emptyQtyErrorMsg == ""){
					
					if(alOrders.size() > 0){
						
						ordMgr.AddOrders(alOrders.toArray(new Order[alOrders.size()]));
						
						JOptionPane.showMessageDialog(OrderInventoryComponent, "Order Submission is successful, email has been sent to suppliers");
						
					}
					else
					{
						JOptionPane.showMessageDialog(OrderInventoryComponent, "Please select at least one item to order");	
					}					
				}
				else {
					
					String finalErrorMsg = "";
					
					if(emptyQtyEntered){
						
						finalErrorMsg += "No quantity was entered for the following orders " + "\r\n"
						         		  + emptyQtyErrorMsg 
						         		  + "quantity entered should be numeric and greater than 0" + "\r\n"
						         		  + "---------------------------------------------------------------"
						         		  + "\r\n\r\n";						
					}
					
					if(parseError){
						
						finalErrorMsg += "Please check quantity ordered for following " + "\r\n"
						         	     + parseErrorMsg 
						                 + "quantity entered must be numeric and greater than 0" + "\r\n"
						                 + "---------------------------------------------------------------"
						         	     + "\r\n\r\n";												
					}
					
					if(wrongQtyEntered){
						
						finalErrorMsg += "Please check quantity ordered for following " + "\r\n"
						         + wrongQtyMsg 
						         + "current item quantiy + ordered qty cannot exceed 10" + "\r\n"
						         + "---------------------------------------------------------------"
						         + "\r\n\r\n";												
					}	
					
					JOptionPane.showMessageDialog(OrderInventoryComponent, finalErrorMsg);
				}				
								
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (DOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}						
		}

		if (evt.getSource() == btnCancel) {
			
			for (int i = 0; i < itemTable.getRowCount(); i++) {
				
				itemTable.clearSelection();
				itemTable.setValueAt("", i, 7);
				itemTable.setValueAt(false, i, 0);
				
			}
		}
	}
}