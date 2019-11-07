package feature.cryptoanalysis;

import config.Config;
import feature.cryptoanalysis.semantic.SemanticAnalysis;
import model.*;
import util.CipherUtils;
import util.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by thales on 08/04/17.
 */
public class CryptoAnalysis {
    public static PlainTexts decrypt(Dictionary dictionary, Dictionary forbiddenExpressions, int cip1len, int cip2len, String strXor) {
        PlainTexts finalPlainTexts = null;
        PlainTexts plainTexts;

        Map<String,String> plainTextsStart = CryptoAnalysisStart.decrypt(dictionary, forbiddenExpressions, strXor);
        Map<String,String> plainTextsEnd = CryptoAnalysisEnd.decrypt(dictionary, forbiddenExpressions, strXor, cip1len, cip2len);

        //CryptoAnalysisMiddle.decrypt(dictionary, forbiddenExpressions, strXor, 27);
        //CryptoAnalysisMiddle.decrypt(dictionary, forbiddenExpressions, strXor, 53);
        //CryptoAnalysisMiddle.decrypt(dictionary, forbiddenExpressions, strXor, 64);
        //CryptoAnalysisMiddle.decrypt(dictionary, forbiddenExpressions, strXor, 138);
        //CryptoAnalysisMiddle.decrypt(dictionary, forbiddenExpressions, strXor, 143);
        for (Map.Entry<String,String> plainTextStart : plainTextsStart.entrySet()) {
            for (Map.Entry<String,String> plainTextEnd : plainTextsEnd.entrySet()) {
                int middleStartPos = plainTextStart.getValue().length() - 1;
                int middleFinalPos = cip2len - plainTextEnd.getValue().length();
                int startPos = middleStartPos;

                List<PartialPlainTexts> partialPlainTexts =
                        decryptMiddle(dictionary, forbiddenExpressions, strXor, startPos, middleFinalPos);

                System.out.println("Tentando achar textos planos");

                plainTexts = tryFind(plainTextsStart, partialPlainTexts, plainTextsEnd, forbiddenExpressions, middleStartPos, middleFinalPos, cip1len, cip2len);

                if(plainTexts != null) {
                    System.out.println("Textos planos encontrados!");
                    finalPlainTexts = plainTexts;
                    break;
                }
            }
            if (finalPlainTexts != null) break;
        }

        if (finalPlainTexts == null) finalPlainTexts = new PlainTexts(cip1len, cip2len);
        System.out.println("Texto plano 1: " + finalPlainTexts.getPlainText1());
        System.out.println("Texto plano 2: " + finalPlainTexts.getPlainText2());

        return finalPlainTexts;
    }

    private static PlainTexts tryFind(Map<String, String> plainTextsStart, List<PartialPlainTexts> partialPlainTexts, Map<String, String> plainTextsEnd, Dictionary forbiddenExpressions, int middleStartPos, int middleFinalPos, int cip1len, int cip2len) {
        int endPos = 0;
        boolean findIt = false;
        PlainTexts plainTexts = null;
        List<String> plainTextsList = new ArrayList<>();
        List<String> plainTextsListAux = new ArrayList<>();

        for (Map.Entry<String,String> plainTextStart : plainTextsStart.entrySet()) {
            plainTextsList.add(plainTextStart.getKey());
            plainTextsList.add(plainTextStart.getValue());
        }

        while (endPos < middleFinalPos && !findIt) {
            for (String plainTextsStr : plainTextsList) {
                List<PartialPlainTexts> possiblePpts = partialPlainTexts.stream()
                        .filter(ppt -> ppt.getStartPos() == plainTextsStr.length())
                        .collect(Collectors.toList());

                for (PartialPlainTexts possiblePpt : possiblePpts) {
                    plainTextsListAux.add(plainTextsStr + possiblePpt.getPpt1());
                    plainTextsListAux.add(plainTextsStr + possiblePpt.getPpt2());
                    endPos = possiblePpt.getEndPos();
                    if(endPos == middleFinalPos) {
                        findIt = true;
                    }
                }
            }

            plainTextsList.clear();
            plainTextsList.addAll(plainTextsListAux.stream()
                .filter(ppt -> !SemanticAnalysis.hasForbiddenExpression(ppt, forbiddenExpressions))
                .collect(Collectors.toList()));
            plainTextsListAux.clear();
        }

        plainTextsList = plainTextsList.stream()
                .filter(pt -> pt.length() - 1 == middleFinalPos)
                .collect(Collectors.toList());

        for (Map.Entry<String,String> plainTextEnd : plainTextsEnd.entrySet()) {
            for (String plainText : plainTextsList) {
                plainTextsListAux.add(plainText + plainTextEnd.getKey().substring(1));
                plainTextsListAux.add(plainText + plainTextEnd.getValue().substring(1));
            }
        }

        plainTextsList.clear();
        plainTextsList.addAll(plainTextsListAux.stream()
                .filter(ppt -> !SemanticAnalysis.hasForbiddenExpression(ppt, forbiddenExpressions))
                .collect(Collectors.toList()));
        plainTextsListAux.clear();

        if (plainTextsList.size() >= 2) {
            plainTexts = new PlainTexts(cip1len, cip2len);
            for (String finalPlainText : plainTextsList) {
                if (finalPlainText.length() == cip1len) {
                    plainTexts.setPlainText1(finalPlainText);
                }

                if (finalPlainText.length() == cip2len) {
                    plainTexts.setPlainText2(finalPlainText);
                }
            }
        }

        return plainTexts;
    }

