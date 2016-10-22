package nl.patrickkik.lysanne.leap3;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.IntStream;

class Subject implements Comparable<Subject> {

    private final int id;
    private final int originalCounterBalance;
    private final int simplifiedCounterBalance;
    private final Map<Test, List<Integer>> scores = new EnumMap<>(Test.class);
    private final Map<Test, List<Integer>> times = new EnumMap<>(Test.class);
    private final Map<Test, List<String>> words = new EnumMap<>(Test.class);
    private String descrXRule;
    private String descrXRuleBasis;
    private String descrXRuleDutch;
    private String descrIRule;
    private String descrIRuleBasis;
    private String descrIRuleDutch;

    Subject(int id, int originalCounterBalance, int simplifiedCounterBalance) {
        this.id = id;
        this.originalCounterBalance = originalCounterBalance;
        this.simplifiedCounterBalance = simplifiedCounterBalance;
    }

    void putScore(Test test, int order, int score) {
        List<Integer> testScores = scores.getOrDefault(test, cleanIntegerList());
        testScores.set(order - 1, score);
        scores.put(test, testScores);
    }

    void putTime(Test test, int order, int time) {
        List<Integer> testTimes = times.getOrDefault(test, cleanIntegerList());
        testTimes.set(order - 1, time);
        times.put(test, testTimes);
    }

    void putWord(Test test, int order, String word) {
        List<String> testWords = words.getOrDefault(test, cleanStringList());
        testWords.set(order - 1, word);
        words.put(test, testWords);
    }

    private ArrayList<Integer> cleanIntegerList() {
        ArrayList<Integer> list = new ArrayList<>(16);
        IntStream.range(0, 16)
                .forEach(i -> list.add(0));
        return list;
    }

    private ArrayList<String> cleanStringList() {
        ArrayList<String> list = new ArrayList<>(16);
        IntStream.range(0, 16)
                .forEach(i -> list.add(""));
        return list;
    }

    private Test toTest(Rule rule) {
        return Arrays.stream(rule.tests())
                .filter(test -> scores.keySet().contains(test))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(rule.name()));
    }

    int getOriginalCounterBalance() {
        return originalCounterBalance;
    }

    int getSimplifiedCounterBalance() {
        return simplifiedCounterBalance;
    }

    String getDescrXRule() {
        return descrXRule;
    }

    void setDescrXRule(String descrXRule) {
        this.descrXRule = clean(descrXRule);
    }

    String getDescrXRuleBasis() {
        return descrXRuleBasis;
    }

    void setDescrXRuleBasis(String descrXRuleBasis) {
        this.descrXRuleBasis = clean(descrXRuleBasis);
    }

    String getDescrXRuleDutch() {
        return descrXRuleDutch;
    }

    void setDescrXRuleDutch(String descrXRuleDutch) {
        this.descrXRuleDutch = clean(descrXRuleDutch);
    }

    String getDescrIRule() {
        return descrIRule;
    }

    void setDescrIRule(String descrIRule) {
        this.descrIRule = clean(descrIRule);
    }

    String getDescrIRuleBasis() {
        return descrIRuleBasis;
    }

    void setDescrIRuleBasis(String descrIRuleBasis) {
        this.descrIRuleBasis = clean(descrIRuleBasis);
    }

    String getDescrIRuleDutch() {
        return descrIRuleDutch;
    }

    void setDescrIRuleDutch(String descrIRuleDutch) {
        this.descrIRuleDutch = clean(descrIRuleDutch);
    }

    List<Integer> getTimes(Rule rule) {
        return times.get(toTest(rule));
    }

    private String clean(String raw) {
        return raw.replaceAll("\\{SPACE\\}", " ")
                .replaceAll("\\{ENTER\\}", "")
                .replaceAll("\\{SHIFT\\}", "")
                .replaceAll("\\{CAPSLOCK\\}", "")
                .replaceAll("\\{UPARROW\\}", "")
                .replaceAll("\\{DOWNARROW\\}", "")
                .replaceAll("\\{LEFTARROW\\}", "")
                .replaceAll("\\{RIGHTARROW\\}", "")
                .replaceAll("\\{TAB\\}", "")
                .replaceAll("\\{CONTROL\\}", "")
                .replaceAll("\\{/\\}", "/")
                .replaceAll("\\{-\\}", "-")
                .replaceAll("\\{,\\}", ",")
                .replaceAll("\\{\\.\\}", ".")
                .replaceAll("\\{'\\}", "'")
                .replaceAll("\\{:\\}", ":")
                .replaceAll("\\{\\+\\}", "+")
                .replaceAll("\\{\\(\\}", "(")
                .replaceAll("\\{\\)\\}", ")")
                ;
    }

    String toCSV(Rule rule, double average, double standardDeviation) {
        Test test = toTest(rule);
        List<Integer> testScores = scores.get(test);
        List<Integer> testTimes = times.get(test);

        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(",");
        sb.append(originalCounterBalance);
        sb.append(",");
        sb.append(simplifiedCounterBalance);
        sb.append(",");
        sb.append(testScores.get(0));
        int totalScore = testScores.get(0);
        for (int i = 1; i < 16; i++) {
            sb.append(",");
            sb.append(testScores.get(i));
            totalScore += testScores.get(i);
        }
        sb.append(",");
        sb.append(new BigDecimal(totalScore).divide(new BigDecimal(16), 4, BigDecimal.ROUND_HALF_EVEN));
        sb.append(",");
        sb.append("");
        sb.append(",");
        sb.append(timeIfCorrectScore(testTimes, testScores, 0, average, standardDeviation));
        for (int i = 1; i < 16; i++) {
            sb.append(",");
            sb.append(timeIfCorrectScore(testTimes, testScores, i, average, standardDeviation));
        }
        sb.append(",");
        sb.append(averageTimeIfEnoughValues(testTimes, testScores, average, standardDeviation));
        return sb.toString();
    }

    int invalidTimes(Rule rule, double average, double standardDeviation) {
        List<Integer> testTimes = times.get(toTest(rule));
        List<Integer> testScores = scores.get(toTest(rule));
        return (int) IntStream.range(0, 16)
                .mapToObj(i -> timeIfCorrectScore(testTimes, testScores, i, average, standardDeviation))
                .filter(String::isEmpty)
                .count();
    }

    private String averageTimeIfEnoughValues(List<Integer> testTimes, List<Integer> testScores, double average, double standardDeviation) {
        double averageTime = testTimes.stream().mapToInt(Integer::intValue).average().orElseThrow(IllegalStateException::new);
        int validTimesCount = (int) IntStream.range(0, 16)
                .mapToObj(i -> timeIfCorrectScore(testTimes, testScores, i, average, standardDeviation))
                .filter(s -> !s.isEmpty())
                .count();
        return validTimesCount >= 12
                ? new BigDecimal(averageTime).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString()
                : "";
    }

    private String timeIfCorrectScore(List<Integer> testTimes, List<Integer> testScores, int i, double average, double standardDeviation) {
        int time = testTimes.get(i);
        return testScores.get(i) == 1
                ? time >= average - 2 * standardDeviation && time <= average + 2 * standardDeviation
                ? Integer.toString(time)
                : ""
                : "";
    }

    List<String> words(Rule rule) {
        Test test = toTest(rule);
        return words.get(test);
    }

    @Override
    public int compareTo(Subject subject) {
        return Integer.compare(id, subject.id);
    }
}
