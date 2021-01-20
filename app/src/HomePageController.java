package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class HomePageController implements Initializable {
    @FXML private Button homeTab;
    @FXML private Button exitTab;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        homeTab.setStyle(null);
        homeTab.setStyle("-fx-border-color: #abeb34");
        homeTab.setStyle("-fx-border-width: 0px 0px 3px 0px");
    }

    public void menuChanged(ActionEvent event) throws IOException {
        Node n = (Node)event.getSource();
        String id = n.getId();
        Parent root = null;
        if (id.equals("learnTab")) {
            root = FXMLLoader.load(getClass().getResource("FXMLpages/learnPage.fxml"));
        }
        else if (id.equals("alignerTab")) {
            root = FXMLLoader.load(getClass().getResource("FXMLpages/alignerPage.fxml"));
        }
        else if (id.equals("aboutTab")) {
            root = FXMLLoader.load(getClass().getResource("FXMLpages/aboutPage.fxml"));
        }
        Scene scene = new Scene(root);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    public void closeApp() {
        Stage stage = (Stage) exitTab.getScene().getWindow();
        stage.close();
    }

}
