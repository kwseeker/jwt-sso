package top.kwseeker.sso.authorizer.domain.jwt.nimbus;

import org.springframework.stereotype.Component;
import top.kwseeker.sso.authorizer.domain.jwt.Authentication;

import java.util.Map;

@Component
public class NimbusJWTAuthAdapter extends NimbusJWTAuth implements Authentication {

    @Override
    public String generateJWT(Map<String, Object> payload) {
        //return super.generateSignature();
        return "";
    }

    @Override
    public boolean verifyJWT(String signature) {
        return false;
    }
}
