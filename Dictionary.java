package medialab.hangman;
import java.io.*;
import java.net.*;
import java.util.*;
import org.json.JSONObject;
import medialab.hangman.exceptions.*;

public class Dictionary {
    public String dictionary_id;
    public ArrayList<String> words;
    public File file;

    private static String Read(Reader re) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = re.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    private ArrayList<String> GetWordsFromUrl(String library_id) throws IOException {
        String url = "https://openlibrary.org/works/" + library_id;
        URL book = new URL(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                book.openStream()));
        String text = Read(in);
        in.close();

        JSONObject book_json = new JSONObject(text);
        JSONObject desc_json = new JSONObject((book_json.get("description")).toString());
        String value = (String) desc_json.get("value");

        String[] words = value.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll("[^\\w]", "");
            words[i] = words[i].toUpperCase();
        }
        return new ArrayList<>(Arrays.asList(words));
    }

    private ArrayList<String> GetWordsFromFile() throws IOException {
        ArrayList<String> words = new ArrayList<>();
        BufferedReader br
                = new BufferedReader(new FileReader(this.file));
        String st;
        while ((st = br.readLine()) != null)
            words.add(st);
        return words;
    }

    private void FilterWords() {
        Set<String> set = new HashSet<>();
        List<String> to_remove = new ArrayList<>();
        for (String word: this.words) {
            if (!set.add(word)) {
                to_remove.add(word);
                continue;
            }
            if (word.length() < 6)
                to_remove.add(word);
        }
        this.words.removeAll(to_remove);
    }

    private void CheckExceptions() throws InvalidCountException,
            InvalidRangeException, UnbalancedException, UndersizeException {
        Set<String> set = new HashSet<>();
        int c = 0;
        for (String word : this.words) {
            if (!set.add(word))
                throw new InvalidCountException();
            if (word.length() < 6)
                throw new InvalidRangeException();
            if (word.length() >= 9)
                c++;
        }
        float per = (float) (c / this.words.size());
        if (per < 0.2)
            throw new UnbalancedException();
        if (this.words.size() < 20)
            throw new UndersizeException();
    }

    private boolean AlreadyExisted() throws IOException {
        String dir = System.getProperty("user.home");
        String dir_ = dir + "\\Desktop\\medialab";
        String filename = "hangman_" + this.dictionary_id + ".txt";
        File dictionary = new File(dir_, filename);
        if (dictionary.createNewFile()) {
            this.file = dictionary;
            return false;
        } else
            return true;
    }

    private void CreateFile() throws IOException {
        FileWriter dictWriter = new FileWriter(this.file);
        int i;
        for (i=0; i < this.words.size()-1; i++) {
            dictWriter.write(this.words.get(i));
            dictWriter.write('\n');
        }
        dictWriter.write(this.words.get(i));
        dictWriter.close();
    }

    private boolean LoadFile() {
        boolean found = false;
        String dir = System.getProperty("user.home");
        String dir_ = dir + "\\Desktop\\medialab";
        File folder = new File(dir_);
        String filename = "hangman_" + this.dictionary_id + ".txt";
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.getName().equals(filename)) {
                this.file = file;
                found = true;
            }
        }
        return found;
    }

    Dictionary(String dictionary_id, String lib_id) throws Exception {
        this.dictionary_id = dictionary_id;
        String library_id = lib_id + ".json";
        this.words = GetWordsFromUrl(library_id);
        FilterWords();
        CheckExceptions();
        if (!AlreadyExisted())
            CreateFile();
        else
            LoadFile();
    }

    Dictionary(String dictionary_id) throws Exception {
        this.dictionary_id = dictionary_id;
        if (LoadFile())
            this.words = GetWordsFromFile();
        else
            throw new Exception("Dictionary not found");
    }

    public static void main (String[] args) {
        System.out.println("Dictionary loaded!");
    }
}