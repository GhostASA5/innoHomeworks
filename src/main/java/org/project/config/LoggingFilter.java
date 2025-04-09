package org.project.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.project.model.LogEntry;
import org.project.repository.LogEntryRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class LoggingFilter extends OncePerRequestFilter {

    private final LogEntryRepository logEntryRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Request URI: " + request.getRequestURI());

        String authHeader = request.getHeader("Authorization");
        String decodedCredentials = null;

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring(6);
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            decodedCredentials = new String(decodedBytes, StandardCharsets.UTF_8);
            System.out.println("Authorization Header (decoded): " + decodedCredentials);
        }

        LogEntry logEntry = new LogEntry();
        logEntry.setMethod(request.getMethod());
        logEntry.setUri(request.getRequestURI());
        logEntry.setAuthHeader(decodedCredentials);
        logEntry.setTimestamp(LocalDateTime.now());
        logEntryRepository.save(logEntry);

        filterChain.doFilter(request, response);
    }
}
