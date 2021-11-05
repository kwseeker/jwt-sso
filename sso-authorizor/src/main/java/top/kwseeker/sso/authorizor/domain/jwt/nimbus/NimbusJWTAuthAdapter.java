package top.kwseeker.sso.authorizor.domain.jwt.nimbus;

import top.kwseeker.sso.authorizor.domain.jwt.Authentication;

public class NimbusJWTAuthAdapter implements Authentication {



    @Override
    public String generateJWT() {
        return null;
    }

    @Override
    public boolean verifyJWT() {
        return false;
    }
}
