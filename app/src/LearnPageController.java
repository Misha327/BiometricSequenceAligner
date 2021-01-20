package sample;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javafx.scene.image.ImageView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LearnPageController implements Initializable {
    @FXML
    private TextField first;
    @FXML
    private TextField second;
    @FXML
    private Text score;
    @FXML
    private Text firstResult;
    @FXML
    private Text secondResult;
    @FXML
    public VBox vbox;
    @FXML
    public HBox resultBox;
    @FXML
    public VBox resultLabelBox;
    @FXML
    private JFXCheckBox globalCheck;
    @FXML
    private JFXCheckBox semiGlobalCheck;
    @FXML
    private JFXCheckBox localCheck;
    @FXML
    private JFXComboBox<String> choiceBox;
    @FXML
    private StackPane parentContainer;
    @FXML
    private AnchorPane anchorRoot;
    @FXML
    private Button learnTab;
    @FXML
    private Text firstText;
    @FXML
    private Text secondText;
    @FXML
    private Text thirdText;
    @FXML
    private Button backButton;
    @FXML
    private Button nextButton;
    @FXML
    private ImageView imageBox;
    @FXML
    private ImageView formulaImageBox;
    @FXML
    private Button exitTab;

    private int slideIndex = 0;

    public void menuChanged(ActionEvent event) throws IOException {
        Node n = (Node) event.getSource();
        String id = n.getId();
        Parent root = null;
        if (id.equals("homeTab")) {
            root = FXMLLoader.load(getClass().getResource("FXMLpages/homePage.fxml"));
        } else if (id.equals("alignerTab")) {
            root = FXMLLoader.load(getClass().getResource("FXMLpages/alignerPage.fxml"));
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        learnTab.setStyle(null);
        learnTab.setStyle("-fx-border-color: #abeb34");
        learnTab.setStyle("-fx-border-width: 0px 0px 3px 0px");
        secondText.setOpacity(0);
        firstText.setOpacity(0);
        textAppear(firstText);
    }

    public void incrementLearnSlideCounter() {
        slideIndex++;
        if (slideIndex > 0) {
            backButton.setDisable(false);
        }
        if (slideIndex == 1) {
            secondText.setY(25);
            textAppear(secondText);
        } else if (slideIndex == 2) {
            firstText.setOpacity(0);
            secondText.setOpacity(0);
            secondText.setY(0);
            firstText.setText("Three methods are available based on what we want to align:\n" +
                    "• global alignment: find best match of both sequences in\n" +
                    "   their entirety\n" +
                    "• local alignment: find best subsequence match\n" +
                    "• semi-global alignment: find best match without penalizing " +
                    "gaps on the ends of the alignment\n");
            textAppear(firstText);
        } else if (slideIndex == 3) {
            secondText.setText("Some possible global alignments for ELV and VIS:\n" +
                    "ELV  -ELV  --ELV  ELV-\n" +
                    "VIS  VIS-  VIS--  -VIS\n" +
                    "Alignments are scored using \n" +
                    "• the gap penalty function \n" +
                    "-y(n) indicates cost of a gap of length n \n" +
                    "• substitution matrix\n" +
                    "-s(a,b) which indicates a score of aligning character a with character b");
            textAppear(secondText);
        } else if (slideIndex == 4) {
            firstText.setOpacity(0);
            secondText.setOpacity(0);
            firstText.setText("The score of an alignment is the sum of the scores for pairs of aligned characters plus the scores for gaps\n" +
                    "Given the following alignment :\n" +
                    "ELV-\n" +
                    "-VIS\n" +
                    "we would score it as such:\n" +
                    "g + s(L, V) + s(V, I) + g\n" +
                    "Using the blosum62 matrix and a gap penalty of -4 " +
                    "the score would sum to = -4 + 1 + 3 – 4 = -4\n");
            textAppear(firstText);
            imageBox.setImage(new Image("sample/Images/blosum62.png"));
        } else if (slideIndex == 5) {
            firstText.setOpacity(0);
            imageBox.setImage(null);
            firstText.setText("The basic operations of match, insert a gap in the upper sequence," +
                    "and insert a gap in the lower sequence are represented by a ↘, ↓ " +
                    "and →, respectively in the matrix. Any alignment can be represented " +
                    "by a path through this matrix using these elemental moves. Each " +
                    "move will be accompanied by the corresponding score, a gap penalty " +
                    "score for ↓ and →, and a substitution score for ↘.\n" +
                    "Note sliding one sequence relative to other is equivalent to inserting " +
                    "gaps at beginning of the sequence which means moving along the top " +
                    "row or left most column.");
            textAppear(firstText);
        } else if (slideIndex == 6) {
            secondText.setText("Consider two sequences X and Y, where xi is the ith residue of " +
                    "sequence X (the upper sequence) and yj is the jth residue of " +
                    "sequence Y (the lower sequence). Consider the matrix denoted, F, for " +
                    "which the value F(i,j) is the score of the best alignment of the " +
                    "subsequences up to xi and yj.\n");
            textAppear(secondText);
        } else if (slideIndex == 7) {
            textAppear(thirdText);
            formulaImageBox.setImage(new Image("sample/Images/matrixFormula.png"));

        } else if (slideIndex == 8) {
            firstText.setOpacity(0);
            secondText.setOpacity(0);
            thirdText.setOpacity(0);

            formulaImageBox.setImage(null);
            firstText.setText("So each cell in the matrix has the maximum value from the 3 " +
                    "surrounding cells." +
                    "To complete the algorithm we need the initial values. They are " +
                    "F(0,0)=0 " +
                    "F(i,0)=ig " +
                    "F(0,j)=jg " +
                    "The value in final cell is the best score of the global alignment. The " +
                    "alignment itself can be found by tracing back from the final cell. This is " +
                    "known as traceback. It involves moving back from the current cell at " +
                    "(i,j) to the previous neighbouring cell (i-1,j), (i,j-1) or (i-1,j-1) from which " +
                    "the score at F(i,j) derived.\n");
            textAppear(firstText);
        } else if (slideIndex == 9) {
            secondText.setY(-10);
            secondText.setText(
                    "The figure below shows the “dynamic programming matrix” for the " +
                            "alignment of the sequences HEAGAWGHEE and PAWHEAE using " +
                            "the BLOSUM50 substitution matrix and a gap penalty g=-8.");
            textAppear(secondText);
            formulaImageBox.setFitHeight(220);
            formulaImageBox.setY(-65);
            formulaImageBox.setImage(new Image("sample/Images/dpMatrix.png"));
        } else if (slideIndex == 10) {
            firstText.setOpacity(0);
            secondText.setOpacity(0);
            thirdText.setOpacity(0);
            formulaImageBox.setImage(null);
            firstText.setText("In the previous global alignment the whole of sequence X is aligned to " +
                    "the whole of sequence Y. If one sequence is longer than the other then gaps must be inserted " +
                    "either within the sequence of the shorter one or at the termini.\n\n" +
                    "The following algorithm doesn’t penalize overhanging ends." +
                    " This algorithm can be used in those cases when one sequence is " +
                    "contained in the other or they overlap.");
            textAppear(firstText);
        } else if (slideIndex == 11) {
            secondText.setY(-25);
            secondText.setText("F(i,0)=0 and F(0,j)=0 so the first row and column are filled with " +
                    "0’s not with an increasing gap penalty.\n" +
                    "The algorithm is the same as previously but traceback starts " +
                    "from the maximum score along the last row or column, and " +
                    "ends on the first row or column reached.");
            textAppear(secondText);
            formulaImageBox.setImage(new Image("sample/Images/semiGlobalAlgo.png"));
        } else if (slideIndex == 12) {
            firstText.setOpacity(0);
            secondText.setOpacity(0);
            thirdText.setOpacity(0);
            formulaImageBox.setImage(null);
            firstText.setText("This dynamic programming matrix gives the score as 25 compared to 1 " +
                    "for the algorithm where overhanging ends are scored as gaps.\n\n " +
                    "This is global alignment as we still consider the whole sequences even if " +
                    "overhanging ends are scored 0.");
            textAppear(firstText);
            imageBox.setImage(new Image("sample/Images/semiGlobalMatrix.png"));
        } else if (slideIndex == 13) {
            firstText.setOpacity(0);
            secondText.setOpacity(0);
            thirdText.setOpacity(0);
            imageBox.setImage(null);
            firstText.setText("More commonly we will want a local alignment, the best match between subsequences of x and y. " +
                    "The Smith-Waterman algorithm will provide that. \n" +
                    "The algorithm finds a pair of segments from each of two long sequences " +
                    "with the maximum score.\n" +
                    "\t\t\t( F(i-1,j-1)+s(xi,yj)\n" +
                    "F(i,j)= max \t| F(i-1,j)+g\n" +
                    "\t\t\t| F(i,j-1)+g\n" +
                    "\t\t\t( 0\n"
            );
            textAppear(firstText);
        } else if (slideIndex == 14) {
            secondText.setText("This means F(i,j)=0 is chosen if the other 3 choices are negative. Again " +
                    "the top row and left column are filled with 0’s. " +
                    "Traceback starts from the cell with the highest score (which can be" +
                    "anywhere within the DP matrix) and ends at the first cell with 0.");
            textAppear(secondText);
            formulaImageBox.setImage(new Image("sample/Images/localMatrix.png"));
        } else if (slideIndex == 15) {
            firstText.setOpacity(0);
            secondText.setOpacity(0);
            thirdText.setOpacity(0);
            formulaImageBox.setImage(null);
            firstText.setText("Up to here the examples used a linear gap penalty where:\n" +
                    "y(n) = ng\n" +
                    "The affine gap penalty punishes the opening of a gap more than " +
                    "the extension of an already opened gap.\n" +
                    "y(n) = g + (n - 1)e\n" +
                    "g gap opening penalty (-ve)\n" +
                    "e gap extension penalty (-ve)\n" +
                    "n gap length\n" +
                    "|e|<|g|\n");
            textAppear(firstText);
        } else if (slideIndex == 16) {
            secondText.setText("The score will now depend on the route into the previous cell. For example:");
            thirdText.setText("\nIn going from (i-1,j) to (i,j), if via red add g, if via green add e. So each cell needs to " +
                    "store 3 scores each derived from the 3 directions into the cell. The traceback principle applies.\n" +
                    "M(i,j)= max \t( M(i-1,j-1)+s(xi,yj),\n \t\t\tIx(i-1,j-1)+ s(xi,yj),\n \t\t\t( Iy(i-1,j-1)+ s(xi,yj))\n" +
                    "Ix(i,j)= max\t(M(i-1,j)+g red path, \n" +
                    "\t\t\t(Ix(i-1,j)+e green path\n" +
                    "Iy(i,j)= max \t( M(i,j-1)+g, Iy(i,j-1)+e)\n");
            formulaImageBox.setImage(new Image("sample/Images/affineAlgo.png"));
            formulaImageBox.setY(-120);
            formulaImageBox.setFitHeight(100);
            formulaImageBox.setFitWidth(230);
            textAppear(secondText);
            textAppear(thirdText);
            nextButton.setDisable(true);
        }
    }

    public void decrementLearnSlideCounter() {
        slideIndex--;

        if (slideIndex == 0) {
            backButton.setDisable(true);
            textDisappear(secondText);
        }
        else if (slideIndex == 1) {
            firstText.setOpacity(0);
            secondText.setY(25);

            firstText.setText("This section will provide insight to the algorithms at work in the \"Aligner\" section.\n" +
                    "\n" +
                    "The Sequence Alignment problem is one of the fundamental problems of Biological Sciences, aimed at " +
                    "finding the similarity of two amino-acid sequences. Comparing amino-acids is of prime " +
                    "importance to humans, since it gives vital information on evolution and development. " +
                    "Saul B. Needleman and Christian D. Wunsch devised a dynamic programming algorithm to the " +
                    "problem and got it published in 1970.");
            textAppear(firstText);

            secondText.setText("Sequences may have diverged from a common ancestor through various types of mutations:\n" +
                    "– substitutions (ACGA→ AGGA)\n" +
                    "– insertions (ACGA→ ACCGGAGA)\n" +
                    "– deletions (ACGGAGA→ AGA) \n" +
                    "the latter two will result in gaps in alignments");
            textAppear(secondText);
        } else if (slideIndex == 2) {
            textDisappear(secondText);
        } else if (slideIndex == 3) {
            firstText.setOpacity(0);
            secondText.setOpacity(0);
            firstText.setText("Three methods are available based on what we want to align:\n" +
                    "• global alignment: find best match of both sequences in\n" +
                    "   their entirety\n" +
                    "• local alignment: find best subsequence match\n" +
                    "• semi-global alignment: find best match without penalizing " +
                    "gaps on the ends of the alignment\n");
            secondText.setText("Some possible global alignments for ELV and VIS:\n" +
                    "ELV  -ELV  --ELV  ELV-\n" +
                    "VIS  VIS-  VIS--  -VIS\n" +
                    "Alignments are scored using \n" +
                    "• the gap penalty function \n" +
                    "-w(k) indicates cost of a gap of length k \n" +
                    "• substitution matrix\n" +
                    "-s(a,b) which indicates a score of aligning character a with character b");
            textAppear(secondText);
            textAppear(firstText);
            imageBox.setImage(null);
        } else if (slideIndex == 4) {
            firstText.setOpacity(0);
            secondText.setOpacity(0);
            firstText.setText("The score of an alignment is the sum of the scores for pairs of aligned characters plus the scores for gaps\n" +
                    "Given the following alignment :\n" +
                    "ELV-\n" +
                    "-VIS\n" +
                    "we would score it as such:\n" +
                    "g + s(L, V) + s(V, I) + g\n" +
                    "Using the blosum62 matrix and a gap penalty of -4\n" +
                    "the score would be = -4 + 1 + 3 – 4 = -4\n");
            textAppear(firstText);

            imageBox.setImage(new Image("sample/Images/blosum62.png"));
        } else if (slideIndex == 5) {
            textDisappear(secondText);
        } else if (slideIndex == 6) {
            textDisappear(thirdText);
            formulaImageBox.setImage(null);

        } else if (slideIndex == 7) {
            firstText.setOpacity(0);
            secondText.setOpacity(0);
            thirdText.setOpacity(0);
            secondText.setY(0);
            firstText.setText("The basic operations of match, insert a gap in the upper sequence," +
                    "and insert a gap in the lower sequence are represented by a ↘, ↓ " +
                    "and →, respectively in the matrix. Any alignment can be represented " +
                    "by a path through this matrix using these elemental moves. Each " +
                    "move will be accompanied by the corresponding score, a gap penalty " +
                    "score for ↓ and →, and a substitution score for ↘.\n" +
                    "Note sliding one sequence relative to other is equivalent to inserting " +
                    "gaps at beginning of the sequence which means moving along the top " +
                    "row or left most column.");
            secondText.setText("Consider two sequences X and Y, where xi is the ith residue of " +
                    "sequence X (the upper sequence) and yj is the jth residue of " +
                    "sequence Y (the lower sequence). Consider the matrix denoted, F, for " +
                    "which the value F(i,j) is the score of the best alignment of the " +
                    "subsequences up to xi and yj.\n");
            thirdText.setText("If F(i-1,j-1), F(i-1,j) and F(i,j-1) are known then F(i,j) can be calculated as:");
            textAppear(firstText);
            textAppear(secondText);
            textAppear(thirdText);
            formulaImageBox.setFitHeight(147);
            formulaImageBox.setY(0);

            formulaImageBox.setImage(new Image("sample/Images/matrixFormula.png"));
        } else if (slideIndex == 8) {
            textDisappear(secondText);
            formulaImageBox.setImage(null);
        } else if (slideIndex == 9) {
            firstText.setOpacity(0);
            secondText.setOpacity(0);
            thirdText.setOpacity(0);
            secondText.setY(-10);
            firstText.setText("So each cell in the matrix has the maximum value from the 3 " +
                    "surrounding cells." +
                    "To complete the algorithm we need an initial values. They are " +
                    "F(0,0)=0 " +
                    "F(i,0)=ig " +
                    "F(0,j)=jg " +
                    "The value in final cell is the best score of the global alignment. The " +
                    "alignment itself can be found by tracing back from the final cell. This is " +
                    "known as traceback. It involves moving back from the current cell at " +
                    "(i,j) to the previous neighbouring cell (i-1,j), (i,j-1) or (i-1,j-1) from which " +
                    "the score at F(i,j) derived.\n");
            secondText.setText(
                    "The figure below shows the “dynamic programming matrix” for the " +
                            "alignment of the sequences HEAGAWGHEE and PAWHEAE using " +
                            "the BLOSUM50 substitution matrix and a gap penalty g=-8.");
            formulaImageBox.setImage(new Image("sample/Images/dpMatrix.png"));
            textAppear(firstText);
            textAppear(secondText);
        } else if (slideIndex == 10) {
            textDisappear(secondText);
            formulaImageBox.setImage(null);
        } else if (slideIndex == 11) {
            firstText.setOpacity(0);
            secondText.setOpacity(0);
            imageBox.setImage(null);

            firstText.setText("In the previous global alignment the whole of sequence X is aligned to " +
                    "the whole of sequence Y. If one sequence is longer than the other then gaps must be inserted " +
                    "either within the sequence of the shorter one or at the termini.\n\n" +
                    "The following algorithm doesn’t penalize overhanging ends." +
                    " This algorithm can be used in those cases when one sequence is " +
                    "contained in the other or they overlap."
            );

            secondText.setY(-25);
            secondText.setText("F(i,0)=0 and F(0,j)=0 so the first row and column are filled with " +
                    "0’s not with an increasing gap penalty.\n" +
                    "The algorithm is the same as previously but traceback starts " +
                    "from the maximum score along the last row or column, and " +
                    "ends on the first row or column reached."
            );
            textAppear(secondText);
            textAppear(firstText);
            formulaImageBox.setImage(new Image("sample/Images/semiGlobalAlgo.png"));
        } else if (slideIndex == 12) {
            firstText.setOpacity(0);
            secondText.setOpacity(0);
            thirdText.setOpacity(0);
            formulaImageBox.setImage(null);
            firstText.setText("This dynamic programming matrix gives the score as 25 compared to 1 " +
                    "for the algorithm where overhanging ends are scored as gaps.\n\n " +
                    "This is global alignment as we still consider the whole sequences even if " +
                    "overhanging ends are scored 0.");

            textAppear(firstText);
            imageBox.setImage(new Image("sample/Images/semiGlobalMatrix.png"));
        } else if (slideIndex == 13) {
            textDisappear(secondText);
            formulaImageBox.setImage(null);
        } else if (slideIndex == 14) {
            firstText.setOpacity(0);
            secondText.setOpacity(0);
            thirdText.setOpacity(0);
//            imageBox.setImage(null);
            formulaImageBox.setFitHeight(220);
            formulaImageBox.setFitWidth(358);

            formulaImageBox.setY(-65);

            firstText.setText("More commonly we will want a local alignment, the best match between subsequences of x and y. " +
                    "The Smith-Waterman algorithm will provide that. \n" +
                    "The algorithm finds a pair of segments from each of two long sequences " +
                    "with the maximum score.\n" +
                    "\t\t\t( F(i-1,j-1)+s(xi,yj)\n" +
                    "F(i,j)= max \t| F(i-1,j)+g\n" +
                    "\t\t\t| F(i,j-1)+g\n" +
                    "\t\t\t( 0\n"
            );
            textAppear(firstText);

            secondText.setText("This means F(i,j)=0 is chosen if the other 3 choices are negative. Again " +
                    "the top row and left column are filled with 0’s. " +
                    "Traceback starts from the cell with the highest score (which can be" +
                    "anywhere within the DP matrix) and ends at the first cell with 0.");
            textAppear(secondText);
            formulaImageBox.setImage(new Image("sample/Images/localMatrix.png"));
        } else if (slideIndex == 15) {
            textDisappear(secondText);
            textDisappear(thirdText);
            formulaImageBox.setImage(null);
            nextButton.setDisable(false);

        }

    }

    private void textDisappear(Text text) {
        FadeTransition ft = new FadeTransition(Duration.millis(400), text);
        ft.setToValue(0);
        ft.setCycleCount(1);
        ft.setAutoReverse(true);

        ft.play();
    }

    private void textAppear(Text text) {
        FadeTransition ft = new FadeTransition(Duration.millis(400), text);
        ft.setToValue(1);
        ft.setCycleCount(1);
        ft.setAutoReverse(true);

        ft.play();
    }
}
