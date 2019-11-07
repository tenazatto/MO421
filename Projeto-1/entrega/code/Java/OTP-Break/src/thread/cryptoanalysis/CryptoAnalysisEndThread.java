package thread.cryptoanalysis;

import feature.cryptoanalysis.CryptoAnalysisEnd;
import model.Dictionary;

import java.util.Map;

/**
 * Created by thales on 09/04/17.
 */
public class CryptoAnalysisEndThread implements Runnable {
    private Map<String,String> plainTexts;
    private Dictionary dictionary;
    private Dictionary forbiddenExpressions;
    private String strXor;
    private int cip1len;
    private int cip2len;

    public CryptoAnalysisEndThread(Dictionary dictionary, Dictionary forbiddenExpressions, String strXor, int cip1len, int cip2len) {
        this.dictionary = dictionary;
        this.forbiddenExpressions = forbiddenExpressions;
        this.strXor = strXor;
        this.cip1len = cip1len;
        this.cip2len = cip2len;
    }

    @Override
    public void run() {
        plainTexts = CryptoAnalysisEnd.decrypt(dictionary, forbiddenExpressions, strXor, cip1len, cip2len);
    }
}
