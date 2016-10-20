package nl.patrickkik.lysanne.leap3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;
import java.util.Objects;

@Component
public class AllRawData {

    @Value("${allrawdata.location}")
    private String location;

    private Map<String, Object> data;

    public String get() {
        if (Objects.isNull(data)) {
            data = read();
        }
        return data;
    }

    private Map<String, Object> read() {


        StringBuilder builder = new StringBuilder();
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(location));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return builder.toString();
    }

}
