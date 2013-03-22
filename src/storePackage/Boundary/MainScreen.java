package storePackage.Boundary;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

public class MainScreen extends JFrame implements ActionListener 
{
	Container contentPane,contentPaneVertical;
	JFrame frame,framevertical;
	JSplitPane splitPane;
	JComponent bannerComponent,rightComponent,leftComponent;
	JLabel jlbempty,jlbempty1,jlbempty2,jlbempty3,jlbempty4,jlbempty5;
	JButton buyButton,returnButton,orderInventoryButton,orderStatusButton,checkinventoryButton;
	
	
	public static void main(String s[]) {
		
		MainScreen swing=new MainScreen();
		swing.init();
		
	}
	
	public void init()
	{
		frame = new JFrame("Inventory manager for Jewellery Store");
		// Add a window listener for close button
		frame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
	    splitPane.setContinuousLayout(true);
	    splitPane.setOneTouchExpandable(true);
	    
	    bannerComponent=new JPanel();
	    bannerComponent.setLayout(null);
	    JLabel bannerImg = new JLabel(new ImageIcon(getClass().getResource("/images/banner.jpg")));
	    bannerImg.setPreferredSize(new Dimension(750, 400));
	    bannerImg.setBounds(200, 150, 750, 400);
	    bannerComponent.add(bannerImg);
	    splitPane.setRightComponent(bannerComponent);
	    
	    leftComponent = new JPanel();
	    buyButton = new JButton("Buy Item");
	    buyButton.addActionListener(this);
	    
	    returnButton = new JButton("Return Item");
	    returnButton.addActionListener(this);
	    
	    orderInventoryButton = new JButton("Order Inventory");
	    orderInventoryButton.addActionListener(this);
	    
	    orderStatusButton = new JButton("Order Status");
	    orderStatusButton.addActionListener(this);
	    
	    checkinventoryButton = new JButton("Check inventory");
	    checkinventoryButton.addActionListener(this);
	    jlbempty = new JLabel("       ");
	    jlbempty.setPreferredSize(new Dimension(100, 200));
	    jlbempty1 = new JLabel("       ");
	    jlbempty1.setPreferredSize(new Dimension(100, 200));
	    jlbempty2 = new JLabel("       ");
	    jlbempty2.setPreferredSize(new Dimension(100, 200));
	    jlbempty3 = new JLabel("       ");
	    jlbempty3.setPreferredSize(new Dimension(100, 200));
	    jlbempty4 = new JLabel("       ");
	    jlbempty4.setPreferredSize(new Dimension(100, 200));
	    jlbempty5 = new JLabel("       ");
	    jlbempty5.setPreferredSize(new Dimension(100, 200));
	    
	    leftComponent.add(jlbempty);
	    leftComponent.add(buyButton);
	    leftComponent.add(jlbempty1);
	    leftComponent.add(returnButton);
	    leftComponent.add(jlbempty2);
	    leftComponent.add(orderInventoryButton);
	    leftComponent.add(jlbempty3);
	    leftComponent.add(orderStatusButton);
	    leftComponent.add(jlbempty4);
	    leftComponent.add(checkinventoryButton);
	    leftComponent.add(jlbempty5);
	    leftComponent.setLayout(new BoxLayout(leftComponent, BoxLayout.Y_AXIS));

	    
	    splitPane.setLeftComponent(leftComponent);
	    contentPane = frame.getContentPane();
	    contentPane.add(splitPane, BorderLayout.CENTER);
	    frame.setSize(1000, 1000);
		frame.pack();
		frame.setVisible(true);
	
	}
	
	public void actionPerformed(ActionEvent evt) 
	{
		if (evt.getSource() == buyButton) 
		{
			Purchases objBuyItem = null;
			try {
				
				objBuyItem = new Purchases();
				
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			splitPane.setRightComponent(objBuyItem.BuyItemComponent);
		}
		
		if (evt.getSource() == returnButton) 
		{
			Returns objReturnItem=new Returns();
			splitPane.setRightComponent(objReturnItem.ReturnItemComponent);
		}	
		
		if(evt.getSource() == checkinventoryButton)
		{
			CheckInventory objChckInv;
			
			try {
				
				objChckInv = new CheckInventory();
				splitPane.setRightComponent(objChckInv.chckInvComponent);
				
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		if(evt.getSource() == orderStatusButton)
		{
			OrderStatus objOrderStatus;
			
			try {
				
				objOrderStatus = new OrderStatus();
				splitPane.setRightComponent(objOrderStatus.OrderStatusComponent);
				
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(evt.getSource() == orderInventoryButton)
		{
			Orders objOrderInventory = null;
			try {
				
				objOrderInventory = new Orders();
				
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			splitPane.setRightComponent(objOrderInventory.OrderInventoryComponent);
		}
		
	}	
	
}

