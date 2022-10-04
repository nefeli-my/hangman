package medialab.hangman;

public class ConcludedGame {
    String word;
    int total_shots;
    boolean user_successful;

    public ConcludedGame(String word, int total_shots, boolean user_successful) {
        this.word = word;
        this.total_shots = total_shots;
        this.user_successful = user_successful;
    }

    public ConcludedGame(String word, boolean user_successful) {
        this.word = word;
        this.user_successful = user_successful;
        this.total_shots = -1;
    }
}
