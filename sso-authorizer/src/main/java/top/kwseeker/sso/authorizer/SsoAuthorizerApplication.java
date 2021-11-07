package top.kwseeker.sso.authorizer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("top.kwseeker.sso.authorizer.dao")
@ServletComponentScan
public class SsoAuthorizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoAuthorizerApplication.class, args);
    }
}
