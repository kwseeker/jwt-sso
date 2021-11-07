package top.kwseeker.sso.authorizer.domain.jwt.auth0;

import top.kwseeker.sso.authorizer.domain.jwt.Authentication;

import java.util.Map;

public class Auth0JWTAuthAdapter extends Auth0JWTAuth implements Authentication {

    @Override
    public String generateJWT(Map<String, Object> payload) {
        return null;
    }

    @Override
    public boolean verifyJWT(String signature) {
        return false;
    }
}
