package feature.cryptoanalysis;

import model.Dictionary;
import util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by thales on 09/04/17.
 */
public class CryptoAnalysisStart extends CryptoAnalysisBase {
    public static Map<String, String> decrypt(Dictionary dictionary, Dictionary forbiddenExpressions, String strXor) {
        Map<String, String> plainTextMap;
        Map<String, String> aux = new HashMap<>();
        Map<String, String> finalPlainTextsMap = new HashMap<>();
        int cip1Position = 0;
        int cip2Position = 0;
        int numExpansions = 0;

        plainTextMap = firstExpansion(dictionary, strXor);
        numExpansions++;

        while (plainTextMap.size() > 0) {
            for (Map.Entry<String, String> endPlainTexts : plainTextMap.entrySet()) {
                final int p1Length = endPlainTexts.getKey().length();
                final int p2Length = endPlainTexts.getValue().length();
                if(!(endPlainTexts.getKey().substring(p1Length - 1, p1Length).equals(" ") && endPlainTexts.getValue().substring(p2Length - 1, p2Length).equals(" "))) {
                    if (numExpansions % 2 == PLAINTEXT_1) {
                        aux.putAll(expansion(dictionary, strXor, " ", endPlainTexts, cip1Position, PLAINTEXT_1));
                        aux.putAll(expansion(dictionary, strXor, ", ", endPlainTexts, cip1Position, PLAINTEXT_1));
                    } else {
                        aux.putAll(expansion(dictionary, strXor, " ", endPlainTexts, cip2Position, PLAINTEXT_2));
                        aux.putAll(expansion(dictionary, strXor, ", ", endPlainTexts, cip2Position, PLAINTEXT_2));
                    }
                } else {
                    finalValidation(finalPlainTextsMap, dictionary, forbiddenExpressions, endPlainTexts.getKey(), endPlainTexts.getValue());
                }
            }
            numExpansions++;
            plainTextMap.clear();
            plainTextMap.putAll(aux);
            aux.clear();
        }

        for(Map.Entry<String, String> endPlainTexts : finalPlainTextsMap.entrySet()) {
            System.out.println("Possível início: 1 - " + endPlainTexts.getKey() + "; 2 - " + endPlainTexts.getValue());
        }
        return finalPlainTextsMap;
    }

    private static Map<String,String> firstExpansion(Dictionary dictionary, String strXor) {
        Map<String, String> possibleCribs = new HashMap<>();
        Character[] initLetterArr = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        List<Character> initLetterList = new ArrayList<>();
        Collections.addAll(initLetterList, initLetterArr);
        String possibleChar;
        Character initLetter;
        for (int i = 0; i < initLetterList.size(); i++) {
            initLetter = initLetterList.get(i);
            possibleChar = StringUtils.xorSubStrings(strXor, String.valueOf(initLetter), 0);

            if (!(possibleChar.toCharArray()[0] >= 'A' && possibleChar.toCharArray()[0] <= 'Z')) {
                initLetterList.remove(initLetter);
                i--;
            } else {
                final String il = String.valueOf(initLetter);
                List<String> initStrings = dictionary.getWords()
                        .stream()
                        .filter(word -> word.startsWith(il.toLowerCase()))
                        .map((w) -> w.substring(0,1).toUpperCase() + w.substring(1))
                        .collect(Collectors.toList());

                for (String initString : initStrings) {
                    final String possible = StringUtils.xorSubStrings(strXor, initString + " ", 0);
                    if (StringUtils.isValidCrib(possible, dictionary)) {
                        System.out.println(initString + " - " + possible);
                        if(dictionary.getWords()
                                .stream()
                                .filter(word -> word.startsWith(possible.toLowerCase()))
                                .collect(Collectors.toList()).size() > 0) {
                            possibleCribs.put(initString + " ", possible);
                        }
                    }
                }
            }
        }

        System.out.println();
        System.out.println("Possíveis na primeira expansão: ");
        System.out.println();
        possibleCribs.forEach((w, p) -> System.out.println(w + " - " + p));

        return possibleCribs;
    }

