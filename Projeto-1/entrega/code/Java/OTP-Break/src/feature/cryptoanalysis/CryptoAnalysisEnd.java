package feature.cryptoanalysis;

import model.Dictionary;
import util.StringUtils;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Created by thales on 09/04/17.
 */
public class CryptoAnalysisEnd extends CryptoAnalysisBase {
    public static Map<String,String> decrypt(Dictionary dictionary, Dictionary forbiddenExpressions, String strXor, int cip1len, int cip2len) {
        int cip1Position = cip1len;
        int cip2Position = cip2len;
        int numExpansions = 0;
        Map<String, String> plainTextMap;
        Map<String, String> aux = new HashMap<>();
        Map<String, String> finalPlainTextsMap = new HashMap<>();

        char pointXor = (char) ('.' ^ strXor.toCharArray()[cip2len - 1]);

        plainTextMap = firstExpansion(dictionary, strXor, pointXor, cip1len, cip2len);
        numExpansions++;

        while (plainTextMap.size() > 0) {
            for (Entry<String, String> endPlainTexts : plainTextMap.entrySet()) {
                if(!(endPlainTexts.getKey().substring(0,1).equals(" ") && endPlainTexts.getValue().substring(0,1).equals(" "))) {
                    if (numExpansions % 2 == PLAINTEXT_1) {
                        aux.putAll(expansion(dictionary, strXor, endPlainTexts, cip1Position, PLAINTEXT_1));
                        cip2Position = cip1len - endPlainTexts.getKey().length();
                    } else {
                        aux.putAll(expansion(dictionary, strXor, endPlainTexts, cip2Position, PLAINTEXT_2));
                        cip1Position = cip2len - endPlainTexts.getValue().length();
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

        for(Entry<String, String> endPlainTexts : finalPlainTextsMap.entrySet()) {
            System.out.println("Possível final: 1 - " + endPlainTexts.getKey() + "; 2 - " + endPlainTexts.getValue());
        }

        return finalPlainTextsMap;
    }

    private static Map<String, String> firstExpansion(Dictionary dictionary, String strXor, char pointXor, int cip1len, int cip2len) {
        Map<String, String> possibleCribs = new HashMap<>();
        int wordLength;

        List<String> possibleFinalWords = dictionary.getWords()
                .stream()
                .filter(word -> word.length() - (cip1len - cip2len) > 0 && word.toCharArray()[word.length() - (cip1len - cip2len)] == pointXor)
                .collect(Collectors.toList());


        for(String word : possibleFinalWords) {
            wordLength = word.length();
            final String possible = StringUtils.xorSubStrings(strXor, " " + word.substring(0, wordLength - (cip1len - cip2len - 1)), cip1len - wordLength - 2);
            if(StringUtils.isValidCrib(possible, dictionary)) {
                System.out.println(word + " - " + possible);
                if(dictionary.getWords()
                        .stream()
                        .filter(w -> w.endsWith(possible.substring(0, possible.length() - 1)))
                        .collect(Collectors.toList()).size() > 0) {
                    possibleCribs.put(" " + word + ".", possible);
                }
            }
        }

        System.out.println();
        System.out.println("Possíveis na primeira expansão: ");
        System.out.println();
        possibleCribs.forEach((w, p) -> System.out.println(w + " - " + p));

        return possibleCribs;
    }

    protected static Map<String, String> expansion(Dictionary dictionary, String strXor, Entry<String, String> strsToExpand, int ptPosition, int plainText) {
        Map<String, String> possibleCribs = new HashMap<>();
        int wordLength;
        List<String> possibleFinalWords;
        String possibleIncompleteEnd;
        char[] pieAux;
        int cipPosition;

        possibleIncompleteEnd = (plainText == PLAINTEXT_1) ?
                  strsToExpand.getKey().split(" |\\. |, |\\? ")[0]
                : strsToExpand.getValue().split(" |\\. |, |\\? ")[0];
        pieAux = possibleIncompleteEnd.toCharArray();

        final String pie;
        if (pieAux[pieAux.length - 1] == '.') {
            pie = possibleIncompleteEnd.substring(0, pieAux.length - 1);
            cipPosition = ptPosition - 1;
        } else {
            pie = possibleIncompleteEnd;
            cipPosition = ptPosition;
        }

        possibleFinalWords = dictionary.getWords()
                .stream()
                .filter(word -> word.endsWith(pie))
                .collect(Collectors.toList());

        for(String word : possibleFinalWords) {
            wordLength = word.length();
            final String possible = StringUtils.xorSubStrings(strXor, " " + word, cipPosition - wordLength - 1);
            if(StringUtils.isValidCrib(possible, dictionary)) {
                System.out.println(word + " - " + possible);
                possibleIncompleteEnd = possible.split(" |\\. |, |\\? ")[0];
                final String validPie = possibleIncompleteEnd;
                if(dictionary.getWords()
                        .stream()
                        .filter(w -> w.endsWith(validPie.trim()))
                        .collect(Collectors.toList()).size() > 0) {
                    if (plainText == PLAINTEXT_1) {
                        possibleCribs.put(" " + mountExpansion(word, strsToExpand.getKey(), true), mountExpansion(possible, strsToExpand.getValue(), false));
                    } else {
                        possibleCribs.put(mountExpansion(possible, strsToExpand.getKey(), false), " " + mountExpansion(word, strsToExpand.getValue(), true));
                    }
                }
            }
        }

        System.out.println();
        System.out.println("Possíveis: ");
        System.out.println();
        possibleCribs.forEach((w, p) -> System.out.println(w + " - " + p));

        return possibleCribs;
    }

    private static String mountExpansion(String incompleteEnd, String definitiveEnd, boolean isWord) {
        String[] incomplete = incompleteEnd.split(" ");
        String[] definitive = definitiveEnd.split(" ");
        String definitiveStart;
        StringBuilder result = new StringBuilder();
        int i = 0;

        if(incompleteEnd.endsWith(" ") && definitiveEnd.startsWith(" ")) {
            result.append(incompleteEnd);
            result.append(definitiveEnd.substring(1));
        } else {
            if (!isWord) {
                definitiveStart = (definitiveEnd.indexOf(' ') == 0) ? definitive[1] : definitive[0];

                while (!definitiveStart.startsWith(incomplete[i])) {
                    result.append(incomplete[i] + " ");
                    i++;
                }

                result.append(definitiveEnd.substring(1));
            } else {
                if (definitive.length > 1) {
                    result.append(incompleteEnd);
                    result.append(definitiveEnd.substring(definitive[0].length()));
                } else {
                    result.append(incompleteEnd + ".");
                }
            }
        }

        return result.toString();
    }
}
