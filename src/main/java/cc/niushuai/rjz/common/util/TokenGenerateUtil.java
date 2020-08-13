package cc.niushuai.rjz.common.util;

import java.util.UUID;

/**
 * @author ns
 * @date 2020/8/13
 */
public class TokenGenerateUtil {
    private TokenGenerateUtil() {
    }

    public static String generateToken() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }
}
