package net.ict_campus.Wordle.security.security;

public class SecurityConstant {
    // Needs to be ATLEAST 32 bytes long
    public static final String SECRET = "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b";

    // 10 days in ms
    public static final long EXPIRATION_TIME = 864_000_000L;
    //whitelisted url's
    public static final String[] API_DOCUMENTATION_URLS = {
            "/swagger.html",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/docs/**",
            "/api-docs/**",
            "/v2/api-docs/**",
            "/v3/api-docs/**"
    };
}