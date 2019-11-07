package otpbreak;

import config.Config;
import feature.crib.CribDrag;
import feature.cryptoanalysis.CryptoAnalysis;
import model.CipherText;
import model.Dictionary;
import model.PlainTexts;
import util.Chronometer;
import util.CipherUtils;
import util.StringUtils;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static final int NUM_THREADS = 4;
    public static final int MANUAL_MODE = 1;
    public static final int SEMI_AUTOMATIC_MODE = 2;
    public static final int AUTOMATIC_MODE = 3;
    public static final int MODE = AUTOMATIC_MODE;

    public static void main(String[] args) {
        Chronometer chronometer = new Chronometer();
        chronometer.start();

        System.out.println("Obtendo cifra 1");
        File f1 = new File(args[0]);
        CipherText cipherText1 = new CipherText(f1.toURI());
        System.out.println("Obtendo cifra 2");
        File f2 = new File(args[1]);
        CipherText cipherText2 = new CipherText(f2.toURI());
        System.out.println("Obtendo dicionarios");
        //File dic = new File(Config.DIC_PATH_10000);
        //File dic = new File(Config.DIC_PATH_DICTIONARY);
        //File dic = new File(Config.DIC_PATH_DICTIONARY_FINAL);
        File dic = new File(Config.DIC_PATH_CORNCOB);
        //File dic = new File(Config.DIC_PATH_ENGLISH3);
        Dictionary dictionary = new Dictionary(dic);
        System.out.println("Obtendo expressões proibidas");
        File dicFExpr = new File(Config.DIC_PATH_FORBIDDEN);
        Dictionary forbiddenExpressions = new Dictionary(dicFExpr);

        System.out.println("Obtendo XOR das cifras");
        String xorCip = CipherUtils.xorCiphers(cipherText1, cipherText2);

        if(MODE == MANUAL_MODE) {
            System.out.println("Fazendo o Crib Dragging das palavras");
            CribDrag.cribDrag(xorCip, dictionary, NUM_THREADS);
        } else if (MODE == SEMI_AUTOMATIC_MODE) {

            List<String> possibleStrings1 = dictionary.getWords()
                    .stream()
                    .collect(Collectors.toList());

            List<String> possibleStrings2 = dictionary.getWords()
                    .stream()
                    //.filter(word -> word.endsWith("r"))
                    .collect(Collectors.toList());

            String possible1 = StringUtils.xorSubStrings(xorCip, "I can't in good conscience ", 0);
            String possible2 = StringUtils.xorSubStrings(xorCip, "Taken in its entirety, the ", 0);
            System.out.println(possible1.length());
            System.out.println(possible1);
            System.out.println(possible2.length());
            System.out.println(possible2);

            possible1 = StringUtils.xorSubStrings(xorCip, "allow the U.S. government ", 27);
            possible2 = StringUtils.xorSubStrings(xorCip, "Snowden archive led to an ", 27);
            System.out.println(possible1.length());
            System.out.println(possible1);
            System.out.println(possible2.length());
            System.out.println(possible2);

            possible1 = StringUtils.xorSubStrings(xorCip, "to destroy ", 53);
            possible2 = StringUtils.xorSubStrings(xorCip, "ultimately ", 53);
            System.out.println(possible1.length());
            System.out.println(possible1);
            System.out.println(possible2.length());
            System.out.println(possible2);

            possible1 = StringUtils.xorSubStrings(xorCip, "privacy, internet freedom and basic liberties for people around the world ", 64);
            possible2 = StringUtils.xorSubStrings(xorCip, "simple conclusion: the U.S. government had built a system that has as its ", 64);
            System.out.println(possible1.length());
            System.out.println(possible1);
            System.out.println(possible2.length());
            System.out.println(possible2);

            possible1 = StringUtils.xorSubStrings(xorCip, "with ", 138);
            possible2 = StringUtils.xorSubStrings(xorCip, "goal ", 138);
            System.out.println(possible1.length());
            System.out.println(possible1);
            System.out.println(possible2.length());
            System.out.println(possible2);

            possible1 = StringUtils.xorSubStrings(xorCip, "this massive ", 143);
            possible2 = StringUtils.xorSubStrings(xorCip, "the complete ", 143);
            System.out.println(possible1.length());
            System.out.println(possible1);
            System.out.println(possible2.length());
            System.out.println(possible2);

            possible1 = StringUtils.xorSubStrings(xorCip, "surveillance machine they're secretly building.", 156);
            possible2 = StringUtils.xorSubStrings(xorCip, "elimination of electronic privacy worldwide.", 156);
            System.out.println(possible1.length());
            System.out.println(possible1);
            System.out.println(possible2.length());
            System.out.println(possible2);

            PlainTexts plainTexts = new PlainTexts(possible1.length(), possible2.length());
            plainTexts.setPlainText1("I can't in good conscience allow the U.S. government to destroy privacy, internet freedom and basic liberties for people around the world with this massive surveillance machine they're secretly building.");
            plainTexts.setPlainText2("Taken in its entirety, the Snowden archive led to an ultimately simple conclusion: the U.S. government had built a system that has as its goal the complete elimination of electronic privacy worldwide.");

            CryptoAnalysis.makePlainTexts(plainTexts);
            CryptoAnalysis.makeKey(plainTexts, cipherText1, cipherText2);
        } else if (MODE == AUTOMATIC_MODE) {
            PlainTexts plainTexts = CryptoAnalysis.decrypt(dictionary, forbiddenExpressions,
                    cipherText1.getCipherText().length, cipherText2.getCipherText().length,
                    xorCip);

            CryptoAnalysis.makePlainTexts(plainTexts);
            CryptoAnalysis.makeKey(plainTexts, cipherText1, cipherText2);
        }

        chronometer.stop();
        System.out.println("Início: " + chronometer.getDatei().toString());
        System.out.println("Fim: " + chronometer.getDatef().toString());
        System.out.print("Tempo de execução: ");
        System.out.print(chronometer.getNanoTime().doubleValue());
        System.out.println(" segundos.");
    }
}
