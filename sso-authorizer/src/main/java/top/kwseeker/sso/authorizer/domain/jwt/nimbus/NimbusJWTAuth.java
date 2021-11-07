package top.kwseeker.sso.authorizer.domain.jwt.nimbus;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.kwseeker.sso.authorizer.basic.exception.ServiceException;

import java.text.ParseException;

@Slf4j
@Component
public class NimbusJWTAuth {



    public String generateSignature(JWSHeader header, JWTClaimsSet claimsSet, JWSSigner signer) {
        try {
            SignedJWT signedJWT = new SignedJWT(header, claimsSet);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("签名生成失败，e=" + e.getMessage());
            throw new ServiceException("签名生成失败", e.getCause());
        }
    }

    public boolean verifyToken(String jwt, JWSVerifier verifier) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(jwt);
            return signedJWT.verify(verifier);
        } catch (ParseException | JOSEException e) {
            log.error("签名校验失败，e=" + e.getMessage());
            throw new ServiceException("签名校验失败", e.getCause());
        }
    }
}