    private static List<PartialPlainTexts> decryptMiddle(Dictionary dictionary, Dictionary forbiddenExpressions, String strXor, int startPos, int middleFinalPos) {
        List<PartialPlainTexts> returnList = new ArrayList<>();

        while (startPos < middleFinalPos) {
            List<PartialPlainTexts> partialPlainTexts =
                    CryptoAnalysisMiddle.decrypt(dictionary, forbiddenExpressions, strXor, startPos + 1);

            if (partialPlainTexts.size() == 0){
                break;
            }

            returnList.addAll(partialPlainTexts);

            for (PartialPlainTexts partialPlainText : partialPlainTexts) {
                startPos = partialPlainTexts.get(0).getEndPos();
                if (partialPlainText.getEndPos() == middleFinalPos) {
                    break;
                }
            }
        }

        return returnList;
    }

    public static void makePlainTexts(PlainTexts plainTexts){
        try {
            FileUtils.createDirectory(Config.PLAINTEXT_PATH);

            String fileUri1 = Config.PLAINTEXT_PATH + "/" + "plaintext1.txt";
            String fileUri2 = Config.PLAINTEXT_PATH + "/" + "plaintext2.txt";

            System.out.println("Gerando arquivo do texto plano 1");
            FileUtils.createFile(fileUri1);
            FileUtils.writeInFile(fileUri1, plainTexts.getPlainText1());

            System.out.println("Gerando arquivo do texto plano 2");
            FileUtils.createFile(fileUri2);
            FileUtils.writeInFile(fileUri2, plainTexts.getPlainText2());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void makeKey(PlainTexts plainTexts, CipherText cipher1, CipherText cipher2){
        try {
            Key key;
            FileUtils.createDirectory(Config.KEY_PATH);

            String fileUri = Config.KEY_PATH + "/" + "key.txt";

            FileUtils.createFile(fileUri);

            if(plainTexts.getPlainText1().length() > plainTexts.getPlainText2().length()) {
                key = CipherUtils.xorCipherWithPlainToFindKey(cipher1, plainTexts.getPlainText1());
            } else {
                key = CipherUtils.xorCipherWithPlainToFindKey(cipher2, plainTexts.getPlainText2());
            }

            System.out.println("Testando chave na cifra 1: ");
            System.out.println(CipherUtils.testDecrypt(cipher1, key));
            System.out.println("Testando chave na cifra 2:");
            System.out.println(CipherUtils.testDecrypt(cipher2, key));
            System.out.println("Tamanho da chave: " + key.getKey().length);
            System.out.println("Gerando arquivo de chave");
            FileUtils.writeInFile(fileUri, key.getKey());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
