package nl.patrickkik.lysanne.leap3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Leap3Application implements CommandLineRunner {

    @Autowired
    private AllRawData allRawData;

	public static void main(String[] args) {
		SpringApplication.run(Leap3Application.class, args);
	}


    @Override
    public void run(String... args) throws Exception {

        System.out.println(allRawData.get().substring(10000, 15000));


    }
}
