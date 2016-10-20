package nl.patrickkik.lysanne.leap3;

import org.apache.commons.csv.CSVFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

@Component
class AllRawData {

    private String location;

    private List<Map<String, String>> data;

    List<Map<String, String>> get() {
        if (Objects.isNull(data)) {
            data = read();
        }
        return data;
    }

    private List<Map<String, String>> read() {
        List<Map<String, String>> rows = new ArrayList<>();
        try {
            Reader reader = new FileReader(new File(location));
            CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(reader)
                    .forEach(record -> {
                        Map<String, String> row = new HashMap<>();
                        for (int colIndex = 0; colIndex < record.size(); colIndex++) {
                            String value = record.get(colIndex);
                            row.put(col(colIndex), value);
                        }
                        rows.add(row);
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rows;
    }

    String col(int index) {
        if (index < 26) {
            return String.valueOf((char) (index + 65));
        }
        return col(index / 26 - 1) + col(index % 26);
    }

    @Value("${allrawdata.location}")
    void setLocation(String location) {
        this.location = location;
    }
}
