package util;

import model.Dictionary;

import java.util.List;

/**
 * Created by thales on 05/04/17.
 */
public class StringUtils {
    public static String xorSubStrings(String majorString, String minorString, int posMajor) {
        char[] majorCa = majorString.toCharArray();
        char[] minorCa = minorString.toCharArray();
        int strSubLen = minorString.length();

        char[] strXor = new char[strSubLen];

        for(int i = 0; i < strSubLen && (posMajor + i) < majorString.length(); i++) {
            int xor = majorCa[posMajor + i] ^ minorCa[i];
            strXor[i] = (char) xor;
        }

        return String.valueOf(strXor);
    }

    public static boolean isValidCrib(String cribValue, Dictionary dictionary) {
        char[] cribChars = cribValue.toCharArray();

        for (char c : cribChars) {
            int ascii = (int) c;

            if(!isValidAlphabet(ascii)) {
                return false;
            }
        }

        /*if(!isValidPossibleWordsInCrib(cribValue, dictionary)) {
            return false;
        }*/

        return true;
    }

    public static boolean isValidLetters(String cribValue, Dictionary dictionary) {
        char[] cribChars = cribValue.toCharArray();

        for (char c : cribChars) {
            int ascii = (int) c;

            if(!isValidAlphabetLetter(ascii)) {
                return false;
            }
        }

        return true;
    }

    private static boolean isValidAlphabetLetter(int ascii) {
        return (ascii >= 65 && ascii <= 90) || (ascii >= 97 && ascii <= 122) || ascii == 32;
    }

    private static boolean isValidAlphabet(int ascii) {
        return (ascii >= 32 && ascii <= 34) || (ascii >= 39 && ascii <= 41)
                || (ascii >= 44 && ascii <= 47)
                || ascii == 58 || ascii == 59 || ascii == 63
                || ascii == 91 || ascii == 93
                || (ascii >= 65 && ascii <= 90) || (ascii >= 97 && ascii <= 122);
    }
    private static boolean isValidPossibleWordsInCrib(String cribValue, Dictionary dictionary){
        return isValidPossibleWordsInCrib(cribValue, dictionary.getWords());
    }
    private static boolean isValidPossibleWordsInCrib(String cribValue, List<String> possibleWords){
        String[] wordsInCrib = cribValue.split(" |\\. |, |! |\\? |: ");
        int validWords = 0;
        for (int i = 0; i < wordsInCrib.length; i++) {
            if(wordsInCrib[i].equals("")) {
                validWords++;
            } else {
                for (String possibleWord : possibleWords) {
                    if (wordsInCrib.length > 1) {
                        if (i == 0) {
                            if (possibleWord.endsWith(wordsInCrib[i]) || wordsInCrib[i].equals(possibleWord)) {
                                validWords++;
                                break;
                            }
                        } else if (i == wordsInCrib.length - 1) {
                            if (possibleWord.startsWith(wordsInCrib[i]) || wordsInCrib[i].equals(possibleWord)) {
                                validWords++;
                                break;
                            }
                        } else {
                            if (wordsInCrib[i].equals(possibleWord)) {
                                validWords++;
                                break;
                            }
                        }
                    } else {
                        if (possibleWord.contains(wordsInCrib[i]) || wordsInCrib[i].equals(possibleWord)) {
                            validWords++;
                            break;
                        }
                    }
                }
            }
        }

        return (validWords == wordsInCrib.length);
    }

    public static boolean isValidPossibleWords(String textValue, List<String> possibleWords) {
        return isValidPossibleWords(textValue.split(" |\\. |, |! |\\? |: "), possibleWords);
    }

    public static boolean isValidPossibleWords(String[] wordsInText, List<String> possibleWords) {
        int validWords = 0;

        for (int i = 0; i < wordsInText.length; i++) {
            if (wordsInText[i].equals("")) {
                validWords++;
            } else {
                for (String possibleWord : possibleWords) {
                    if(i == wordsInText.length - 1) {
                        if (possibleWord.startsWith(wordsInText[i]) || wordsInText[i].equals(possibleWord)) {
                            validWords++;
                            break;
                        }
                    } else {
                        if (wordsInText[i].equals(possibleWord)) {
                            validWords++;
                            break;
                        }
                    }
                }
            }
        }
        return (validWords == wordsInText.length);
    }
}
