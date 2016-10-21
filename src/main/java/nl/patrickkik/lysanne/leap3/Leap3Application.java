package nl.patrickkik.lysanne.leap3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;

@SpringBootApplication
public class Leap3Application implements CommandLineRunner {

    private AllRawData allRawData;
    private CounterBalance counterBalance;
    private File exportLocation;
    private Configuration configuration;

    public static void main(String[] args) {
        SpringApplication.run(Leap3Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        final Map<Integer, Subject> subjects = new TreeMap<>();
        final List<Map<String, String>> rows = allRawData.get();

        for (final Map<String, String> row : rows) {
            final String counterBalancingRaw = row.get("EO");
            if (!counterBalancingRaw.isEmpty()) {
                int subjectId = Integer.parseInt(row.get("B"));
                int simplifiedCounterBalance = Integer.parseInt(counterBalancingRaw);
                int originalCounterBalance = counterBalance.get(subjectId);
                Test test = Test.of(row.get("GC"));
                int order = Integer.parseInt(row.get(configuration.orderCol(test)));
                int score = Integer.parseInt(row.get(configuration.scoreCol(test)));
                int reactionTime = Integer.parseInt(row.get(configuration.reactionTimeCol(test)));
                String word = row.get(configuration.wordCol(test));

                Subject subject = subjects.getOrDefault(subjectId, new Subject(subjectId, originalCounterBalance, simplifiedCounterBalance));
                subject.putScore(test, order, score);
                subject.putTime(test, order, reactionTime);
                subjects.put(subjectId, subject);
            }
        }

        for (Rule rule : Rule.values()) {
            System.out.println(rule);
            File export = new File(exportLocation, "export_" + rule.name().toLowerCase() + "_v3.csv");
            FileWriter writer = new FileWriter(export);

            subjects.forEach((subjectId, subject) -> {
                try {
                    writer.append(subject.toCSV(rule) + "\n");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            writer.flush();
            writer.close();
        }


        Rule rule = Rule.STEPS_X;

        System.out.println(subjects.get(1).toCSV(rule));
        System.out.println(subjects.get(2).toCSV(rule));
        System.out.println(subjects.get(3).toCSV(rule));
        System.out.println(subjects.get(4).toCSV(rule));


//        List<String> words = new ArrayList<>(16);
//        for (int i = 0; i < 16; i++) {
//            words.add("Score " + (i + 1));
//        }
//        List<Subject> subjects = new ArrayList<>();
//        Subject subject = new Subject(0, 0);
//
//        List<Map<String, String>> rows = allRawData.get();
//
//        for (Map<String, String> row : rows) {
//
//            String counterBalancingRaw = row.get("EO");
//
//            if (!counterBalancingRaw.isEmpty()) {
//                int counterBalancing = Integer.parseInt(counterBalancingRaw);
//
//                if (counterBalancing == 1) {
//
//                    int subjectId = Integer.parseInt(row.get("B"));
//                    int trial = Integer.parseInt(row.get("FG"));
//
//                    if (trial == 1) {
//                        subject = new Subject(subjectId, counterBalance.get(subjectId));
//                    }
//
//                    if (trial >= 1 && trial <= 16) {
//
//                        int order = Integer.parseInt(row.get("GN"));
//                        int score = Integer.parseInt(row.get("FT"));
//                        int reactionTime = Integer.parseInt(row.get("GA"));
//
//                        subject.putScore(order, score);
//                        subject.putTime(order, reactionTime);
//
//                        String word = row.get("FH");
//                        words.set(order - 1, word);
//                    }
//
//                    if (trial == 16) {
//                        subjects.add(subject);
//                    }
//                }
//
//                if (counterBalancing == 2) {
//
//                    int subjectId = Integer.parseInt(row.get("B"));
//                    int trial = Integer.parseInt(row.get("FG"));
//
//                    if (trial == 33) {
//                        subject = new Subject(subjectId, counterBalance.get(subjectId));
//                    }
//
//                    if (trial >= 33 && trial <= 48) {
//
//                        int order = Integer.parseInt(row.get("GT"));
//                        int score = Integer.parseInt(row.get("FT"));
//                        int reactionTime = Integer.parseInt(row.get("GA"));
//
//                        subject.putScore(order, score);
//                        subject.putTime(order, reactionTime);
//                    }
//
//                    if (trial == 48) {
//                        subjects.add(subject);
//                    }
//
//                }
//            }
//        }
//
//        subjects.sort(Subject::compareTo);
//
//        List<List<String>> headerRows = header(words);
//
//
//        File export = new File(exportLocation, "Data_Step_X-Rule_v2.csv");
//        FileWriter writer = new FileWriter(export);
//
//        for (List<String> headerRow : headerRows) {
//            System.out.print(headerRow.get(0));
//            writer.append(headerRow.get(0));
//            for (int i = 1; i < headerRow.size(); i++) {
//                System.out.print(",");
//                writer.append(",");
//                System.out.print(headerRow.get(i));
//                writer.append(headerRow.get(i));
//            }
//            System.out.println();
//            writer.append("\n");
//        }
//        subjects.forEach(s -> {
//            System.out.println(s.toCSV());
//            try {
//                writer.append(s.toCSV() + "\n");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//
//        writer.flush();
//        writer.close();

    }

    private List<List<String>> header(List<String> words) {
        List<List<String>> headers = new ArrayList<>();

        final List<String> header1 = new ArrayList<>();
        header1.add("Subject");
        header1.add("Counter balance");
        header1.add("RecSteps (Correct answers on steps of transformation)");
        IntStream.rangeClosed(1, 17).forEach(i -> header1.add(""));
        header1.add("All RTs RecSteps (in ms)");
        IntStream.rangeClosed(1, 15).forEach(i -> header1.add(""));
        headers.add(header1);

        final List<String> header2 = new ArrayList<>();
        header2.add("");
        header2.add("");
        for (int i = 0; i < words.size(); i++) {
            header2.add((i + 1) + "=" + words.get(i));
        }
        header2.add("Score (som/16)");
        header2.add("");
        for (int i = 0; i < words.size(); i++) {
            header2.add((i + 1) + "=" + words.get(i));
        }
        headers.add(header2);

        return headers;
    }

    @Autowired
    public void setAllRawData(AllRawData allRawData) {
        this.allRawData = allRawData;
    }

    @Autowired
    public void setCounterBalance(CounterBalance counterBalance) {
        this.counterBalance = counterBalance;
    }

    @Value("${export.location}")
    public void setExportLocation(String exportLocation) {
        this.exportLocation = new File(exportLocation);
    }

    @Autowired
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
