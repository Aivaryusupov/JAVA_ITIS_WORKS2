package ru.yusupov.security.filter;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.GenericFilterBean;
import ru.yusupov.security.auth.TokenAuthentication;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenAuthFilter extends GenericFilterBean {
    private static final String AUTH_TOKEN = "Auth-Token";

    private AuthenticationManager authenticationManager;


    public TokenAuthFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        System.out.println(httpServletRequest.getRequestURI());
        try {
            String headerValue = httpServletRequest.getHeader(AUTH_TOKEN);
            if (isNotRequiringProtection(httpServletRequest)) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else if (headerValue == null || headerValue.equals("")) {
                throw new IllegalArgumentException("Token not found");
            } else {
                authenticationManager.authenticate(new TokenAuthentication(headerValue));
                filterChain.doFilter(servletRequest, servletResponse);
            }
        } catch (AuthenticationException authenticationException) {
            throw new IllegalArgumentException(authenticationException);
        }
    }

    private boolean isNotRequiringProtection(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/users") && request.getMethod().equals("POST")
                || request.getRequestURI().endsWith("favicon.ico")
                || request.getRequestURI().startsWith("/login") && request.getMethod().equals("POST");
    }
}
