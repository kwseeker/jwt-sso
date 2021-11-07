package top.kwseeker.sso.authorizer.util;

import com.nimbusds.jose.util.StandardCharset;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public class RS256KeyUtil {

    private static final String ALG_RSA = "RSA";
    private static final int RSA_KEY_SIZE = 2048;   //推荐的最小程度

    public static void generatePairKey2File() throws Exception {
        generatePairKey2File(ALG_RSA, "");
    }

    public static void generatePairKey2File(String algorithm, String dir) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm);
        kpg.initialize(RSA_KEY_SIZE);

        KeyPair keyPair = kpg.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        String publicKeyStr = new String(publicKey.getEncoded());
        String privateKeyStr = new String(privateKey.getEncoded());

        FileUtils.writeStringToFile(new File(dir + "authorizer.pub"), publicKeyStr, StandardCharset.UTF_8);
        FileUtils.writeStringToFile(new File(dir + "authorizer.pri"), privateKeyStr, StandardCharset.UTF_8);
    }


}
