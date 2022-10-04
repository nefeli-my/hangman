package medialab.hangman;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.*;
import javafx.scene.paint.Color;

import java.util.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Hangman extends Application {
    Stage window;
    MenuBar menuBar;
    Dictionary dictionary;
    String word;
    boolean loaded = false;
    boolean[] found;
    ArrayList<ConcludedGame> games_stats = new ArrayList<>();

    private Scene InitializeWindow(MenuBar menuBar) {
        GridPane layout = new GridPane();
        layout.add(menuBar, 0, 0);
        layout.setVgap(5);
        layout.setHgap(5);
        Text instructions = new Text();
        instructions.setText("""
                To start the game, load or create a dictionary.\s
                To load an existing dictionary go to 'Application' ⮕ 'Load'.\s
                To create a new dictionary go to 'Application' ⮕ 'Create'.""");
        instructions.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.REGULAR, 23));
        Text title = new Text("Hangman");
        title.setUnderline(true);
        title.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.ITALIC, 40));
        layout.add(title, 1, 41);
        layout.add(instructions, 1, 42);
        layout.setStyle("-fx-background-color: DAE6F3;");
        return new Scene(layout, 900, 650);
    }

    private void ErrorWindow(Exception e) {
        BorderPane layout = new BorderPane();
        Text t = new Text("Oops, some error occurred!\n" +
                             "Error: ' " + e.getMessage() + "'.\n" +
                             "Please make sure there is an active dictionary, or " +
                             "try loading or creating \n a different dictionary."
                          );
        t.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.ITALIC, 25));
        t.setFill(Color.RED);
        layout.setCenter(t);
        layout.setTop(this.menuBar);
        layout.setStyle("-fx-background-color: EEE7D5;");
        layout.setMargin(t, new Insets(0, 0, 60, 0));
        Scene errorScene = new Scene(layout, 900, 650);
        this.window.setScene(errorScene);
        this.window.show();
    }

    private void ResultWindow(boolean succ) {
        BorderPane layout = new BorderPane();
        Text t = new Text();
        if (succ)
            t.setText("Congatulations, you won! \uD83C\uDF89");
        else
            t.setText("Sorry, you lost. \uD83D\uDE22 \n" +
                      "The disclosed word was: " + this.word);
        t.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.ITALIC, 25));
        layout.setCenter(t);
        layout.setTop(this.menuBar);
        layout.setStyle("-fx-background-color: EEE7D5;");
        layout.setMargin(t, new Insets(0, 0, 60, 0));
        Scene resultScene = new Scene(layout, 900, 500);
        this.window.setScene(resultScene);
        this.window.show();
    }

    private void CreateDictionary() {
        CreatePopUp.display();
        String dictionary_id = CreatePopUp.dictionary_id;
        String library_id = CreatePopUp.library_id;
        try {
            if (dictionary_id != null && library_id != null)  {
                this.loaded = true;
                this.dictionary = new Dictionary(dictionary_id, library_id);
                StartGame();
            }
        } catch (Exception e) {
            this.loaded = false;
            ErrorWindow(e);
        }
    }

    private void LoadDictionary() {
        LoadPopUp.display();
        String dictionary_id = LoadPopUp.dictionary_id;
        try {
            if (dictionary_id != null) {
                this.loaded = true;
                this.dictionary = new Dictionary(dictionary_id);
                StartGame();
            }
        } catch (Exception e) {
            this.loaded = false;
            ErrorWindow(e);
        }
    }

    private void ChooseWord() {
        try {
            ArrayList<String> words = this.dictionary.words;
            Random rand = new Random();
            this.word = words.get(rand.nextInt(words.size()));
        } catch (Exception e) {
            ErrorWindow(e);
        }
    }

    private HBox DrawLines(String[] chars) {
        HBox hb = new HBox();
        for (int i=0; i<chars.length; i++)
            if (this.found[i]) {
                Text t = new Text(chars[i]);
                t.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.ITALIC, 30));
                hb.getChildren().add(t);
            } else {
                Text t = new Text("__");
                t.setFont(Font.font("candara", FontWeight.BOLD, 25));
                hb.getChildren().add(t);
            }
        hb.setSpacing(10);
        return hb;
    }

    boolean[] CheckGameStatus(Player p) {
        boolean[] game_status = {true, true};  // {game_over, user_success}
        if (p.missed_shots > 5) {
            game_status[1] = false;
            games_stats.add(new ConcludedGame(this.word, p.shots, false));
            return game_status;
        }
        else {
            for (boolean b : this.found)
                if (!b) {
                    game_status[0] = false;
                    return game_status;
                }
        }
        games_stats.add(new ConcludedGame(this.word, p.shots, true));
        return game_status;
    }

    private void StartNewRound(int round_id, char c, int c_pos, Player player,
                               ArrayList<HashMap<Character, Float>> probabilities) {
        Round new_round = new Round(round_id, this.word, this.dictionary.words,
                                    c, c_pos, this.found, probabilities);
        this.found = new_round.found;
        player.UpdateFields(new_round);
        boolean[] game_status = CheckGameStatus(player);
        if (game_status[0])
            ResultWindow(game_status[1]);
        else {
            int i;
            for (i=0; i<this.found.length; i++)
                if (!this.found[i])
                    break;
            PlayGame(player, round_id + 1, i);
        }
    }

    private VBox ProbLists(Round r) {
        VBox vbox = new VBox();
        Text title = new Text("Possible characters per \n letter position:");
        title.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.REGULAR, 20));
        vbox.getChildren().add(title);
        ArrayList<HashMap<Character, Float>> probabilities = r.probabilities;
        for (int i=0; i<probabilities.size(); i++) {
            if (!this.found[i]) {
                HBox hb = new HBox();
                Text label = new Text(i + 1 + " :");
                label.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.REGULAR, 20));
                List<Character> l = new ArrayList<>(probabilities.get(i).keySet());
                hb.getChildren().addAll(label, new ChoiceBox<>(FXCollections.observableArrayList(l)));
                hb.setSpacing(10);
                vbox.getChildren().add(hb);
            }
        }
        vbox.setSpacing(15);
        return vbox;
    }

    private void PlayGame(Player p, int round_id, int char_pos)  {
        Round r = new Round(round_id, this.word, this.dictionary.words, this.found);
        int num_words = this.dictionary.words.size();
        String[] chars = this.word.split("");

        StackPane top = new StackPane();
        Text round_info = new Text("Available words in dictionary: " + num_words +
                "    Total points: " + p.points +
                "    Successful shots: " + p.successful_shots + "/" + p.shots);
        round_info.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.ITALIC, 20));
        top.getChildren().addAll(this.menuBar, round_info);

        String home_dir = System.getProperty("user.home");
        String IMAGE_PATH = home_dir + "\\Desktop\\medialab\\" + p.missed_shots + ".png";
        ImageView hangman_img = new ImageView();
        hangman_img.setImage(new Image(IMAGE_PATH));
        hangman_img.setFitHeight(300);
        hangman_img.setFitWidth(200);

        HBox center = DrawLines(chars);

        HBox char_position = new HBox();
        Label pos_label = new Label("Letter position:");
        pos_label.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.REGULAR, 20));
        ChoiceBox<Integer> pos_list = new ChoiceBox<>();
        for (int i=0; i<this.found.length; i++)
            if (!this.found[i])
                pos_list.getItems().add(i + 1);
        // char_pos parameter used to set default value to pos_list ChoiceBox
        // needed in order to avoid null exception in line 235 (and initialize char_list ChoiceBox)
        pos_list.setValue(char_pos+1);
        pos_list.getSelectionModel().selectedItemProperty().addListener((v, oldVal, newVal) -> PlayGame(p, round_id, newVal-1));
        char_position.setSpacing(10);
        char_position.getChildren().addAll(pos_label, pos_list);

        HBox char_guess = new HBox();
        Label guess_label = new Label("Your guess:");
        guess_label.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.REGULAR, 20));
        ChoiceBox<Character> char_list = new ChoiceBox<>();
        HashMap<Character, Float> probs = r.probabilities.get(pos_list.getValue()-1);
        for (Map.Entry<Character, Float> set :
                probs.entrySet()) {
            char_list.getItems().add(set.getKey());
        }
        char_guess.setSpacing(10);
        char_position.getChildren().addAll(guess_label, char_list);

        Button submit_guess = new Button("Make guess!");
        submit_guess.setFont(Font.font("candara", FontWeight.BOLD, FontPosture.REGULAR, 14));
        submit_guess.setOnAction(e -> StartNewRound(round_id, char_list.getValue(),
                                pos_list.getValue()-1, p, r.probabilities));

        HBox bottom = new HBox(char_position, char_guess, submit_guess);
        bottom.setSpacing(10);

        VBox vbox = ProbLists(r);

        BorderPane layout = new BorderPane();
        layout.setStyle("-fx-background-color: DAE6F3;");
        layout.setTop(top);
        layout.setLeft(hangman_img);
        layout.setCenter(center);
        layout.setRight(vbox);
        layout.setBottom(bottom);
        BorderPane.setMargin(center, new Insets(320, 20, 0, 30));
        BorderPane.setMargin(hangman_img, new Insets(50, 10, 0, 50));
        BorderPane.setMargin(bottom, new Insets(0, 100, 30, 200));
        BorderPane.setMargin(vbox, new Insets(40, 80, 0, 40));
        Scene scene = new Scene(layout, 925, 550);
        this.window.setScene(scene);
        this.window.show();
    }

    public void StartGame() {
        Player p = new Player();
        ChooseWord();
        this.found = new boolean[this.word.length()];
        PlayGame(p, 1, 0);
    }

    public void start(Stage primaryStage) {
        this.window = primaryStage;
        // Application menu
        Menu appMenu = new Menu("Application");
        MenuItem start = new MenuItem("Start       ");
        MenuItem load = new MenuItem("Load...       ");
        MenuItem create = new MenuItem("Create...       ");
        MenuItem exit = new MenuItem("Exit       ");
        start.setOnAction(e -> StartGame());
        load.setOnAction(e -> LoadDictionary());
        create.setOnAction(e -> CreateDictionary());
        exit.setOnAction(e -> primaryStage.close());
        appMenu.getItems().add(start);
        appMenu.getItems().add(load);
        appMenu.getItems().add(create);
        appMenu.getItems().add(exit);

        Menu detailsMenu = new Menu("Details");
        MenuItem d = new MenuItem("Dictionary...       ");
        MenuItem r = new MenuItem("Rounds...       ");
        MenuItem s = new MenuItem("Solution       ");
        d.setOnAction(e -> DictionaryPopUp.display(this.dictionary, this.loaded));
        r.setOnAction(e -> RoundsPopUp.display(this.games_stats));
        s.setOnAction(e -> {games_stats.add(new ConcludedGame(this.word, false)); ResultWindow(false);});
        detailsMenu.getItems().add(d);
        detailsMenu.getItems().add(r);
        detailsMenu.getItems().add(s);

        // Main menu bar
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(appMenu, detailsMenu);
        this.menuBar = menuBar;

        primaryStage.setTitle("MediaLab Hangman");
        Scene initialScene = InitializeWindow(menuBar);
        primaryStage.setScene(initialScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
