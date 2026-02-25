package com.personal.bidding.app.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ResponseLoggerFilterConfig extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(ResponseLoggerFilterConfig.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            logDetails(requestWrapper, responseWrapper);
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logDetails(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {

        String requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);

        String responseBody = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);

        log.info("RESPONSE DATA -> method: {}, URI: {}, request Body: {}, Status: {}, response body: {}", request.getMethod(), request.getRequestURI(), requestBody, response.getStatus(), responseBody);
    }
}
