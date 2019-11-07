package thread.crib;

import feature.crib.CribDrag;
import model.Dictionary;

/**
 * Created by thales on 08/04/17.
 */
public class CribDragThread implements Runnable {
    private String strXor;
    private Dictionary dictionary;
    private int startIndex;
    private int endIndex;
    private int dictionarySize;

    public CribDragThread(String strXor, Dictionary dictionary, int startIndex, int endIndex, int dictionarySize) {
        this.strXor = strXor;
        this.dictionary = dictionary;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.dictionarySize = dictionarySize;
    }

    @Override
    public void run() {
        CribDrag.cribDragPartitioned(strXor, dictionary, startIndex, (endIndex < dictionarySize) ? endIndex : dictionarySize);
    }
}
