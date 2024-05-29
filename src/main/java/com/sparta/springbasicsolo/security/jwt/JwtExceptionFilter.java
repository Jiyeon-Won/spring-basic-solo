package com.sparta.springbasicsolo.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            log.error(e.getMessage());
            setErrorResponse(HttpStatus.BAD_REQUEST, response, e);
        }
    }

    private void setErrorResponse(HttpStatus status, HttpServletResponse response, Exception e) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");

        Map<String, String> errorDetails = new HashMap<>();
        errorDetails.put("status", String.valueOf(status.value()));
        errorDetails.put("message", e.getMessage());

        ObjectMapper mapper = new ObjectMapper();
        response.getOutputStream().write(mapper.writeValueAsBytes(errorDetails));
    }
}