package top.kwseeker.jwt.nimbus;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import junit.framework.TestCase;

import java.net.URI;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

public class SignedJWTTest extends TestCase {

    private static final String ALG_RSA = "RSA";
    private static final int RSA_KEY_SIZE = 2048;   //这个并不是生成的公私钥的字符串长度，估计是里面的极大整数的长度

    public void testGenerateRSAKeys() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALG_RSA);
        //kpg.initialize(RSA_KEY_SIZE);
        kpg.initialize(512);

        KeyPair keyPair = kpg.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        String publicKeyStr = Base64.encode(publicKey.getEncoded());
        String privateKeyStr = Base64.encode(privateKey.getEncoded());
        System.out.println("------- public key -------");
        System.out.println(publicKeyStr);
        System.out.println("------- private key -------");
        System.out.println(privateKeyStr);
    }

    public void testSignAndVerify() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALG_RSA);
        kpg.initialize(RSA_KEY_SIZE);

        KeyPair keyPair = kpg.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject("alice")
                .issueTime(new Date(123000L))
                .issuer("https://c2id.com")
                .claim("scope", "openid")
                .build();

        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.RS256)
                .keyID("1")
                .jwkURL(new URI("https://c2id.com/jwks.json"))
                .build();
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        assertEquals(JWSObject.State.UNSIGNED, signedJWT.getState());

        System.out.println("------- signing input -------");
        System.out.println("default charset: " + Charset.defaultCharset().name());
        //System.out.println("raw_sigInput: " + java.util.Base64.getEncoder().encodeToString(signedJWT.getSigningInput()));
        System.out.println("raw_sigInput: " + new String(signedJWT.getSigningInput()));
        Base64URL sigInput = Base64URL.encode(signedJWT.getSigningInput());     //TODO Base64URL做了啥
        System.out.println("encoded_sigInput: " + sigInput);

        //服务端使用私钥生成签名
        JWSSigner signer = new RSASSASigner(privateKey);
        signedJWT.sign(signer);
        assertEquals(JWSObject.State.SIGNED, signedJWT.getState());
        System.out.println("------- sign -------");
        System.out.println("raw signature: " + signedJWT.getSignature().toString());
        String serializedJWT = signedJWT.serialize();       //将 sigInput 和 signature 以“.”拼接起来
        System.out.println("final signature: " + serializedJWT);

        SignedJWT parsedSingedJWT = SignedJWT.parse(serializedJWT);
        assertEquals(sigInput, Base64URL.encode(parsedSingedJWT.getSigningInput()));
        //客户端校验
        JWSVerifier verifier = new RSASSAVerifier(publicKey);
        assertTrue(parsedSingedJWT.verify(verifier));
        assertEquals(JWSObject.State.VERIFIED, signedJWT.getState());
    }
}
