package medialab.hangman;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;

public class RoundsPopUp {
    public static void display(ArrayList<ConcludedGame> games_stats) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Rounds Statistics");
        BorderPane bp = new BorderPane();
        bp.setStyle("-fx-background-color: ddc4c4;");
        VBox layout = new VBox();
        if (games_stats.size() == 0) {
            Text text = new Text("It seems you haven't played any games yet!");
            text.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.REGULAR, 20));
            layout.getChildren().add(text);
        }
        else {
            Text title = new Text("Information about the last 5 games you played:");
            title.setUnderline(true);
            title.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.REGULAR, 20));
            layout.getChildren().add(title);
            int i = Math.max(games_stats.size() - 5, 0);
            while (i < games_stats.size()) {
                Text text = new Text();
                String winner = games_stats.get(i).user_successful
                        ? "User"
                        : "Computer";
                if (games_stats.get(i).total_shots != -1) {
                    text.setText("Game " + (i + 1) + ":  \n" +
                            "Hidden word: " + games_stats.get(i).word +
                            "   Total shots: " + games_stats.get(i).total_shots +
                            "   Winner: " + winner
                    );
                } else {
                    text.setText("Game " + (i + 1) + ":  \n" +
                            "Hidden word: " + games_stats.get(i).word +
                            "   Winner: " + winner
                    );
                }
                text.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.REGULAR, 20));
                layout.getChildren().add(text);
                i++;
            }
        }
        layout.setSpacing(5);
        bp.setCenter(layout);
        bp.setMargin(layout, new Insets(50, 50, 50, 50));
        Scene scene = new Scene(bp, 600, 400);
        window.setScene(scene);
        window.showAndWait();
    }
}
