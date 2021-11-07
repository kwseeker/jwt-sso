package top.kwseeker.sso.authorizer.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.shaded.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {

    private static final byte[] SECRET = "3d990d2276917dfac04467df11fff26d".getBytes();

    private static final JWSHeader header = new JWSHeader(JWSAlgorithm.HS256, JOSEObjectType.JWT,
            null, null, null, null, null, null, null, null, null, null, null);

    public static String createToken(Map<String, Object> payload) {
        String tokenString = null;
        JWSObject jwsObject = new JWSObject(header, new Payload(new JSONObject(payload)));
        try {
            jwsObject.sign(new MACSigner(SECRET));
            tokenString = jwsObject.serialize();
        } catch (JOSEException e) {
            System.err.println("签名失败：" + e.getMessage());
            e.printStackTrace();
        }
        return tokenString;
    }

    //校验通过或未过期，则返回负载的内容对象，否则返回状态码字符串
    public static Map<String, Object> validToken(String token) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
            JWSObject jwsObject = JWSObject.parse(token);   //签名
            Payload payload = jwsObject.getPayload();       //负载
            JWSVerifier verifier = new MACVerifier(SECRET); //签名校对

            if (jwsObject.verify(verifier)) {
                Map<String, Object> jsonOBj = payload.toJSONObject();    //负载内容
                // token校验成功（此时没有校验是否过期）
                resultMap.put("state", TokenState.VALID.toString());
                // 若payload包含ext字段，则校验是否过期
                if (jsonOBj.containsKey("ext")) {
                    long extTime = Long.parseLong(jsonOBj.get("ext").toString());
                    long curTime = new Date().getTime();
                    // 过期了
                    if (curTime > extTime) {
                        resultMap.clear();
                        resultMap.put("state", TokenState.EXPIRED.toString());
                    }
                }
                resultMap.put("data", jsonOBj);

            } else {
                // 校验失败
                resultMap.put("state", TokenState.INVALID.toString());
            }

        } catch (Exception e) {
            //e.printStackTrace();
            // token格式不合法导致的异常
            resultMap.clear();
            resultMap.put("state", TokenState.INVALID.toString());
        }
        return resultMap;
    }
}
