package medialab.hangman;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class DictionaryPopUp {

    private static float perc1 = 0f, perc2 = 0f, perc3 = 0f;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    private static void calcPerc(ArrayList<String> words) {
        int len = words.size();
        for (String word : words) {
            if (word.length() == 6)
                perc1 += 1;
            else if (word.length() <= 9)
                perc2 += 1;
            else
                perc3 += 1;
        }
        perc1 = (perc1 / len) * 100;
        perc2 = (perc2 / len) * 100;
        perc3 = (perc3 / len) * 100;
    }

    public static void display(Dictionary d, boolean loaded) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Dictionary Statistics");
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setStyle("-fx-background-color: FBE7B3;");
        Text text = new Text();

        if (!loaded) {
            text.setText("It seems you haven't chosen any dictionary yet!");
            text.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.REGULAR, 20));
        } else {
            calcPerc(d.words);
            text.setText("Dictionary:  '" + d.dictionary_id + "'\n" +
                         "Words with 6 letters: " + df.format(perc1) +
                         "%\nWords with more than 6 and less than 10 letters: " +  df.format(perc2) +
                         "%\nWords with more than 10 letters: "  + df.format(perc3) + "%");
            text.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.REGULAR, 18));
        }

        Scene scene = new Scene(layout, 500, 300);
        window.setScene(scene);
        layout.setCenter(text);
        window.showAndWait();
    }
}
