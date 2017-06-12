package sf.templator;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

public class ReadXMLFile {

    public static void readXMLFile() {
        try {
//            String workingDir = System.getProperty("user.dir");
//            System.out.println("Current working directory : " + workingDir);
            File fXmlFile = new File("src/sf/templator/pages.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();
            System.out.println("Root element :" + root.getNodeName());
            NodeList nList = doc.getElementsByTagName("page");
            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("Current Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.println("URL name : " + eElement.getAttribute("url"));
                    System.out.println("Page name : " + eElement.getElementsByTagName("name").item(0).getTextContent());
                    System.out.println("Code : " + eElement.getElementsByTagName("code").item(0).getTextContent());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
