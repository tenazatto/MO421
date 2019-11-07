package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by thales on 04/04/17.
 */
public class Dictionary {
    private List<String> words;

    public Dictionary(File dictionary) {
        words = new ArrayList<>();

        try {
            Scanner scan = new Scanner(dictionary);
            scan.useDelimiter(System.getProperty("line.separator"));

            while (scan.hasNextLine()) {
                String word = scan.nextLine();
                System.out.println("Palavra adicionada: " + word);
                words.add(word);
            }
            scan.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
}
