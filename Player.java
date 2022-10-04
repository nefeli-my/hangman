package medialab.hangman;

public class Player {
    public int points = 0;
    public int missed_shots = 0;
    public int successful_shots = 0;
    public int shots;

    /**
     * Updates the fields of a Player object given a Round object 'round'.
     * If the given round is successful, the object's successful_shots are incremented
     * by one and the object's points are updated as specified by the instructions.
     * In case of an unsuccessful round, the missed_shots are incremented by one
     * and the object's points are decremented by 15 or set to zero.
     * Either way, the player's (total) shots are incremented by one.
     * @param  round  an object of the class Round
     * @see           class Round 
     */
    public void UpdateFields(Round round) {
        shots += 1;
        if (round.was_successful) {
            successful_shots += 1;
            int i = round.char_index;
            char c = round.char_chosen;
            float p = round.prev_probabilities.get(i).get(c);
            if (p >= 0.6)
                points += 5;
            else if (p >= 0.4)
                points += 10;
            else if (p >= 0.25)
                points += 15;
            else
                points += 30;
        } else {
            missed_shots += 1;
            points -= 15;
            if (points <= 0)
                points = 0;
        }
    }
}
