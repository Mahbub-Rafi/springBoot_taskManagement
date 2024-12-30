package com.example.springBoot_taskManagement.config;

import com.example.springBoot_taskManagement.services.jwt.UserService;
import com.example.springBoot_taskManagement.utils.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
//@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtutil;

    public JwtAuthenticationFilter(@NonNull JwtUtil jwtutil, UserService userService) {
        this.jwtutil = jwtutil;
        this.userService = userService;
    }

    private final UserService userService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        userEmail = jwtutil.extractUserName(jwt);
        if (StringUtils.isEmpty(userEmail) && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userEmail);
            if (jwtutil.isTokenValid(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetails(request));
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
        }

        filterChain.doFilter(request, response);


    }
//    @Component
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public class SimpleCorsFilter implements Filter {
//
//        @Override
//        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//            HttpServletResponse response = (HttpServletResponse) res;
//            HttpServletRequest request = (HttpServletRequest) req;
//            Map<String, String> map = new HashMap<>();
//            String originHeader = request.getHeader("origin");
//            response.setHeader("Access-Control-Allow-Origin", originHeader);
//            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
//            response.setHeader("Access-Control-Max-Age", "3600");
//            response.setHeader("Access-Control-Allow-Headers", "*");
//
//            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//                response.setStatus(HttpServletResponse.SC_OK);
//            } else {
//                chain.doFilter(req, res);
//            }
//        }
//
//        @Override
//        public void init(FilterConfig filterConfig) {
//        }
//
//        @Override
//        public void destroy() {
//        }
//
//    }
}
