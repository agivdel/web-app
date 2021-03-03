package agivdel.webApp1311.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtil {
    public static void storeUserCookie(HttpServletResponse resp, String username) {
        Cookie cookieUsername = new Cookie("username", username);
        System.out.println("username in cookie: " + username);
        cookieUsername.setMaxAge(24 * 60 * 60);
        resp.addCookie(cookieUsername);
    }

    public static String getUsernameInCookie(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static void deleteUserCookie(HttpServletResponse resp) {
        Cookie cookieUserName = new Cookie("username", null);
        //cookie недействительна через 0 секунд
        cookieUserName.setMaxAge(0);
        resp.addCookie(cookieUserName);
    }
}