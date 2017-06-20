package sf.templator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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

public class Controller implements Initializable {

    private String filename = "file_name";
    Random randomNo = new Random();
    int headerMax = 0, bodyMax = 0, footerMax = 0;
    List<String> headerList = new ArrayList<String>();
    List<String> bodyList = new ArrayList<String>();
    List<String> footerList = new ArrayList<String>();
    String assembledCode;

    private Model model = new Model();

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

    @FXML
    public void mouseClick(MouseEvent mouseEvent) {
//        model.testing();
        TreeItem item = (TreeItem) treeView.getSelectionModel().getSelectedItem();
        System.out.println("Tree Clicked!");
        if (mouseEvent.getClickCount() == 2) {
            System.out.println("Clicked twice. Code: " + item.getValue());
//            int row = treeView.getRow(item);
//            System.out.println(row);
//            int index = item.getParent().getChildren().indexOf(item);
//            System.out.println(index);

            assembledCode = assembleCode();

//            htmlEditor.setHtmlText("" + item.getValue());
            htmlEditor.setHtmlText(assembledCode);
            textArea.setText(assembledCode);
            webView.getEngine().loadContent(assembledCode);
        }
    }

    @FXML
    private void handleSaveButton(ActionEvent event) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename + ".html"));
            writer.write(assembledCode);

        } catch (IOException e) {
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
            }
        }
    }

    @FXML
    private void handleViewButton(ActionEvent event) throws URISyntaxException, IOException {
        String url = filename + ".html";
        URI u = new URI(url);
        java.awt.Desktop.getDesktop().browse(u);
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        assembledCode = assembleCode();
        htmlEditor.setHtmlText(INITIAL_TEXT);
        textArea.setWrapText(true);
        textArea.setText(htmlEditor.getHtmlText());
        webView.getEngine().loadContent(htmlEditor.getHtmlText());
    }

    private String assembleCode() {
        assembledCode = headerList.get(randomNo.nextInt(headerMax))
                + bodyList.get(randomNo.nextInt(bodyMax))
                + footerList.get(randomNo.nextInt(footerMax));
        return assembledCode;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
//            String workingDir = System.getProperty("user.dir");
//            System.out.println("Current working directory : " + workingDir);
            File fXmlFile = new File("src/sf/templator/pages.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();
//            System.out.println("Root element :" + root.getNodeName());
            NodeList nList = doc.getElementsByTagName("page");
            TreeItem<String> rootXML = new TreeItem<String>(root.getNodeName());
            rootXML.setExpanded(true);

            TreeItem<String> nodeItemChild;

            NodeList nListTemp;
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    TreeItem<String> nodeItem = new TreeItem<>(eElement.getAttribute("section"));
                    nListTemp = doc.getElementsByTagName(eElement.getAttribute("section"));
                    for (int i = 0; i < nListTemp.getLength(); i++) {
                        nodeItemChild = new TreeItem<>(eElement.getElementsByTagName("name").item(i).getTextContent());
                        nodeItem.getChildren().add(nodeItemChild);
                        if (eElement.getAttribute("section").equals("header")) {
                            headerList.add(eElement.getElementsByTagName("code").item(i).getTextContent());
                        }
                        if (eElement.getAttribute("section").equals("body")) {
                            bodyList.add(eElement.getElementsByTagName("code").item(i).getTextContent());
                        }
                        if (eElement.getAttribute("section").equals("footer")) {
                            footerList.add(eElement.getElementsByTagName("code").item(i).getTextContent());
                        }
                        headerMax = headerList.size();
                        bodyMax = bodyList.size();
                        footerMax = footerList.size();
                    }
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
