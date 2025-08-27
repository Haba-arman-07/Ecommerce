package com.ecom.API.Gateway.security.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ApplicationContext context;

    // Request Limit Code
    private static final int MAX_REQUESTS = 5;
    private static final long TIME_WINDOW_MS = 60_000;


    private final Map<String, RequestInfo> requestMap = new ConcurrentHashMap<>();

    private boolean isRequestAllowed(String ip) {
        long now = Instant.now().toEpochMilli();
        requestMap.putIfAbsent(ip, new RequestInfo(0, now));

        RequestInfo info = requestMap.get(ip);

        // अगर 1 minute पूरा हो गया -> reset counter
        if (now - info.startTime > TIME_WINDOW_MS) {
            info.startTime = now;
            info.count = 0;
        }

        info.count++;
        return info.count <= MAX_REQUESTS;
    }

    private static class RequestInfo {
        int count;
        long startTime;

        RequestInfo(int count, long startTime) {
            this.count = count;
            this.startTime = startTime;
        }
    }
    // --------------------------------------------------------

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();

        if (!requestMap.containsKey(clientIp)) {
            System.out.println("\nUser IP : " + clientIp + "\n");
        }

        // ---- Rate Limit Check ----
        if (!isRequestAllowed(clientIp)) {
            response.setStatus(429);
            response.getWriter().write("Too many requests! Please wait and try again.");
            return;
        }

        // ---- JWT Authentication ----
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userName = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            userName = jwtUtil.extractUsername(token);
        }

        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails =
                    context.getBean(MyUserDetailsService.class).loadUserByUsername(userName);

            if (jwtUtil.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
