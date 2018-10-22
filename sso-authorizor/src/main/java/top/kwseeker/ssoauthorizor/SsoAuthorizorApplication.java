package top.kwseeker.ssoauthorizor;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("top.kwseeker.ssoauthorizor.dao")
@ServletComponentScan
public class SsoAuthorizorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoAuthorizorApplication.class, args);
    }
}
