package storePackage.Control;

import org.w3c.dom.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import javax.xml.crypto.NodeSetData;
import javax.xml.parsers.*;

import java.io.File;
import java.io.IOException;
import org.xml.sax.SAXException;

import storePackage.Entities.Order;

import java.util.*;

public class OrderManager {
	
	private String strOrdersDataPath = "src/Entity Data/Orders_Data.xml";
	
	
	public void UpdateOrderStatus(Order[] orders) throws ParserConfigurationException, SAXException, 
	                                             IOException, XPathExpressionException, 
	                                             TransformerException
    {		
		//Get "Orders" xml document
		Document ordersDoc = getOrdersDocument();
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		for (int i = 0; i < orders.length; i++) {
			
			XPathExpression expr = xpath.compile("/Orders/Order[OrderID=" + orders[i].get_orderID() + "]/OrderStatus");
			
			Object result = expr.evaluate(ordersDoc, XPathConstants.NODE);			
			Node orderStatusNode = (Node)result;
			
			orderStatusNode.setTextContent(orders[i].get_orderStatus());
		}
		
		SaveItemsDocument(ordersDoc);								
	}
	
	public Integer GetMaxOrderID(Document docOrderXML) throws XPathExpressionException{
		
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression expr = xpath.compile("/Orders/Order/OrderID");
		
		Object result = expr.evaluate(docOrderXML, XPathConstants.NODESET);
		NodeList orderIDNodes = (NodeList) result;
		
		int maxOrderId = -1;
		int currentOrderId = -1;
		
		for (int i = 0; i < orderIDNodes.getLength() ; i++) {
			
			currentOrderId = Integer.parseInt(orderIDNodes.item(i).getTextContent());
			
			if(currentOrderId > maxOrderId)
			{
				maxOrderId = currentOrderId;
			}						
		}
		
		return maxOrderId;		
	}
	
	public void AddOrders(Order[] orders) throws ParserConfigurationException, SAXException, IOException, TransformerException, DOMException, XPathExpressionException 
	{
		Document ordersDoc = getOrdersDocument();
		
		NodeList nodeList = ordersDoc.getElementsByTagName("Order");
		
		//Loop through the orders
		for (int i = 0; i < orders.length; i++) {
			
			//Create the parent element <order></order>
			Element orderParentElement = ordersDoc.createElement("Order");
			
			//Create orderid node <orderid></orderid>
			Element orderIDNode = ordersDoc.createElement("OrderID");
			orderIDNode.setTextContent(((Integer) (GetMaxOrderID(ordersDoc) + 1)).toString());
			orderParentElement.appendChild(orderIDNode);
			
			//Create itemid node <itemid></itemid>
			Element orderItemIDNode = ordersDoc.createElement("ItemID");
			orderItemIDNode.setTextContent(orders[i].get_itemID());
			orderParentElement.appendChild(orderItemIDNode);
			
			//Create itemname node <itemName></itemName>
			Element orderItemNameNode = ordersDoc.createElement("ItemName");
			orderItemNameNode.setTextContent(orders[i].get_itemName());
			orderParentElement.appendChild(orderItemNameNode);
			
			//Create supplier node
			Element orderSupplierNode = ordersDoc.createElement("Supplier");
			orderSupplierNode.setTextContent(orders[i].get_supplier());
			orderParentElement.appendChild(orderSupplierNode);
			
			//Create quantity node
			Element orderQuantityNode = ordersDoc.createElement("Quantity");
			orderQuantityNode.setTextContent(orders[i].get_quantity().toString());
			orderParentElement.appendChild(orderQuantityNode);
			
			//create price node
			Element orderPriceNode = ordersDoc.createElement("Price");
			orderPriceNode.setTextContent(orders[i].get_price().toString());
			orderParentElement.appendChild(orderPriceNode);
			
			//create order date node
			Element orderDateNode = ordersDoc.createElement("OrderDate");
			orderDateNode.setTextContent(orders[i].get_orderDate());
			orderParentElement.appendChild(orderDateNode);
			
			//create order status node
			Element orderStatusNode = ordersDoc.createElement("OrderStatus");
			orderStatusNode.setTextContent(orders[i].get_orderStatus());
			orderParentElement.appendChild(orderStatusNode);
			
			nodeList.item(0).getParentNode().appendChild(orderParentElement);
			
		}
		
		SaveItemsDocument(ordersDoc);				
	}
	
	public Order[] GetOrders() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException
	{
		//Get "orders" xml document
		Document ordersDoc = getOrdersDocument();
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		XPathExpression expr = xpath.compile("/Orders/Order[OrderStatus='Ordered'] | /Orders/Order[OrderStatus='Pending']");
		//returns a set of nodes with order status as ordered or pending
		Object result = expr.evaluate(ordersDoc, XPathConstants.NODESET);
		
		XPathExpression exprOrderID = xpath.compile("OrderID/text()");
		XPathExpression exprItemID = xpath.compile("ItemID/text()");
		XPathExpression exprItemName = xpath.compile("ItemName/text()");
		XPathExpression exprSupplier = xpath.compile("Supplier/text()");
		XPathExpression exprQuantity = xpath.compile("Quantity/text()");
		XPathExpression exprPrice = xpath.compile("Price/text()");
		XPathExpression exprOrderDate = xpath.compile("OrderDate/text()");
		XPathExpression exprOrderStatus = xpath.compile("OrderStatus/text()");
		
		//convert object type to nodelist type
		NodeList orderNodes = (NodeList) result;
		
		Order[] orders = new Order[orderNodes.getLength()];
		
		for (int i = 0; i < orderNodes.getLength(); i++) {
			
			Order order = new Order();
			
			Node orderNode = orderNodes.item(i);
			
			order.set_orderID(exprOrderID.evaluate(orderNode));
			order.set_itemID(exprItemID.evaluate(orderNode));
			order.set_itemName(exprItemName.evaluate(orderNode));
			order.set_supplier(exprSupplier.evaluate(orderNode));
			order.set_quantity(Integer.parseInt(exprQuantity.evaluate(orderNode)));
			order.set_price(Double.parseDouble(exprPrice.evaluate(orderNode)));
			order.set_orderDate(exprOrderDate.evaluate(orderNode));
			order.set_orderStatus(exprOrderStatus.evaluate(orderNode));
			
			orders[i] = order;
		}
			
		return orders;				
	}
	
	//"Order ID", "ItemID", "Quantity Ordered", "Supplier Name", "Order Status", "Order Date","Total Price"
	public Object[][] ConvertIntoObjectArray(Order[] orders)
	{
		Object[][] data = new Object[orders.length][7];
		
		for (int i = 0; i < orders.length; i++) {
			
			data[i][0] = orders[i].get_orderID();
			data[i][1] = orders[i].get_itemID();
			data[i][2] = orders[i].get_quantity();
			data[i][3] = orders[i].get_supplier();
			data[i][4] = orders[i].get_orderStatus();
			data[i][5] = orders[i].get_orderDate();
			data[i][6] = orders[i].get_price();
		}
		
		return data;//object array
	}
	
	
	private Document getOrdersDocument() throws ParserConfigurationException, SAXException, IOException{		
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		domFactory.setIgnoringComments(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder.parse(strOrdersDataPath);
		return doc;		
	}	
	
	private void SaveItemsDocument(Document doc) throws TransformerException
	{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		
		DOMSource source = new DOMSource(doc);
		
		StreamResult result = new StreamResult(new File(strOrdersDataPath));
		transformer.transform(source, result);				
	}
}
