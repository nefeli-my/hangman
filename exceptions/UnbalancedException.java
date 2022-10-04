package medialab.hangman.exceptions;

public class UnbalancedException extends Exception {
    public UnbalancedException() {
        super("Less than 20% of words have more than 9 letters");
    }
}
