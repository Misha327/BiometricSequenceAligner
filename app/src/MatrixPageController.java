package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class MatrixPageController {
    @FXML private Text matrixArea;
    @FXML private Button exitTab;

    private Scene prevScene;

    public void setMatrixArea(StringBuilder matrix) {
        matrixArea.setText(matrix.toString());
        matrixArea.setFont(Font.font("Courier New", 13));
    }

    public void saveStage(Scene prevScene) {
        this.prevScene = prevScene;
    }

    public void back(ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(prevScene);
        window.show();
    }

    public void menuChanged(ActionEvent event) throws IOException {
        Node n = (Node)event.getSource();
        String id = n.getId();
        Parent root = null;
        if (id.equals("learnTab")) {
            root = FXMLLoader.load(getClass().getResource("FXMLpages/learnPage.fxml"));
        }
        else if (id.equals("homeTab")) {
            root = FXMLLoader.load(getClass().getResource("FXMLpages/homePage.fxml"));
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
