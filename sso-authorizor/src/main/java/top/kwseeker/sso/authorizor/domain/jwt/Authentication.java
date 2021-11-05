package top.kwseeker.sso.authorizor.domain.jwt;

public interface Authentication {

    String generateJWT();

    boolean verifyJWT();
}
