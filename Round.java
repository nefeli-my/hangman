package medialab.hangman;

import java.util.*;
import java.util.stream.Collectors;
import static java.util.Collections.reverseOrder;


public class Round {
    public int id;
    private final ArrayList<String> words;
    private final String word;
    public char char_chosen;
    public int char_index;
    public boolean[] found;
    public ArrayList<HashMap<Character, Float>> prev_probabilities;
    public ArrayList<HashMap<Character, Float>> probabilities;
    public boolean was_successful;

    private Set<String> CalcSubset() {
        Set<String> candidate_words = new HashSet<>();
        for (String w: this.words) {
            if (this.word.length() == w.length()) {
                candidate_words.add(w);
                for (int i = 0; i < w.length(); i++)
                    if (found[i] && (w.charAt(i) != this.word.charAt(i)))
                        candidate_words.remove(w);
            }
        }
        return candidate_words;
    }

    private HashMap<Character, Float> CalcProb(int i, Set<String> candidate_words) {
        Set<Character> set_i = new HashSet<>();
        HashMap<Character, Float> prob = new HashMap<>();
        int n = candidate_words.size();
        for (String w: candidate_words)
            set_i.add(w.charAt(i));
        for (Character c: set_i) {
            prob.put(c, 0f);
            for (String w: candidate_words)
                if (w.charAt(i) == c) {
                    float prev = prob.get(c);
                    prob.put(c, prev+1);
                }
            float prev = prob.get(c);
            prob.put(c, prev/n);
        }
        return prob;
    }

    private HashMap<Character, Float> SortProb(HashMap<Character, Float> probs) {
        HashMap<Character, Float> sorted_probs =
                probs.entrySet().stream()
                        .sorted(reverseOrder(Map.Entry.comparingByValue()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (e1, e2) -> e1, LinkedHashMap::new));
        return sorted_probs;
    }

    private void CalcProbs() {
        Set<String> candidate_words = CalcSubset();
        this.probabilities = new ArrayList<>();
        for (int i =0 ; i < this.word.length(); i++) {
            if (this.found[i]) {
                HashMap<Character, Float> tmp = new HashMap<>();
                tmp.put(this.word.charAt(i), 0f);
                this.probabilities.add(tmp);
            } else {
                HashMap<Character, Float> prob = CalcProb(i, candidate_words);
                this.probabilities.add(SortProb(prob));
            }
        }
    }

    private void CheckMoveSuccess() {
        if (word.charAt(char_index) == char_chosen) {
            this.found[char_index] = true;
            this.was_successful = true;
        } else
            this.was_successful = false;
    }

    public Round(int id, String word, ArrayList<String> words, boolean[] found) {
        this.id = id;
        this.word = word;
        this.words = words;
        this.found = found;
        CalcProbs();
    }

    public Round(int id, String word, ArrayList<String> words, char c,
                 int char_index, boolean[] found, ArrayList<HashMap<Character, Float>> probabilities) {
        this.id = id;
        this.words = words;
        this.word = word;
        this.char_chosen = c;
        this.char_index = char_index;
        this.found = found;
        this.prev_probabilities = probabilities;
        CheckMoveSuccess();
        CalcProbs();
    }
}