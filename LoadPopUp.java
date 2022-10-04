package medialab.hangman;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoadPopUp {

    public static String dictionary_id;
    private static Stage window;

    private static void load(String id) {
        dictionary_id = id;
        window.close();
    }

    public static void display() {

        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Load Dictionary");
        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: F0E2DF;");

        Text txt = new Text("Insert a dictionary id to load a dictionary:");
        txt.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.REGULAR, 18));
        layout.setTop(txt);

        HBox tf = new HBox();
        Label l = new Label("id:  ");
        l.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.REGULAR, 14));
        TextField id = new TextField();
        tf.getChildren().addAll(l, id);
        tf.setSpacing(10);
        layout.setCenter(tf);

        Button b = new Button("Load");
        b.setMaxSize(120, 120);
        b.setOnAction(e -> load(id.getText()));
        layout.setBottom(b);

        layout.setMargin(txt, new Insets(40, 0, 0, 50));
        layout.setMargin(tf, new Insets(15, 0, 0, 95));
        layout.setMargin(b, new Insets(15, 0, 80, 137.5));

        Scene scene = new Scene(layout, 420, 180);
        window.setScene(scene);
        window.showAndWait();
    }
}
