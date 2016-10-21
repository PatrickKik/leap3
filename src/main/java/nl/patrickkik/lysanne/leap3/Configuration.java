package nl.patrickkik.lysanne.leap3;

import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
class Configuration {

    private final Map<Test, String> orderColumns = new EnumMap<>(Test.class);
    private final Map<Test, String> scoreColumns = new EnumMap<>(Test.class);
    private final Map<Test, String> reactionTimeColumns = new EnumMap<>(Test.class);
    private final Map<Test, String> wordColumns = new EnumMap<>(Test.class);

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
}
