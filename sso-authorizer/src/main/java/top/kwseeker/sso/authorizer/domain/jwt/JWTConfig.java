package top.kwseeker.sso.authorizer.domain.jwt;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

@Data
@Component
//@PropertySource("classpath:jwt-config.properties")
//@ConfigurationProperties(prefix = "jwt")
public class JWTConfig {

    private static final String ALGO_TYPE = "algo.type";
    private static final String RS256_PRIVATE_KEY_PATH = "algo.rs256.privateKeyPath";
    private static final String RS256_PUBLIC_KEY_PATH = "algo.rs256.publicKeyPath";
    private static final String HMAC256_KEY = "algo.hmac256.key";
    private static final String HS256_KEY = "algo.hs256.key";

    private Properties properties;

    public JWTConfig() {
        this.properties = new Properties();
        String configPath = Objects.requireNonNull(getClass().getClassLoader().getResource("jwt-config.properties")).getPath();
        try (FileInputStream fip = new FileInputStream(configPath)) {
            properties.load(fip);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAlgoType() {
        return (String) properties.get(ALGO_TYPE);
    }
}
