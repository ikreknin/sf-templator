package sf.templator;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private Button button;
    @FXML
    private TreeView treeView;
    @FXML
    private HTMLEditor htmlEditor;
    @FXML
    private TextArea textArea;
    @FXML
    private WebView webView;

    private final String INITIAL_TEXT = "<html><body>Lorem ipsum dolor sit "
            + "amet, consectetur adipiscing elit. Nam tortor felis, pulvinar "
            + "in scelerisque cursus, pulvinar at ante. Nulla consequat"
            + "congue lectus in sodales. Nullam eu est a felis ornare "
            + "bibendum et nec tellus. Vivamus non metus tempus augue auctor "
            + "ornare. Duis pulvinar justo ac purus adipiscing pulvinar. "
            + "Integer congue faucibus dapibus. Integer id nisl ut elit "
            + "aliquam sagittis gravida eu dolor. Etiam sit amet ipsum "
            + "sem.</body></html>";

    private FXMLDocumentModel model = new FXMLDocumentModel();

    @FXML
    public void mouseClick(MouseEvent mouseEvent) {
        TreeItem item = (TreeItem) treeView.getSelectionModel().getSelectedItem();
        System.out.println("Tree Clicked!");
        if (mouseEvent.getClickCount() == 2) {
            System.out.println("Twice !! " + item.getValue());
            System.out.println("Code: " + item.getValue());
//            int row = treeView.getRow(item);
//            System.out.println("NEW !! " + row);
//            int index = item.getParent().getChildren().indexOf(item);
//            System.out.println("NEW !! " + index);

            htmlEditor.setHtmlText("" + item.getValue());
            textArea.setText(htmlEditor.getHtmlText());
            webView.getEngine().loadContent(htmlEditor.getHtmlText());

        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        button.setText("Clicked");
        htmlEditor.setHtmlText(INITIAL_TEXT);
        textArea.setWrapText(true);
        textArea.setText(htmlEditor.getHtmlText());
        webView.getEngine().loadContent(htmlEditor.getHtmlText());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        System.out.println("TESTING");
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

            TreeItem<String> rootXML = new TreeItem<String>(root.getNodeName());
            rootXML.setExpanded(true);
//        TreeItem<String> nodeItemA = new TreeItem<>("Item A");
//        TreeItem<String> nodeItemB = new TreeItem<>("Item B");
//        TreeItem<String> nodeItemC = new TreeItem<>("Item C");
//        rootXML.getChildren().addAll(nodeItemA, nodeItemB, nodeItemC);
//        TreeItem<String> nodeItemA1 = new TreeItem<>("Item A1");
//        TreeItem<String> nodeItemA2 = new TreeItem<>("Item A2");
//        TreeItem<String> nodeItemA3 = new TreeItem<>("Item A3");
//        nodeItemA.getChildren().addAll(nodeItemA1, nodeItemA2, nodeItemA3);

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
//                System.out.println("Current Element :" + nNode.getNodeName());
                if (nNode.getNodeName() == "page") {
                    System.out.println("PAGE");
                }
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.println("URL name : " + eElement.getAttribute("url"));
                    System.out.println("Page name : " + eElement.getElementsByTagName("name").item(0).getTextContent());
                    System.out.println("Code : " + eElement.getElementsByTagName("code").item(0).getTextContent());

                    TreeItem<String> nodeItem = new TreeItem<>(nNode.getNodeName());
                    TreeItem<String> nodeItemChild = new TreeItem<>(eElement.getElementsByTagName("name").item(0).getTextContent());

                    TreeItem<String> found = new TreeItem<>(eElement.getElementsByTagName("code").item(0).getTextContent());
                    nodeItemChild.getChildren().add(found);

                    nodeItem.getChildren().add(nodeItemChild);
                    rootXML.getChildren().add(nodeItem);
                    nodeItem.setExpanded(true);
                }
            }
            treeView.setRoot(rootXML);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
