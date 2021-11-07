package top.kwseeker.sso.authorizer.domain.jwt;

import java.util.Map;

public interface Authentication {

    String generateJWT(Map<String, Object> payload);

    boolean verifyJWT(String signature);
}
