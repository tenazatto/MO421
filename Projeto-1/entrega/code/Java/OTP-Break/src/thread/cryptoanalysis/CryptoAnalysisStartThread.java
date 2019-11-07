package thread.cryptoanalysis;

import feature.cryptoanalysis.CryptoAnalysisStart;
import model.Dictionary;

import java.util.Map;

/**
 * Created by thales on 09/04/17.
 */
public class CryptoAnalysisStartThread implements Runnable {
    private Map<String,String> plainTexts;
    private Dictionary dictionary;
    private Dictionary forbiddenExpressions;
    private String strXor;

    public CryptoAnalysisStartThread(Dictionary dictionary, Dictionary forbiddenExpressions, String strXor) {
        this.dictionary = dictionary;
        this.forbiddenExpressions = forbiddenExpressions;
        this.strXor = strXor;
    }

    @Override
    public void run() {
        plainTexts = CryptoAnalysisStart.decrypt(dictionary, forbiddenExpressions, strXor);
    }
}
