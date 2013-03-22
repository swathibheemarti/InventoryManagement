package storePackage.Boundary;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import storePackage.Control.InventoryManager;
import storePackage.Control.OrderManager;
import storePackage.Entities.Item;
import storePackage.Entities.Order;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderStatus implements ActionListener {
	JComponent OrderStatusComponent;
	DefaultTableModel model;
	JTable itemTable;
	JScrollPane scrollPane;
	JButton btnUpdateInventory;
	JLabel lblUpdated;
	JLabel lblInstruction;
	
	Object rowdata[][];

	public OrderStatus() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		
		OrderManager objOrderMgr = new OrderManager();
		rowdata = objOrderMgr.ConvertIntoObjectArray(objOrderMgr.GetOrders());

		Object colnames[] = { "Order ID", "ItemID", "Quantity Ordered", "Supplier Name", "Order Status", "Order Date","Total Price" };
		
		OrderStatusComponent = new JPanel();
		OrderStatusComponent.setLayout(null);		
		
		model = new DefaultTableModel(rowdata, colnames);
		
		itemTable = new JTable(model){
			
			public boolean isCellEditable(int row, int column){
				
				Boolean result = false;
				
				if(column == 4)
				{
					Object jComboObject = getValueAt(row, column);
					
					if(!jComboObject.toString().equalsIgnoreCase("Received"))
					{
						result = true;
					}									
				}
									
				return result;
			}
			
			@Override
			public Class getColumnClass(int column){
				return getValueAt(0, column).getClass();
			}
					
		};
		
		//itemTable.setDefaultRenderer(String.class, new CustomRendererOrderStatus());
		
		for(int rowCnt = 0; rowCnt < 10; rowCnt++){			
			itemTable.setRowHeight(rowCnt, 25);									
		}
		
		itemTable.setPreferredScrollableViewportSize(new Dimension(1000, 250));
	    scrollPane=new JScrollPane(itemTable);
	    //scrollPane.add(itemTable);
	    scrollPane.setBounds(50, 150, 1000, 250);
	    
	    
	    //Adding order status combo box to jtable
	    JComboBox<String> statusComboBox = new JComboBox<String>();
	    
	    statusComboBox.addItem("Ordered");
	    statusComboBox.addItem("Received");	    
	    statusComboBox.addItem("Pending");
	    
	    itemTable.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(statusComboBox));
	    
	    //Set up tool tips for the status cells.
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setToolTipText("Click to change status");
        itemTable.getColumnModel().getColumn(4).setCellRenderer(renderer);
        
	    
	    OrderStatusComponent.add(scrollPane);
	    
	    btnUpdateInventory = new JButton("Update Inventory");
	    btnUpdateInventory.addActionListener(this);
	    
	    btnUpdateInventory.setBounds(800,450, 250, 20);
	    
	    lblUpdated = new JLabel("Inventory updated with received items");
	    lblUpdated.setBounds(570, 450, 350, 20);
	    lblUpdated.setForeground(Color.RED);
	    lblUpdated.setVisible(false);
	    
	    lblInstruction = new JLabel("ORDER STATUS SCREEN");
	    Font f = new Font("Dialog", Font.BOLD, 30);
	    lblInstruction.setFont(f);
	    lblInstruction.setBounds(300,50,500,100);
	    lblInstruction.setForeground(Color.BLUE);
	
	    OrderStatusComponent.add(btnUpdateInventory);
	    OrderStatusComponent.add(lblUpdated);
	    OrderStatusComponent.add(lblInstruction);	    
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		List<Order> alOrders = new ArrayList<Order>();	
		List<Item> alItems = new ArrayList<Item>();
		
		Order order = null;
		Item item = null;
		
		Object objSelectedValue;
		
		if(e.getSource() == btnUpdateInventory){
			
			for (int i = 0; i < rowdata.length; i++) {
				
				/*if(!itemTable.isCellEditable(i, 4)){
					continue;
				}*/
				
				objSelectedValue = itemTable.getValueAt(i, 4);
				//update the status of items whose orders have been received or are pending
				if(objSelectedValue.toString().equalsIgnoreCase("Received") || objSelectedValue.toString().equalsIgnoreCase("Pending")){
					
					order = new Order();
					
					order.set_orderID(rowdata[i][0].toString());
					order.set_orderStatus(objSelectedValue.toString());
					
					//add all orders "received" or "pending" to array list of orders
					alOrders.add(order);	
					
					//update the quantity of items whose orders have been received
					if(objSelectedValue.toString().equalsIgnoreCase("Received")){
						
						item = new Item();
						
						item.set_itemID(rowdata[i][1].toString());
						item.set_quantity(Integer.parseInt(rowdata[i][2].toString()));
						
						//add all items with "received" status to array list of items 
						alItems.add(item);
					}									
				}				
			}
			
			
			lblUpdated.setVisible(alOrders.size() > 0);
			
			OrderManager orderMgr = new OrderManager();
			InventoryManager inventoryMgr = new InventoryManager();
			
			
			try {
				
				//Update order status in Orders xml data file
				orderMgr.UpdateOrderStatus(alOrders.toArray(new Order[alOrders.size()]));
				
				//Update inventory...
				inventoryMgr.AddItemsToInventory(alItems.toArray(new Item[alItems.size()]));
				
				//Disable update button
				btnUpdateInventory.setEnabled(false);
				
			} catch (XPathExpressionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParserConfigurationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (TransformerException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}


