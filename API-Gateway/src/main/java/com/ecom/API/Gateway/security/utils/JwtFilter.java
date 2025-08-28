package com.ecom.API.Gateway.security.utils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // Request Limit Config
    private static final int MAX_REQUESTS = 5;
    private static final long TIME_WINDOW_SECONDS = 60; // 1 min

    private boolean isRequestAllowed(String ip) {
        String key = "req_limit:" + ip;
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        Long count = ops.increment(key);
//        System.out.println("count : "  + count);

        if (count == 1) {
            redisTemplate.expire(key, TIME_WINDOW_SECONDS, TimeUnit.SECONDS);
        }

        return count <= MAX_REQUESTS;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();
        String key = "req_limit:" + clientIp;

        if (!redisTemplate.hasKey(key)) {
            System.out.println("\nUser IP : " + clientIp + "\n");
        }

        if (!isRequestAllowed(clientIp)) {
            response.setStatus(429);
            response.getWriter().write("Too many requests! Please wait and try After 1 min.");
            return;
        }

        // ---- JWT Authentication Code----
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
