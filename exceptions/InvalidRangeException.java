package medialab.hangman.exceptions;

public class InvalidRangeException extends Exception {
    public InvalidRangeException() {
        super("Words with less than 6 letters contained");
    }
}
