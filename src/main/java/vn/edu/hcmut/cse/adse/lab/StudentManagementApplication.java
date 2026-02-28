package vn.edu.hcmut.cse.adse.lab;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StudentManagementApplication {

	public static void main(String[] args) {
				Dotenv.load();
        SpringApplication.run(StudentManagementApplication.class, args);
	}

}
