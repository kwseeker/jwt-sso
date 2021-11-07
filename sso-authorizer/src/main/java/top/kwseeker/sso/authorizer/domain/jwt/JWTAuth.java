package top.kwseeker.sso.authorizer.domain.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.kwseeker.sso.authorizer.domain.jwt.nimbus.NimbusJWTAuthAdapter;

import javax.annotation.Resource;
import java.util.Map;

/**
 * token生成，token校验
 */
@Component
public class JWTAuth implements Authentication {

    @Resource
    private JWTConfig jwtConfig;
    @Resource
    private NimbusJWTAuthAdapter authentication;

    @Override
    public String generateJWT(Map<String, Object> payload) {
        return authentication.generateJWT(payload);
    }

    @Override
    public boolean verifyJWT(String signature) {
        return authentication.verifyJWT(signature);
    }
}
