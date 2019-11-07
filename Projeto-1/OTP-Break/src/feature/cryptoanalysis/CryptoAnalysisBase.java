package feature.cryptoanalysis;

import feature.cryptoanalysis.semantic.SemanticAnalysis;
import model.Dictionary;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by thales on 15/04/17.
 */
public abstract class CryptoAnalysisBase {
    protected static final int PLAINTEXT_1 = 0;
    protected static final int PLAINTEXT_2 = 1;

    protected static Map<String, String> expansion(Dictionary dictionary, String strXor, Map.Entry<String, String> strsToExpand, int ptPosition, int plainText) {
        return null;
    }


    protected static void finalValidation(Map<String, String> finalPlainTextsMap, Dictionary dictionary, Dictionary forbiddenExpressions, String str1, String str2) {
        List<String> strsToValidate1 = Arrays.stream(str1.split(" |\\.|\\. |, |: |\\? "))
                .filter(w -> !w.isEmpty())
                .map((w) -> w.toLowerCase())
                .collect(Collectors.toList());
        List<String> strsToValidate2 = Arrays.stream(str2.split(" |\\.|\\. |, |: |\\? "))
                .filter(w -> !w.isEmpty())
                .map((w) -> w.toLowerCase())
                .collect(Collectors.toList());

        if (isValidSentence(strsToValidate1, dictionary) && isValidSentence(strsToValidate2, dictionary)
                && !SemanticAnalysis.hasForbiddenExpression(str1, forbiddenExpressions)
                && !SemanticAnalysis.hasForbiddenExpression(str2, forbiddenExpressions)
                && !SemanticAnalysis.existsInMap(finalPlainTextsMap, str1, str2)) {
            finalPlainTextsMap.put(str1, str2);
        }
    }

    protected static boolean isValidSentence(List<String> strsToValidate, Dictionary dictionary) {
        int validWord = 0;
        boolean isValid;

        for(String wordStr : strsToValidate){
            isValid = false;
            for(String word : dictionary.getWords()) {
                if(word.equals(wordStr)) {
                    isValid = true;
                    validWord++;
                    break;
                }
            }
            if (!isValid) break;
        }

        return (validWord == strsToValidate.size());
    }
}
