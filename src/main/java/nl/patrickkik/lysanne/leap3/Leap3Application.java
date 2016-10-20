package nl.patrickkik.lysanne.leap3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Leap3Application implements CommandLineRunner {

    private AllRawData allRawData;
    private CounterBalance counterBalance;

    public static void main(String[] args) {
        SpringApplication.run(Leap3Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        List<Subject> subjects = new ArrayList<>();
        Subject subject = new Subject(0, 0);

        List<Map<String, String>> rows = allRawData.get();

        for (Map<String, String> row : rows) {

            String counterBalancingRaw = row.get("EO");

            if (!counterBalancingRaw.isEmpty()) {
                int counterBalancing = Integer.parseInt(counterBalancingRaw);

                if (counterBalancing == 1) {

                    int subjectId = Integer.parseInt(row.get("B"));
                    int trial = Integer.parseInt(row.get("FG"));

                    if (trial == 1) {
                        subject = new Subject(subjectId, counterBalance.get(subjectId));
                    }

                    if (trial >= 1 && trial <= 16) {

                        int order = Integer.parseInt(row.get("GN"));
                        int score = Integer.parseInt(row.get("FT"));
                        int reactionTime = Integer.parseInt(row.get("GA"));

                        subject.putScore(order, score);
                        subject.putTime(order, reactionTime);
                    }

                    if (trial == 16) {
                        subjects.add(subject);
                    }
                }

                if (counterBalancing == 2) {

                    int subjectId = Integer.parseInt(row.get("B"));
                    int trial = Integer.parseInt(row.get("FG"));

                    if (trial == 33) {
                        subject = new Subject(subjectId, counterBalance.get(subjectId));
                    }

                    if (trial >= 33 && trial <= 48) {

                        int order = Integer.parseInt(row.get("GT"));
                        int score = Integer.parseInt(row.get("FT"));
                        int reactionTime = Integer.parseInt(row.get("GA"));

                        subject.putScore(order, score);
                        subject.putTime(order, reactionTime);
                    }

                    if (trial == 48) {
                        subjects.add(subject);
                    }

                }
            }
        }

        subjects.sort(Subject::compareTo);

        subjects.forEach(s -> System.out.println(s.toCSV()));

    }

    @Autowired
    public void setAllRawData(AllRawData allRawData) {
        this.allRawData = allRawData;
    }

    @Autowired
    public void setCounterBalance(CounterBalance counterBalance) {
        this.counterBalance = counterBalance;
    }
}
