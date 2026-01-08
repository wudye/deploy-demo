package com.easybank.keycloak.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.IOException;
import java.net.URI;

public class KeycloakRedirectAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final String authorizeUrl;
    private final String clientId;
    private final String redirectUri;

    public KeycloakRedirectAuthenticationEntryPoint(String authorizeUrl, String clientId, String redirectUri) {
        this.authorizeUrl = authorizeUrl;
        this.clientId = clientId;
        this.redirectUri = redirectUri;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        URI location = UriComponentsBuilder.fromUriString(authorizeUrl)
                .queryParam("client_id", clientId)
                .queryParam("response_type", "code")
                .queryParam("scope", "openid profile email")
                .queryParam("redirect_uri", redirectUri)
                .build(true).toUri();

        response.setStatus(HttpServletResponse.SC_SEE_OTHER);
        response.setHeader("Location", location.toString());
        response.sendRedirect(location.toString());
    }
}
