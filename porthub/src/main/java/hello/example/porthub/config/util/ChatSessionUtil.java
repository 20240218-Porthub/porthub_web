package hello.example.porthub.config.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class ChatSessionUtil {
    public static String generateSessionKey(int userId1, int userId2) {
        // Ensure userId1 is always less than userId2 to maintain consistency
        int lowerId = Math.min(userId1, userId2);
        int higherId = Math.max(userId1, userId2);
        String rawKey = lowerId + "_" + higherId;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawKey.getBytes(StandardCharsets.UTF_8));
            //FIXME: Base64 results '/' and '+' characters which are not URL safe
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating session key", e);
        }
    }
}
