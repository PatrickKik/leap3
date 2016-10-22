package nl.patrickkik.lysanne.leap3;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@Component
class Configuration {

    private final Map<Test, String> orderColumns = new EnumMap<>(Test.class);
    private final Map<Test, String> scoreColumns = new EnumMap<>(Test.class);
    private final Map<Test, String> reactionTimeColumns = new EnumMap<>(Test.class);
    private final Map<Test, String> wordColumns = new EnumMap<>(Test.class);
    private final Map<Integer, String> descrXRuleColumns = new HashMap<>();
    private final Map<Integer, String> descrXRuleBasisColumns = new HashMap<>();
    private final Map<Integer, String> descrXRuleDutchColumns = new HashMap<>();
    private final Map<Integer, String> descrIRuleColumns = new HashMap<>();
    private final Map<Integer, String> descrIRuleBasisColumns = new HashMap<>();
    private final Map<Integer, String> descrIRuleDutchColumns = new HashMap<>();

    Configuration() {
        orderColumns.put(Test.TEST_A1, "GN");
        orderColumns.put(Test.TEST_A2, "GQ");
        orderColumns.put(Test.TEST_A3, "GT");
        orderColumns.put(Test.TEST_A4, "GW");
        orderColumns.put(Test.TEST_B1, "GZ");
        orderColumns.put(Test.TEST_B2, "HC");
        orderColumns.put(Test.TEST_B3, "HF");
        orderColumns.put(Test.TEST_B4, "HI");

        scoreColumns.put(Test.TEST_A1, "FT");
        scoreColumns.put(Test.TEST_A2, "GD");
        scoreColumns.put(Test.TEST_A3, "FT");
        scoreColumns.put(Test.TEST_A4, "GD");
        scoreColumns.put(Test.TEST_B1, "FT");
        scoreColumns.put(Test.TEST_B2, "GD");
        scoreColumns.put(Test.TEST_B3, "FT");
        scoreColumns.put(Test.TEST_B4, "GD");

        reactionTimeColumns.put(Test.TEST_A1, "GA");
        reactionTimeColumns.put(Test.TEST_A2, "GK");
        reactionTimeColumns.put(Test.TEST_A3, "GA");
        reactionTimeColumns.put(Test.TEST_A4, "GK");
        reactionTimeColumns.put(Test.TEST_B1, "GA");
        reactionTimeColumns.put(Test.TEST_B2, "GK");
        reactionTimeColumns.put(Test.TEST_B3, "GA");
        reactionTimeColumns.put(Test.TEST_B4, "GK");

        wordColumns.put(Test.TEST_A1, "FH");
        wordColumns.put(Test.TEST_A2, "FJ");
        wordColumns.put(Test.TEST_A3, "FA");
        wordColumns.put(Test.TEST_A4, "FJ");
        wordColumns.put(Test.TEST_B1, "FH");
        wordColumns.put(Test.TEST_B2, "FJ");
        wordColumns.put(Test.TEST_B3, "FH");
        wordColumns.put(Test.TEST_B4, "FJ");

        descrXRuleColumns.put(1, "DT");
        descrXRuleColumns.put(2, "EC");
        descrXRuleBasisColumns.put(1, "AB");
        descrXRuleBasisColumns.put(2, "AK");
        descrXRuleDutchColumns.put(1, "DA");
        descrXRuleDutchColumns.put(2, "DJ");
        descrIRuleColumns.put(1, "EC");
        descrIRuleColumns.put(2, "DT");
        descrIRuleBasisColumns.put(1, "AK");
        descrIRuleBasisColumns.put(2, "AB");
        descrIRuleDutchColumns.put(1, "DJ");
        descrIRuleDutchColumns.put(2, "DA");
    }

    String orderCol(Test test) {
        return orderColumns.get(test);
    }

    String scoreCol(Test test) {
        return scoreColumns.get(test);
    }

    String reactionTimeCol(Test test) {
        return reactionTimeColumns.get(test);
    }

    String wordCol(Test test) {
        return wordColumns.get(test);
    }

    String descrXRuleCol(int simplifiedCounterBalance) {
        return descrXRuleColumns.get(simplifiedCounterBalance);
    }

    String descrXRuleBasisCol(int simplifiedCounterBalance) {
        return descrXRuleBasisColumns.get(simplifiedCounterBalance);
    }

    String descrXRuleDutchCol(int simplifiedCounterBalance) {
        return descrXRuleDutchColumns.get(simplifiedCounterBalance);
    }

    String descrIRuleCol(int simplifiedCounterBalance) {
        return descrIRuleColumns.get(simplifiedCounterBalance);
    }

    String descrIRuleBasisCol(int simplifiedCounterBalance) {
        return descrIRuleBasisColumns.get(simplifiedCounterBalance);
    }

    String descrIRuleDutchCol(int simplifiedCounterBalance) {
        return descrIRuleDutchColumns.get(simplifiedCounterBalance);
    }


}
