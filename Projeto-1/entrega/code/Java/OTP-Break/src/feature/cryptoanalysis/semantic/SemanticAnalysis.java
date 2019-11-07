package feature.cryptoanalysis.semantic;

import config.Config;
import model.Dictionary;

import java.io.File;
import java.util.Map;

/**
 * Created by thales on 15/04/17.
 */
public class SemanticAnalysis {
    public static boolean hasForbiddenExpression(String str, Dictionary forbiddenExpressions){
        for (String expression : forbiddenExpressions.getWords()) {
            if(str.contains(expression)) {
                return true;
            }
        }

        return false;
    }

    public static boolean existsInMap(Map<String, String> finalPlainTextsMap, String str1, String str2) {
        for (Map.Entry<String, String> finalPlainTexts : finalPlainTextsMap.entrySet()) {
            if((finalPlainTexts.getKey().equals(str1) && finalPlainTexts.getValue().equals(str2))
                || (finalPlainTexts.getKey().equals(str2) && finalPlainTexts.getValue().equals(str1))) {
                return true;
            }
        }
        return false;
    }
}
