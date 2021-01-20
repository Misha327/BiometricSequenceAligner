package sample;

import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.biojava.nbio.core.alignment.matrices.SubstitutionMatrixHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AlignerController implements Initializable {

    @FXML private TextField first;
    @FXML private TextField second;
    @FXML public VBox vbox;
    @FXML public HBox resultBox;
    @FXML public VBox resultLabelBox;
    @FXML private JFXComboBox<String> choiceBox;
    @FXML private TextField gapPenaltyField;
    @FXML private TextField gapExtField;
    @FXML private Text gapExtLabel;
    @FXML private Text gapPenLabel;
    @FXML private JFXComboBox<String> matrixPicker;
    @FXML private Button alignerTab;
    @FXML private Text matrixArea;
    @FXML private Text errorText;
    @FXML private Button viewMatrixButton;
    @FXML private Button exitTab;

    private StringBuilder matrix;
    private String globalAffine = "Global (with affine gap)";
    private String global = "Global";
    private String semi = "Semi-global";
    private String local = "Local";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().add(globalAffine);
        choiceBox.getItems().add(global);
        choiceBox.getItems().add(semi);
        choiceBox.getItems().add(local);
        matrixPicker.getItems().add("Blosum50");
        matrixPicker.getItems().add("Blosum60");
        matrixPicker.getItems().add("Blosum62");
        matrixPicker.getItems().add("Identity");
        alignerTab.setStyle(null);
        alignerTab.setStyle("-fx-border-color: #abeb34");
        alignerTab.setStyle("-fx-border-width: 0px 0px 3px 0px");
        viewMatrixButton.setDisable(true);
    }

    public void align() {
        try {
            if (first.getLength() > 0 && second.getLength() > 0 && !matrixPicker.getValue().isEmpty()) {
                //Clear error text
                errorText.setText("");
                if (choiceBox.getValue().equals(globalAffine)) {
                    SequenceAligner aligner = new SequenceAligner(first.getText().toUpperCase(), second.getText().toUpperCase(), Integer.parseInt(gapPenaltyField.getText()), Integer.parseInt(gapExtField.getText()));
                    StringBuilder[][] results = new StringBuilder[0][];

                    if (matrixPicker.getValue().equals("Identity")) {
                        short[][] identity = new short[20][20];

                        for (int i = 0; i < 20; i++) {
                            for (int j = 0; j < 20; j++) {
                                if (i == j) {
                                    identity[i][j] = 1;
                                } else identity[i][j] = 0;
                            }
                        }
                        results = aligner.globalAffineAlignment(identity);
                    } else if (matrixPicker.getValue().equals("Blosum50")) {
                        results = aligner.globalAffineAlignment(SubstitutionMatrixHelper.getBlosum50().getMatrix());

                    } else if (matrixPicker.getValue().equals("Blosum60")) {
                        results = aligner.globalAffineAlignment(SubstitutionMatrixHelper.getBlosum60().getMatrix());

                    } else if (matrixPicker.getValue().equals("Blosum62")) {
                        results = aligner.globalAffineAlignment(SubstitutionMatrixHelper.getBlosum62().getMatrix());
                    }

                    if (resultBox.getChildren().size() > 0) {
                        resultBox.getChildren().clear();
                        resultLabelBox.getChildren().clear();
                    }
                    matrixArea.setText("");
                    resultLabelBox.getChildren().add(new Text("Result(s):"));

                    for (int i = 0; i < results.length; i++) {
                        VBox newVBox = new VBox();

                        for (int j = 0; j < results[i].length; j++) {
                            if (j == 2) {
                                Text text = new Text("Score: " + results[i][j].toString());
                                text.setStyle("-fx-font-weight: bold");
                                text.setFont(Font.font("Courier New", 13));

                                newVBox.getChildren().add(text);
                            } else {
                                Text text = new Text(results[i][j].toString());
                                text.setStyle("-fx-font-weight: bold");
                                text.setFont(Font.font("Courier New", 13));

                                newVBox.getChildren().add(text);
                            }
                        }
                        viewMatrixButton.setDisable(true);
                        resultBox.getChildren().add(newVBox);
                    }

                } else if (choiceBox.getValue().equals(global)) {
                    SequenceAligner aligner = new SequenceAligner(first.getText().toUpperCase(), second.getText().toUpperCase(), Integer.parseInt(gapPenaltyField.getText()));

                    String[] results = new String[0];

                    if (matrixPicker.getValue().equals("Identity")) {
                        short[][] identity = new short[20][20];

                        for (int i = 0; i < 20; i++) {
                            for (int j = 0; j < 20; j++) {
                                if (i == j) {
                                    identity[i][j] = 1;
                                } else identity[i][j] = 0;
                            }
                        }
                        results = aligner.semiGlobalAlignment(identity);
                    } else if (matrixPicker.getValue().equals("Blosum50")) {
                        results = aligner.globalAlignment(SubstitutionMatrixHelper.getBlosum50().getMatrix());

                    } else if (matrixPicker.getValue().equals("Blosum60")) {
                        results = aligner.globalAlignment(SubstitutionMatrixHelper.getBlosum60().getMatrix());

                    } else if (matrixPicker.getValue().equals("Blosum62")) {
                        results = aligner.globalAlignment(SubstitutionMatrixHelper.getBlosum62().getMatrix());
                    }
                    if (resultBox.getChildren().size() > 0 || resultLabelBox.getChildren().size() > 0) {
                        resultBox.getChildren().clear();
                        resultLabelBox.getChildren().clear();
                    }
                    //Build the dp matrix
                    matrix = buildMatrix(aligner);

                    resultLabelBox.getChildren().add(new Text("Result(s):"));
                    VBox newVBox = new VBox();

                    for (int i = 0; i < results.length; i++) {
                        if (i == 2) {
                            Text text = new Text("Score: " + results[i]);
                            text.setStyle("-fx-font-weight: bold");
                            text.setFont(Font.font("Courier New", 13));

                            newVBox.getChildren().add(text);
                        } else {
                            Text text = new Text(results[i]);
                            text.setStyle("-fx-font-weight: bold");
                            text.setFont(Font.font("Courier New", 13));

                            newVBox.getChildren().add(text);
                        }
                    }
                    viewMatrixButton.setDisable(false);
                    resultBox.getChildren().add(newVBox);
                } else if (choiceBox.getValue().equals(semi)) {
                    SequenceAligner aligner = new SequenceAligner(first.getText().toUpperCase(), second.getText().toUpperCase(), Integer.parseInt(gapPenaltyField.getText()));

                    String[] results = new String[0];

                    if (matrixPicker.getValue().equals("Identity")) {
                        short[][] identity = new short[20][20];

                        for (int i = 0; i < 20; i++) {
                            for (int j = 0; j < 20; j++) {
                                if (i == j) {
                                    identity[i][j] = 1;
                                } else identity[i][j] = 0;
                            }
                        }
                        results = aligner.semiGlobalAlignment(identity);
                    } else if (matrixPicker.getValue().equals("Blosum50")) {
                        results = aligner.semiGlobalAlignment(SubstitutionMatrixHelper.getBlosum50().getMatrix());

                    } else if (matrixPicker.getValue().equals("Blosum60")) {
                        results = aligner.semiGlobalAlignment(SubstitutionMatrixHelper.getBlosum60().getMatrix());

                    } else if (matrixPicker.getValue().equals("Blosum62")) {
                        results = aligner.semiGlobalAlignment(SubstitutionMatrixHelper.getBlosum62().getMatrix());
                    }
                    if (resultBox.getChildren().size() > 0 || resultLabelBox.getChildren().size() > 0) {
                        resultBox.getChildren().clear();
                        resultLabelBox.getChildren().clear();
                    }
                    matrix = buildMatrix(aligner);

                    resultLabelBox.getChildren().add(new Text("Result(s):"));
                    VBox newVBox = new VBox();

                    for (int i = 0; i < results.length; i++) {
                        if (i == 2) {
                            Text text = new Text("Score: " + results[i]);
                            text.setStyle("-fx-font-weight: bold");
                            text.setFont(Font.font("Courier New", 13));

                            newVBox.getChildren().add(text);
                        } else {
                            Text text = new Text(results[i]);
                            text.setStyle("-fx-font-weight: bold");
                            text.setFont(Font.font("Courier New", 13));

                            newVBox.getChildren().add(text);
                        }
                    }
                    viewMatrixButton.setDisable(false);
                    resultBox.getChildren().add(newVBox);
                } else if (choiceBox.getValue().equals(local)) {

                    SequenceAligner aligner = new SequenceAligner(first.getText().toUpperCase(), second.getText().toUpperCase(), Integer.parseInt(gapPenaltyField.getText()));
                    String[] results = new String[0];

                    if (matrixPicker.getValue().equals("Identity")) {
                        short[][] identity = new short[20][20];

                        for (int i = 0; i < 20; i++) {
                            for (int j = 0; j < 20; j++) {
                                if (i == j) {
                                    identity[i][j] = 1;
                                } else identity[i][j] = 0;
                            }
                        }
                        results = aligner.localAlignment(identity);
                    } else if (matrixPicker.getValue().equals("Blosum50")) {
                        results = aligner.localAlignment(SubstitutionMatrixHelper.getBlosum50().getMatrix());
                    } else if (matrixPicker.getValue().equals("Blosum60")) {
                        results = aligner.localAlignment(SubstitutionMatrixHelper.getBlosum60().getMatrix());
                    } else if (matrixPicker.getValue().equals("Blosum62")) {
                        results = aligner.localAlignment(SubstitutionMatrixHelper.getBlosum62().getMatrix());
                    }
                    if (resultBox.getChildren().size() > 0 || resultLabelBox.getChildren().size() > 0) {
                        resultBox.getChildren().clear();
                        resultLabelBox.getChildren().clear();
                    }

                    matrix = buildMatrix(aligner);

                    resultLabelBox.getChildren().add(new Text("Result(s):"));
                    VBox newVBox = new VBox();

                    for (int i = 0; i < results.length; i++) {
                        if (i == 2) {
                            Text text = new Text("Score: " + results[i]);
                            text.setStyle("-fx-font-weight: bold");
                            text.setFont(Font.font("Courier New", 13));
                            newVBox.getChildren().add(text);
                        } else {
                            Text text = new Text(results[i]);
                            text.setStyle("-fx-font-weight: bold");
                            text.setFont(Font.font("Courier New", 13));

                            newVBox.getChildren().add(text);
                        }
                    }
                    viewMatrixButton.setDisable(false);
                    resultBox.getChildren().add(newVBox);
                }
            } else {
                if (resultBox.getChildren().size() > 0 || resultLabelBox.getChildren().size() > 0) {
                    resultBox.getChildren().clear();
                    resultLabelBox.getChildren().clear();
                    matrixArea.setText("");
                }
                errorText.setText("Enter your sequences!");
            }
        } catch (NullPointerException e) {
            if (matrixPicker.getValue() == null && choiceBox.getValue() == null) {
                errorText.setText("Select alignment and matrix type!");
            } else if (choiceBox.getValue() == null) {
                errorText.setText("Set the alignment type!");
            } else errorText.setText("Choose a substitution matrix!");
        }
    }

    public void viewMatrix(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLpages/matrixPage.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        MatrixPageController controller = loader.getController();
        if (matrix != null) {
            controller.setMatrixArea(matrix);
            controller.saveStage(((Node) event.getSource()).getScene());
        }
        window.setScene(scene);
        window.show();
    }

    public StringBuilder buildMatrix(SequenceAligner aligner) {
        StringBuilder matrix = new StringBuilder();
        for (int i = 0; i < aligner.matrix[0].length; i++) {
            if (i > 0) matrix.append(first.getText().toUpperCase().charAt(i - 1)).append("   ");
            else matrix.append("        ");
        }
        matrix.append("\n");
        for (int i = 0; i < aligner.matrix.length; i++) {
            if (i > 0) {
                matrix.append(second.getText().toUpperCase().charAt(i - 1)).append(" ");
            } else matrix.append("  ");
            for (int j = 0; j < aligner.matrix[i].length; j++) {

                if (aligner.matrix[i][j] < 10 && aligner.matrix[i][j] > -1) {
                    matrix.append("  ").append(aligner.matrix[i][j]).append(" ");
                } else if (aligner.matrix[i][j] < 0 && aligner.matrix[i][j] > -10) {
                    matrix.append(" ").append(aligner.matrix[i][j]).append(" ");
                } else if (aligner.matrix[i][j] > 9) {
                    matrix.append(" ").append(aligner.matrix[i][j]).append(" ");
                } else matrix.append(aligner.matrix[i][j]).append(" ");

            }
            matrix.append("\n");
        }
        return matrix;
    }

    public void menuChanged(ActionEvent event) throws IOException {
        Node n = (Node) event.getSource();
        String id = n.getId();
        Parent root = null;
        if (id.equals("learnTab")) {
            root = FXMLLoader.load(getClass().getResource("FXMLpages/learnPage.fxml"));
        } else if (id.equals("homeTab")) {
            root = FXMLLoader.load(getClass().getResource("FXMLpages/homePage.fxml"));
        } else if (id.equals("aboutTab")) {
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

    public void changeCombo(ActionEvent event) {
        if (choiceBox.getValue().equals(globalAffine)) {
            gapExtField.setDisable(false);
            gapExtLabel.setOpacity(1);
            gapPenLabel.setText("Gap opening penalty");
        } else if (choiceBox.getValue().equals(global)) {
            gapExtField.setDisable(true);
            gapExtLabel.setOpacity(0.5);
            gapPenLabel.setText("Gap penalty");
        } else if (choiceBox.getValue().equals(semi)) {
            gapExtField.setDisable(true);
            gapExtLabel.setOpacity(0.5);
            gapPenLabel.setText("Gap penalty");

        } else if (choiceBox.getValue().equals(local)) {
            gapExtField.setDisable(true);
            gapExtLabel.setOpacity(0.5);
            gapPenLabel.setText("Gap penalty");
        }
    }
}




