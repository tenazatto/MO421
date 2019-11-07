package feature.crib;

import config.Config;
import model.Crib;
import model.Dictionary;
import thread.crib.CribDragThread;
import thread.ThreadMonitor;
import util.FileUtils;
import util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thales on 04/04/17.
 */
public class CribDrag {
    public static void cribDrag(String strXor, Dictionary dictionary, int numThreads) {
        int dictionarySize = dictionary.getWords().size();
        int partitionSize = (dictionarySize % numThreads == 0) ? dictionarySize / numThreads : (dictionarySize / numThreads) + 1;
        int startIndex;
        int endIndex;
        ArrayList<Thread> threads = new ArrayList<>();

        for (int thread = 0; thread < numThreads; thread++) {
            startIndex = thread * partitionSize;
            endIndex = (thread + 1) * partitionSize;
            threads.add(
                new Thread(new CribDragThread(strXor, dictionary, startIndex, endIndex, dictionarySize))
            );
            threads.get(thread).start();
        }

        ThreadMonitor.waitAllThreads(threads);
    }

    public static void cribDragPartitioned(String strXor, Dictionary dictionary, int startIndex, int endIndex) {
        List<Crib> cribs = new ArrayList<>();
        List<String> words = dictionary.getWords();
        String actualWord;
        int numWord;

        for (int i = startIndex; i < endIndex; i++) {
            actualWord = words.get(i);
            numWord =  i+1;
            cribs.addAll(getCribs(strXor, actualWord, dictionary));

            numWord++;
            System.out.println("Palavra " + numWord + " - " + actualWord + ": " + cribs.size());

            if(cribs.size() > 0) {
                makeFile(actualWord, cribs);
                cribs.clear();
            } else {
                System.out.println("Crib não feito da palavra: " + actualWord);
            }
        }
    }

    private static void makeFile(String word, List<Crib> wordCribs) {
        try {
            String startLetter = word.substring(0,1).toUpperCase();
            FileUtils.createDirectory(Config.CRIB_PATH);
            FileUtils.createDirectory(Config.CRIB_PATH + "/" + startLetter);

            String fileUri = Config.CRIB_PATH + "/" + startLetter + "/" + word + ".txt";

            FileUtils.createFile(fileUri);

            PrintWriter writer = new PrintWriter(fileUri, "UTF-8");
            for (Crib crib : wordCribs) {
                    writer.print(crib.getChrPosition());
                    writer.print(": ");
                    writer.println(crib.getCribValue());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Crib> getCribs(String strXor, String word, Dictionary dictionary) {
        List<Crib> cribs = new ArrayList<>();
        String possibleCrib;
        for(int i = 0; i < strXor.length() - word.length() + 1; i++) {
            possibleCrib = StringUtils.xorSubStrings(strXor,word, i);

            if(StringUtils.isValidCrib(possibleCrib, dictionary)) {
                Crib crib = new Crib(word, i, possibleCrib);
                cribs.add(crib);
            }
        }

        return cribs;
    }
}
