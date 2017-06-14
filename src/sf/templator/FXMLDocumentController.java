package sf.templator;

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
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;

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
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        button.setText("Clicked");
        htmlEditor.setHtmlText(INITIAL_TEXT);
        textArea.setWrapText(true);
        textArea.setText(htmlEditor.getHtmlText());
        webView.getEngine().loadContent(htmlEditor.getHtmlText());

        TreeItem<String> root = new TreeItem<>("Root");
        root.setExpanded(true);
        TreeItem<String> itemChild = new TreeItem<>("Child");
        itemChild.setExpanded(false);
        root.getChildren().add(itemChild);
        treeView.setRoot(root);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
//        System.out.println("TESTING");
        ReadXMLFile.readXMLFile();
    }

}