    protected static Map<String, String> expansion(Dictionary dictionary, String strXor, String strToExpand, Map.Entry<String, String> strsToExpand, int ptPosition, int plainText) {
        Map<String, String> possibleCribs = new HashMap<>();
        List<String> possibleFinalWords;
        String possibleIncompleteStart;
        int cipPosition;

        String[] possibleIncompleteKeys = strsToExpand.getKey().split(" |\\. |, |\\? ");
        String[] possibleIncompleteValues = strsToExpand.getValue().split(" |\\. |, |\\? ");
        possibleIncompleteStart = (plainText == PLAINTEXT_1) ?
                possibleIncompleteKeys[possibleIncompleteKeys.length - 1]
                : possibleIncompleteValues[possibleIncompleteValues.length - 1];

        final String pis = possibleIncompleteStart;
        String[] possibleIncompleteWords;
        cipPosition = (plainText == PLAINTEXT_1) ?
                strsToExpand.getKey().lastIndexOf(' ') + 1
                : strsToExpand.getValue().lastIndexOf(' ') + 1;

        if (cipPosition == 0) {
            possibleFinalWords = dictionary.getWords()
                    .stream()
                    .filter(word -> word.startsWith(pis.toLowerCase()))
                    .map((w) -> w.substring(0, 1).toUpperCase() + w.substring(1))
                    .collect(Collectors.toList());
        } else {
            possibleFinalWords = dictionary.getWords()
                    .stream()
                    .filter(word -> word.startsWith(pis.toLowerCase()))
                    .collect(Collectors.toList());
        }

        for(String word : possibleFinalWords) {
            final String possible = StringUtils.xorSubStrings(strXor, word + strToExpand, cipPosition);

            if (StringUtils.isValidCrib(possible, dictionary)) {
                System.out.println(word + " - " + possible);
                possibleIncompleteWords = possible.split(" |\\. |, |\\? ");
                possibleIncompleteStart = possibleIncompleteWords[possibleIncompleteWords.length - 1];
                final String validPis = possibleIncompleteStart;
                if (dictionary.getWords()
                        .stream()
                        .filter(w -> w.startsWith(validPis.toLowerCase().trim()))
                        .collect(Collectors.toList()).size() > 0) {
                    if (plainText == PLAINTEXT_1) {
                        possibleCribs.put(mountExpansion(word, strsToExpand.getKey(), true) + strToExpand, mountExpansion(possible, strsToExpand.getValue(), false));
                    } else {
                        possibleCribs.put(mountExpansion(possible, strsToExpand.getKey(), false), mountExpansion(word, strsToExpand.getValue(), true) + strToExpand);
                    }
                }
            }
        }

        System.out.println();
        System.out.println("Possíveis com\"" + strToExpand + "\": ");
        System.out.println();
        possibleCribs.forEach((w, p) -> System.out.println(w + " - " + p));

        return possibleCribs;
    }

    private static String mountExpansion(String incompleteStart, String definitiveStart, boolean isWord) {
        String[] incomplete = incompleteStart.split(" ");
        String[] definitive = definitiveStart.split(" ");
        StringBuilder result = new StringBuilder();

        result.append(definitiveStart);

        if (!isWord) {
            if (incomplete.length > 1) {
                for (int i = 1;i < incomplete.length; i++) {
                    result.append(incomplete[i]);
                    if (i < incomplete.length - 1) {
                        result.append(" ");
                    }
                }

                if(incompleteStart.substring(incompleteStart.length() - 1, incompleteStart.length()).equals(" ")) {
                    result.append(" ");
                }
            }
        } else {
            result.append(incompleteStart.substring(definitive[definitive.length - 1].length()));
        }

        return result.toString();
    }
}
