package top.kwseeker.ssoappone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class SsoApponeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoApponeApplication.class, args);
    }
}
