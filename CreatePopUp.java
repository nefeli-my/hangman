package medialab.hangman;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CreatePopUp {

    static String dictionary_id;
    static String library_id;
    private static Stage window;

    private static void create(String d_id, String lib_id) {
        dictionary_id = d_id;
        library_id = lib_id;
        window.close();
    }

    public static void display() {
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Create Dictionary");

        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: FBE7B3;");

        Text txt = new Text("Insert a dictionary id and a library id\n" + "to create a new dictionary:");
        txt.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.REGULAR, 18));
        layout.setTop(txt);

        HBox d_id = new HBox();
        Label l1 = new Label("dictionary id:");
        l1.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.REGULAR, 14));
        TextField tf1 = new TextField();
        d_id.getChildren().addAll(l1, tf1);
        d_id.setSpacing(10);

        HBox l_id = new HBox();
        Label l2 = new Label("library id:");
        l2.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.REGULAR, 14));
        TextField tf2 = new TextField();
        l_id.getChildren().addAll(l2, tf2);
        l_id.setSpacing(30);

        GridPane gp = new GridPane();
        gp.add(d_id,0,5);
        gp.add(l_id, 0, 6);
        gp.setVgap(5);
        layout.setCenter(gp);

        Button b = new Button("Create");
        b.setMaxSize(120, 120);
        b.setOnAction(e -> {create(tf1.getText(), tf2.getText());});
        layout.setBottom(b);

        layout.setMargin(txt, new Insets(40, 0, 0, 50));
        layout.setMargin(gp, new Insets(0, 0, 0, 95));
        layout.setMargin(b, new Insets(15, 0, 80, 137.5));

        Scene scene = new Scene(layout, 425, 250);
        window.setScene(scene);
        window.showAndWait();
    }
}