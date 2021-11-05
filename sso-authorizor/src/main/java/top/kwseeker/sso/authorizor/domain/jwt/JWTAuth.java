package top.kwseeker.sso.authorizor.domain.jwt;

import org.springframework.stereotype.Component;

/**
 * token生成，token校验
 */
@Component
public class JWTAuth implements Authentication {

    public JWTAuth() {

    }

    @Override
    public String generateJWT() {
        return null;
    }

    @Override
    public boolean verifyJWT() {
        return false;
    }
}
