package nl.patrickkik.lysanne.leap3;

import java.math.BigDecimal;
import java.util.Arrays;

class Subject implements Comparable<Subject> {

    private int id;
    private int counterBalance;
    private int[] scores = new int[16];
    private int[] times = new int[16];

    Subject(int id, int counterBalance) {
        this.id = id;
        this.counterBalance = counterBalance;
        Arrays.fill(scores, 0);
        Arrays.fill(times, 0);
    }

    void putScore(int trial, int score) {
        scores[trial - 1] = score;
    }

    void putTime(int trial, int time) {
        times[trial - 1] = time;
    }

    public String toCSV() {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(",");
        sb.append(counterBalance);
        sb.append(",");
        sb.append(scores[0]);
        int totalScore = scores[0];
        for (int i = 1; i < 16; i++) {
            sb.append(",");
            sb.append(scores[i]);
            totalScore += scores[i];
        }
        sb.append(",");
        sb.append(new BigDecimal(totalScore).divide(new BigDecimal(16), 4, BigDecimal.ROUND_HALF_EVEN));
        sb.append(",");
        sb.append("");
        sb.append(",");
        sb.append(times[0]);
        for (int i = 1; i < 16; i++) {
            sb.append(",");
            sb.append(times[i]);
        }
        return sb.toString();
    }

    @Override
    public int compareTo(Subject subject) {
        return Integer.compare(id, subject.id);
    }
}
