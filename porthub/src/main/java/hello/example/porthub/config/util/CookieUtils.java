package hello.example.porthub.config.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public class CookieUtils {

    public static final int MAX_ENTRIES = 5;
    public static final String COOKIE_NAME = "portfolioCookie";

    public static Map<String, String> getCookieData(Cookie[] cookies, String cookieName) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                String value = new String(Base64.getDecoder().decode(cookie.getValue()));
                Gson gson = new Gson();
                Type type = new TypeToken<LinkedHashMap<String, String>>(){}.getType();
                return gson.fromJson(value, type);
            }
        }
        return new LinkedHashMap<>();
    }

    public static void setCookieData(HttpServletResponse response, String cookieName, Map<String, String> data) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        String encodedJson = Base64.getEncoder().encodeToString(json.getBytes());
        Cookie cookie = new Cookie(cookieName, encodedJson);
        cookie.setMaxAge(60 * 60 * 24 * 1); // 7일간 유효
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static void addPortfolioData(Cookie[] cookies, HttpServletResponse response, String cookieName, String portfolioID, String thumbnailUrl) {
        Map<String, String> data = getCookieData(cookies, cookieName);

        if (data.containsKey(portfolioID)) {
            data.remove(portfolioID); // 중복 제거
        } else if (data.size() >= MAX_ENTRIES) {
            String oldestKey = data.keySet().iterator().next(); // 가장 오래된 항목 찾기
            data.remove(oldestKey); // 가장 오래된 항목 제거
        }

        data.put(portfolioID, thumbnailUrl); // 새로운 항목 추가
        setCookieData(response, cookieName, data); // 쿠키에 저장
    }
}
