package storePackage.Boundary;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;

public class Returns implements ActionListener
{
	JComponent ReturnItemComponent;
	DefaultTableModel model;
	JTable itemTable;
	JScrollPane scrollPane;
	JLabel lblSubtotal,lblTax,lblTotal,lblInstruction;
	JButton btnReturn,btnCancel,btnUpdateCart;
	JTextField txtSubtotal,txtTax,txtTotal;
	ImageIcon icon1,icon2,icon3,icon4,icon5,icon6;
	double doubleTotal=0,doubleSubTotal=0,doubleTax=0;
	Returns()
	{
		ReturnItemComponent=new JPanel();
		ReturnItemComponent.setLayout(null);
		icon1=new ImageIcon(getClass().getResource("/images/bangles1.gif"));
		icon2=new ImageIcon(getClass().getResource("/images/necklace1.jpg"));
		icon3=new ImageIcon(getClass().getResource("/images/necklace2.jpg"));
		icon4=new ImageIcon(getClass().getResource("/images/Ruby1.jpg"));
		icon5=new ImageIcon(getClass().getResource("/images/ring1.jpeg"));
		icon6=new ImageIcon(getClass().getResource("/images/ring2.jpg"));
		//JTable Begins
	    String[] itemColumnNames= {"Select",
	    						   "Item ID",
                				   "Item Name",
                				   "Preview",
                				   "Price",
                				   "Quantity"};
	    
	    Object[][] itemData= {
	    		{false,"101","Emerald Bangle Bracelet",icon1,"$10.00",""},
	    		{false,"102","Emerald Necklace Yellow Gold",icon2,"$11.00",""},
	    		{false,"103","Sterling Silver Necklace",icon3,"$12.00",""},
	    		{false,"104","Natural Ruby Ring 14K White Gold",icon4,"$13.00",""},
	    		{false,"105","Natural Emerald Ring",icon5,"$14.00",""},
	    		{false,"106","Diamond Heart Ring Round Cut",icon6,"$15.00",""}
	    };
	    
	   // int rowNum,columnNum;
	    model = new DefaultTableModel(itemData, itemColumnNames);
	    itemTable=new JTable(model){  
	        public boolean isCellEditable(int row,int column){  
	            //Object o = getValueAt(row,column);  
	            
	            		if(column==1) return false;
	            		if(column==2) return false;
	            		if(column==4) return false;
	            		
	            
	            /*if(o != null && o.equals("3")) return false;  */
	            return true;  }
	        @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 1:
                        return String.class;
                    case 2:
                        return String.class;
                    case 3:
                        return ImageIcon.class;
                    case 4:
                        return String.class;
                    case 5:
                        return String.class;
                    default:
                        return Boolean.class;
                }
            }};
            
            //model.addTableModelListener( this );
            
        for(int rowCnt=0;rowCnt<6;rowCnt++)
        {
        	itemTable.setRowHeight( rowCnt, 75 );
        	//itemTable.getColumnModel().getColumn(3).setWidth(75);
        }
	    itemTable.setPreferredScrollableViewportSize(new Dimension(1000, 450));
	    scrollPane=new JScrollPane(itemTable);
	    //scrollPane.add(itemTable);
	    scrollPane.setBounds(50, 50, 1000, 450);
	    ReturnItemComponent.add(scrollPane);
	    
	    //JTable Ends

	    //Total price Calculation
	    lblSubtotal=new JLabel("Sub Total:");
	    lblTax=new JLabel("Tax:");
	    lblTotal=new JLabel("Total:");
	    
	    txtSubtotal=new JTextField();
	    txtTax=new JTextField();
	    txtTotal=new JTextField();
	    
	    btnUpdateCart=new JButton("Update cart");
	    btnUpdateCart.addActionListener(this);
	    btnReturn=new JButton("Return");
	    btnReturn.addActionListener(this);
	    btnReturn.setEnabled(false);
	    btnCancel=new JButton("Cancel");
	    
	    lblSubtotal.setBounds(700, 550, 80, 20);
	    lblTax.setBounds(700, 580, 80, 20);
	    lblTotal.setBounds(700, 610, 80, 20);
	    
	    txtSubtotal.setBounds(790, 550, 50, 20);
	    txtTax.setBounds(790, 580, 50, 20);
	    txtTotal.setBounds(790, 610, 50, 20);
	    
	    btnUpdateCart.setBounds(580, 550, 100, 20);
	    btnReturn.setBounds(700, 650, 80, 20);
	    btnCancel.setBounds(800, 650, 90, 20);
	    
	    lblInstruction = new JLabel("RETURN AN ITEM");
	    Font f = new Font("Dialog", Font.BOLD, 25);
	    lblInstruction.setFont(f);
	    lblInstruction.setBounds(400,10,500,50);
	    lblInstruction.setForeground(Color.BLUE);
	    
	    ReturnItemComponent.add(lblSubtotal);
	    ReturnItemComponent.add(lblTax);
	    ReturnItemComponent.add(lblTotal);
	    ReturnItemComponent.add(txtSubtotal);
	    ReturnItemComponent.add(txtTax);
	    ReturnItemComponent.add(txtTotal);
	    ReturnItemComponent.add(btnReturn);
	    ReturnItemComponent.add(btnCancel);
	    ReturnItemComponent.add(btnUpdateCart);
	    ReturnItemComponent.add(lblInstruction);
	}
	
	private String RemoveDollarSign(String strAmount)
	{
		strAmount=strAmount.substring(1);
		return strAmount;
	}
	
	public void actionPerformed(ActionEvent evt) 
	{
		
		if(evt.getSource() == btnUpdateCart)
		{
			int rows = itemTable.getRowCount();
			int cols = itemTable.getColumnCount();
			int intLoopCnt=0;
			Boolean isSelected=false;
			double price;
			int intQuantity=0;
			String strQuantity="";
			doubleSubTotal=0;doubleTax=0;doubleTotal=0;
			
			for(intLoopCnt=0;intLoopCnt<rows;intLoopCnt++)
			{
				intQuantity=0;
				strQuantity="";
				isSelected=false;
				isSelected=Boolean.valueOf((Boolean) itemTable.getValueAt(intLoopCnt, 0));
				strQuantity = String.valueOf( itemTable.getValueAt(intLoopCnt, 5) );
				price=Double.parseDouble(RemoveDollarSign((String)itemTable.getValueAt(intLoopCnt, 4)));
				if(isSelected)
				{
					if(!strQuantity.isEmpty())
					{
						intQuantity=Integer.parseInt(strQuantity);
						btnReturn.setEnabled(true);
					}
					price=intQuantity*price;
					doubleSubTotal= doubleSubTotal + price ;
					doubleTax= doubleTax + ((price*7.0)/100.0);
					doubleTotal= ((doubleTotal + (price + (price*0.07)))*100.0)/100.0;
				}
			}
			txtSubtotal.setText(Double.toString(doubleSubTotal));
			txtTax.setText(Double.toString(doubleTax));
			txtTotal.setText(Double.toString(doubleTotal));
		}
		
		if(evt.getSource() == btnReturn)
		{
			//InventoryManager objInventoryManager=new InventoryManager();
			int rows = itemTable.getRowCount();
			int intLoopCnt=0;
			Boolean blnResult=true,blnEmptySelection=false,isSelected=false;
			for(intLoopCnt=0;intLoopCnt<rows;intLoopCnt++)
			{
				isSelected=Boolean.valueOf((Boolean) itemTable.getValueAt(intLoopCnt, 0));
				if(isSelected)
				{
					blnEmptySelection=false;
					break;
				}
				else
				{
					blnEmptySelection=true;
				}
			}
			
			if(!blnEmptySelection)
			{
				if(blnResult)
				{
					JOptionPane.showMessageDialog(null, "Items Returned !!");
					btnUpdateCart.setEnabled(false);
					btnReturn.setEnabled(false);
					btnCancel.setEnabled(false);
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Select atleast one item");
			}
		}
		
		if(evt.getSource() == btnCancel)
		{
			int intLoopCnt=0;
			int rows = itemTable.getRowCount();
			for(intLoopCnt=0;intLoopCnt<rows;intLoopCnt++)
			{
				itemTable.setValueAt(false, intLoopCnt, 0);
				itemTable.setValueAt("", intLoopCnt, 5);
			}
			
			txtSubtotal.setText("");
			txtTax.setText("");
			txtTotal.setText("");
		}
	}
}
