//package com.mwu.whatsappclone.demo.keycloakdemo;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.io.Writer;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//import java.time.OffsetDateTime;
//
//@Component
//public class SecurityContextFilter extends OncePerRequestFilter {
//
//
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//        Path logFile = Paths.get("logs/authorization.log");
//        Files.createDirectories(logFile.getParent());
//        Writer writer = Files.newBufferedWriter(logFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
//        // 在每个请求开始时检查
//        Authentication auth =
//                SecurityContextHolder.getContext().getAuthentication();
//
//        System.out.println("--- Security Context Filter ---");
//        if (auth != null) {
//            System.out.println("Request: " + request.getRequestURI());
//            System.out.println("Authenticated: " + auth.isAuthenticated());
//            System.out.println("Principal: " + auth.getPrincipal());
//        } else {
//            System.out.println("Request: " + request.getRequestURI());
//            System.out.println("Not authenticated");
//        }
//
//        String header = request.getHeader("Authorization");
//        writer.write(OffsetDateTime.now() + " -> header=" + header + ", length=" +
//                (header == null ? 0 : header.length()) + System.lineSeparator());
//        writer.flush();
//        filterChain.doFilter(request, response);
//        filterChain.doFilter(request, response);
//    }
//}
