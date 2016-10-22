package nl.patrickkik.lysanne.leap3;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

        // ============= Parsing

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
                subject.putWord(test, order, word);
                subject.setDescrXRule(row.get(configuration.descrXRuleCol(simplifiedCounterBalance)));
                subject.setDescrXRuleBasis(row.get(configuration.descrXRuleBasisCol(simplifiedCounterBalance)));
                subject.setDescrXRuleDutch(row.get(configuration.descrXRuleDutchCol(simplifiedCounterBalance)));
                subject.setDescrIRule(row.get(configuration.descrIRuleCol(simplifiedCounterBalance)));
                subject.setDescrIRuleBasis(row.get(configuration.descrIRuleBasisCol(simplifiedCounterBalance)));
                subject.setDescrIRuleDutch(row.get(configuration.descrIRuleDutchCol(simplifiedCounterBalance)));

                subjects.put(subjectId, subject);
            }
        }


        // ============= Global data

        Writer exportTestsWriter = new FileWriter(new File(exportLocation, "export_tests_v1.csv"));
        Map<Rule, Double> standardDeviations = new EnumMap<>(Rule.class);
        Map<Rule, Double> averages = new EnumMap<>(Rule.class);
        exportTestsWriter.append("Test,Average,Standard Deviation,Total times,Invalid times,Invalid time percentage\n");
        for (Rule rule : Rule.values()) {
            StandardDeviation standardDeviation = new StandardDeviation();
            subjects.values().stream()
                    .flatMap(subject -> subject.getTimes(rule).stream())
                    .forEach(standardDeviation::increment);
            double avg = subjects.values().stream()
                    .flatMapToInt(subject -> subject.getTimes(rule).stream().mapToInt(Integer::intValue))
                    .average()
                    .orElseThrow(() -> new IllegalStateException("Kan niet"));
            averages.put(rule, avg);
            standardDeviations.put(rule, standardDeviation.getResult());
            int invalidTimes = subjects.values().stream()
                    .mapToInt(subject -> subject.invalidTimes(rule, avg, standardDeviation.getResult()))
                    .sum();
            int totalTimes = 16 * subjects.size();
            exportTestsWriter.append(rule.name() + ","
                    + new BigDecimal(avg).setScale(2, RoundingMode.HALF_EVEN) + ","
                    + new BigDecimal(standardDeviation.getResult()).setScale(2, RoundingMode.HALF_EVEN) + ","
                    + totalTimes + ","
                    + invalidTimes + ","
                    + new BigDecimal(100 * invalidTimes).divide(new BigDecimal(totalTimes), 2, RoundingMode.HALF_EVEN) + "\n");
        }


        // ============= Exporting test data

//        for (Rule rule : Rule.values()) {
//            File export = new File(exportLocation, "export_" + rule.name().toLowerCase() + "_v5.csv");
//            Writer writer = new FileWriter(export);
//
//            List<List<String>> headerRows = header(subjects.get(1).words(rule));
//            for (List<String> headerRow : headerRows) {
//                writer.append(headerRow.get(0));
//                for (int i = 1; i < headerRow.size(); i++) {
//                    writer.append(",");
//                    writer.append(headerRow.get(i));
//                }
//                writer.append("\n");
//            }
//
//            subjects.forEach((subjectId, subject) -> {
//                try {
//                    writer.append(subject.toCSV(rule, averages.get(rule), standardDeviations.get(rule))).append("\n");
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            });
//
//            writer.flush();
//            writer.close();
//        }

        // ============= Exporting subject data

//        Writer subjectsWriter = new FileWriter(new File(exportLocation, "export_subjects_v1.csv"));
//        subjectsWriter.append("Subject,Counter balanace (original),Counter balance (simplified),");
//        subjectsWriter.append("Beschrijving van de X-regel,Basis van beschrijving van de X-regel,Nederlandse regel herkenning X-regel,");
//        subjectsWriter.append("Beschrijving van de I-regel,Basis van beschrijving van de I-regel,Nederlandse regel herkenning I-regel\n");
//        subjects.forEach((subjectId, subject) -> {
//            try {
//                subjectsWriter.append(Integer.toString(subjectId)).append(",");
//                subjectsWriter.append(Integer.toString(subject.getOriginalCounterBalance())).append(",");
//                subjectsWriter.append(Integer.toString(subject.getSimplifiedCounterBalance())).append(",");
//                subjectsWriter.append("\"").append(subject.getDescrXRule()).append("\",");
//                subjectsWriter.append("\"").append(subject.getDescrXRuleBasis()).append("\",");
//                subjectsWriter.append("\"").append(subject.getDescrXRuleDutch()).append("\",");
//                subjectsWriter.append("\"").append(subject.getDescrIRule()).append("\",");
//                subjectsWriter.append("\"").append(subject.getDescrIRuleBasis()).append("\",");
//                subjectsWriter.append("\"").append(subject.getDescrIRuleDutch()).append("\"\n");
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        subjectsWriter.flush();
//        subjectsWriter.close();
    }

    private List<List<String>> header(List<String> words) {
        List<List<String>> headers = new ArrayList<>();

        final List<String> header1 = new ArrayList<>();
        header1.add("Subject");
        header1.add("Counter balance (original)");
        header1.add("Counter balance (simplified)");
        header1.add("RecSteps (Correct answers on steps of transformation)");
        IntStream.rangeClosed(1, 17).forEach(i -> header1.add(""));
        header1.add("All RTs RecSteps (in ms)");
        IntStream.rangeClosed(1, 15).forEach(i -> header1.add(""));
        headers.add(header1);

        final List<String> header2 = new ArrayList<>();
        header2.add("");
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
        header2.add("Average time");
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
