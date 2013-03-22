package storePackage.Control;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import storePackage.Entities.Item;
import storePackage.Entities.Order;

public class InventoryManager {
	
	private String strItemsDataPath = "src/Entity Data/Items_Data.xml";

	public Boolean RemoveItemsFromInventory(String ItemsIDs,
			String QuantityOfItems) {
		
		Boolean isUpdateSuccessful = false;
		int cnt = 0, itemCnt;
		ItemsIDs = ItemsIDs.trim();
		QuantityOfItems = QuantityOfItems.trim();
		String[] ItemsToRemove = ItemsIDs.split("-");
		String[] ItemsQuantity = QuantityOfItems.split("-");
		
		try {
			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(strItemsDataPath);

			// Get the root element
			Node Items = doc.getFirstChild();
			NodeList ItemsCollection = doc.getElementsByTagName("Item");

			// Loop through the Items
			for (itemCnt = 0; itemCnt < ItemsToRemove.length; itemCnt++) {
				for (cnt = 0; cnt < ItemsCollection.getLength(); cnt++) {
 
					Node itemID = ItemsCollection.item(cnt);
					
					if (itemID.getNodeType() == Node.ELEMENT_NODE) {
						
						Element eElement = (Element) itemID;
						String strItemID = getTagValue("ItemID", eElement);
						
						if (strItemID.equalsIgnoreCase(ItemsToRemove[itemCnt])) {
						
							String strItemQuantity = getTagValue("QuantityAvailable", eElement);
							
							int QuantityAvailable = Integer.parseInt(strItemQuantity);							
							int QuantityTobeRemoved = Integer.parseInt(ItemsQuantity[itemCnt]);
							
							QuantityAvailable = QuantityAvailable - QuantityTobeRemoved;
							
							
							NodeList nlList = eElement.getElementsByTagName("QuantityAvailable").item(0).getChildNodes();							
							Node nValue = (Node) nlList.item(0);
							nValue.setTextContent(Integer.toString(QuantityAvailable));
						}
					}
				}
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(strItemsDataPath));
			transformer.transform(source, result);
			isUpdateSuccessful = true;

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (SAXException sae) {
			sae.printStackTrace();
		}
		
		return isUpdateSuccessful;
	}

	public void AddItemsToInventory(Item[] items){
		
		try {
			
			Document itemsDoc = getItemsDocument();
			
			XPath xpath = XPathFactory.newInstance().newXPath();
			Integer currentQty = 0;
			Integer updateQtyBy = 0;
			
			for (int i = 0; i < items.length; i++) {
				
				XPathExpression expr = xpath.compile("/Items_Collection/Item[ItemID=" + items[i].get_itemID() + "]/QuantityAvailable");
				
				Object result = expr.evaluate(itemsDoc, XPathConstants.NODE);			
				Node itemQuantityNode = (Node)result;
				
				currentQty =  Integer.parseInt(itemQuantityNode.getTextContent());
				updateQtyBy = Integer.parseInt(items[i].get_quantity().toString());
				
				itemQuantityNode.setTextContent(((Integer) (currentQty + updateQtyBy)).toString());
			}
			
			SaveItemsDocument(itemsDoc);
			
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Item[] CheckInventory() throws ParserConfigurationException, 
	                                      SAXException, IOException, 
	                                      XPathExpressionException
	                                      {
		
		//Get "orders" xml document
		Document itemsDoc = getItemsDocument();
		XPath xpath = XPathFactory.newInstance().newXPath();
		
		XPathExpression expr = xpath.compile("/Items_Collection/Item");
		
		Object result = expr.evaluate(itemsDoc, XPathConstants.NODESET);
		
		XPathExpression exprItemID = xpath.compile("ItemID/text()");
		XPathExpression exprItemName = xpath.compile("ItemName/text()");
		XPathExpression exprQuantity = xpath.compile("QuantityAvailable/text()");
		XPathExpression exprCost = xpath.compile("ItemCost/text()");
		XPathExpression exprSupplierID = xpath.compile("SupplierID/text()");
		XPathExpression exprSupplier = xpath.compile("Supplier/text()");
		
		//convert object type to nodelist type
		NodeList itemNodes = (NodeList) result;
		
		Item[] items = new Item[itemNodes.getLength()];
				
		
		for (int i = 0; i < itemNodes.getLength(); i++) {
			
			Item item = new Item();
			
			item.set_itemID(exprItemID.evaluate(itemNodes.item(i)));
			item.set_itemName(exprItemName.evaluate(itemNodes.item(i)));
			item.set_quantity(Integer.parseInt(exprQuantity.evaluate(itemNodes.item(i))));
			item.set_itemCost(Double.parseDouble(exprCost.evaluate(itemNodes.item(i))));
			item.set_suppID(exprSupplierID.evaluate(itemNodes.item(i)));
			item.set_supplier(exprSupplier.evaluate(itemNodes.item(i)));
			
			items[i] = item;
			
		}
					
		return items;					
	}
	
	//"Product ID", "Description", "Quantity", "Price", "Supplier"
	public Object[][] ConvertIntoObjectArray(Item[] items){
		
		Object[][] objItems = new Object[items.length][6];
		
		for (int i = 0; i < items.length; i++) {
			
			objItems[i][0] = items[i].get_itemID();
			objItems[i][1] = items[i].get_itemName();
			objItems[i][2] = items[i].get_quantity();
			objItems[i][3] = items[i].get_itemCost();
			objItems[i][4] = items[i].get_supplier();
			objItems[i][5] = items[i].get_suppID();
			
		}
		
		return objItems;
		
	}
	
	private static String getTagValue(String sTag, Element eElement) {
		
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();
	}
	
	private Document getItemsDocument() throws ParserConfigurationException, SAXException, IOException{	
		
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder.parse(strItemsDataPath);
		
		return doc;		
	}	
	
	private void SaveItemsDocument(Document doc) throws TransformerException
	{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		
		DOMSource source = new DOMSource(doc);
		
		StreamResult result = new StreamResult(new File(strItemsDataPath));
		
		transformer.transform(source, result);				
	}
}
