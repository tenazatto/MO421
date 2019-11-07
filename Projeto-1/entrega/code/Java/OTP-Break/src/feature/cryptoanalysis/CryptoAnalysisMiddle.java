package feature.cryptoanalysis;

import model.Dictionary;
import model.PartialPlainTexts;
import util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by thales on 09/04/17.
 */
public class CryptoAnalysisMiddle extends CryptoAnalysisBase {
    public static List<PartialPlainTexts> decrypt(Dictionary dictionary, Dictionary forbiddenExpressions, String strXor, int startPos) {
        Map<String, String> plainTextMap;
        Map<String, String> aux = new HashMap<>();
        Map<String, String> finalPlainTextsMap = new HashMap<>();
        int numExpansions = 0;

        plainTextMap = firstExpansion(dictionary, strXor, startPos);
        numExpansions++;

        while (plainTextMap.size() > 0) {
            for (Map.Entry<String, String> endPlainTexts : plainTextMap.entrySet()) {
                final int p1Length = endPlainTexts.getKey().length();
                final int p2Length = endPlainTexts.getValue().length();
                if(!(endPlainTexts.getKey().substring(p1Length - 1, p1Length).equals(" ") && endPlainTexts.getValue().substring(p2Length - 1, p2Length).equals(" "))
                        && (endPlainTexts.getKey().length() + startPos < strXor.length())) {
                    if (numExpansions % 2 == PLAINTEXT_1) {
                        aux.putAll(expansion(dictionary, strXor, " ", endPlainTexts, startPos, PLAINTEXT_1));
                        aux.putAll(expansion(dictionary, strXor, ", ", endPlainTexts, startPos, PLAINTEXT_1));
                        aux.putAll(expansion(dictionary, strXor, ": ", endPlainTexts, startPos, PLAINTEXT_1));
                    } else {
                        aux.putAll(expansion(dictionary, strXor, " ", endPlainTexts, startPos, PLAINTEXT_2));
                        aux.putAll(expansion(dictionary, strXor, ", ", endPlainTexts, startPos, PLAINTEXT_2));
                        aux.putAll(expansion(dictionary, strXor, ": ", endPlainTexts, startPos, PLAINTEXT_2));
                    }
                } else {
                    finalValidation(finalPlainTextsMap, dictionary, forbiddenExpressions, endPlainTexts.getKey(), endPlainTexts.getValue());
                }
            }
            numExpansions++;
            plainTextMap.clear();
            for(Map.Entry<String, String> plainTexts : aux.entrySet()) {
                if(validWords(dictionary, plainTexts.getKey(), plainTexts.getValue())) {
                    plainTextMap.put(plainTexts.getKey(), plainTexts.getValue());
                }
            }
            aux.clear();
        }

        for(Map.Entry<String, String> endPlainTexts : finalPlainTextsMap.entrySet()) {
            System.out.println("Possível meio: 1 - " + endPlainTexts.getKey() + "; 2 - " + endPlainTexts.getValue());
        }
        return makePartialPlainTexts(finalPlainTextsMap, startPos);
    }

    private static boolean validWords(Dictionary dictionary, String str1, String str2) {
        List<String> strsToValidate1 = Arrays.stream(str1.split(" |\\.|\\. |, |: |\\? "))
                .filter(w -> !w.isEmpty())
                .map((w) -> w.toLowerCase())
                .collect(Collectors.toList());
        List<String> strsToValidate2 = Arrays.stream(str2.split(" |\\.|\\. |, |: |\\? "))
                .filter(w -> !w.isEmpty())
                .map((w) -> w.toLowerCase())
                .collect(Collectors.toList());
        strsToValidate1.remove(strsToValidate1.size() - 1);
        strsToValidate2.remove(strsToValidate2.size() - 1);

        return (strsToValidate1.size() == 0 || strsToValidate1.size() == 0) ? true
                : isValidSentence(strsToValidate1, dictionary) && isValidSentence(strsToValidate2, dictionary);
    }

