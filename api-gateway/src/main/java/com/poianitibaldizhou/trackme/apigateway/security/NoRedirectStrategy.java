package com.poianitibaldizhou.trackme.apigateway.security;

import org.springframework.security.web.RedirectStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Encapsulates the redirection logic: since REST services are provided, no redirection logic is need
 */
public class NoRedirectStrategy implements RedirectStrategy {
    @Override
    public void sendRedirect(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, String s) throws IOException {
        // No redirect is required with pure REST
    }
}