    private static List<PartialPlainTexts> makePartialPlainTexts(Map<String, String> finalPlainTextsMap, int startPos) {
        List<PartialPlainTexts> ppts = new ArrayList<>();
        finalPlainTextsMap.forEach((ppt1, ppt2) -> ppts.add(new PartialPlainTexts(ppt1, ppt2, startPos, startPos + ppt1.length() - 1)));

        return ppts;
    }

    private static Map<String,String> firstExpansion(Dictionary dictionary, String strXor, int startPos) {
        Map<String, String> possibleCribs = new HashMap<>();
        Character[] initLetterArr = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
                'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        List<Character> initLetterList = new ArrayList<>();
        Collections.addAll(initLetterList, initLetterArr);
        String possibleChar;
        Character initLetter;
        for (int i = 0; i < initLetterList.size(); i++) {
            initLetter = initLetterList.get(i);
            possibleChar = StringUtils.xorSubStrings(strXor, String.valueOf(initLetter), startPos);

            if (!(possibleChar.toCharArray()[0] >= 'A' && possibleChar.toCharArray()[0] <= 'Z')
                    && !(possibleChar.toCharArray()[0] >= 'a' && possibleChar.toCharArray()[0] <= 'z')) {
                initLetterList.remove(initLetter);
                i--;
            } else {
                final String il = String.valueOf(initLetter);
                List<String> initStrings = dictionary.getWords()
                        .stream()
                        .filter(word -> word.startsWith(il.toLowerCase()))
                        .map((w) -> {
                            if (il.toCharArray()[0] >= 'A' && il.toCharArray()[0] <= 'Z') {
                                return w.substring(0, 1).toUpperCase() + w.substring(1);
                            } else {
                                return w;
                            }
                        })
                        .collect(Collectors.toList());

                for (String initString : initStrings) {
                    final String possible = StringUtils.xorSubStrings(strXor, initString + " ", startPos);
                    if (StringUtils.isValidCrib(possible, dictionary)) {
                        System.out.println(initString + " - " + possible);
                        if(dictionary.getWords()
                                .stream()
                                .filter(word -> word.startsWith(possible.toLowerCase().trim()))
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

    protected static Map<String, String> expansion(Dictionary dictionary, String strXor, String strToExpand, Map.Entry<String, String> strsToExpand, int startPos, int plainText) {
        Map<String, String> possibleCribs = new HashMap<>();
        List<String> possibleFinalWords;
        String possibleIncompleteStart;
        int cipPosition;

        String[] possibleIncompleteKeys = strsToExpand.getKey().split(" |\\. |, |: |\\? ");
        String[] possibleIncompleteValues = strsToExpand.getValue().split(" |\\. |, |: |\\? ");
        possibleIncompleteStart = (plainText == PLAINTEXT_1) ?
                possibleIncompleteKeys[possibleIncompleteKeys.length - 1]
                : possibleIncompleteValues[possibleIncompleteValues.length - 1];

        final String pis = possibleIncompleteStart;
        String[] possibleIncompleteWords;
        cipPosition = (plainText == PLAINTEXT_1) ?
                strsToExpand.getKey().lastIndexOf(' ') + 1 + startPos
                : strsToExpand.getValue().lastIndexOf(' ') + 1 + startPos;

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
                    .map(word -> {
                        if (word.toUpperCase().startsWith(pis)) {
                            return word.toUpperCase();
                        } else {
                            return word;
                        }
                    })
                    .collect(Collectors.toList());
        }

        for(String word : possibleFinalWords) {
            final String possible = StringUtils.xorSubStrings(strXor, word + strToExpand, cipPosition);

            if (StringUtils.isValidCrib(possible, dictionary)) {
                System.out.println(word + " - " + possible);
                possibleIncompleteWords = possible.split(" |\\. |, |: |\\? ");
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
