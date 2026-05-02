package com.bhuvi.personalTrackerAPI.config;

import com.bhuvi.personalTrackerAPI.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String requestTokenHeader = getTokenFromCookie(request);

        String mailId = null;
        String jwtToken = null;

        // JWT Token is in the form "Bearer token" or just the token in cookie
        if (requestTokenHeader != null) {
            jwtToken = requestTokenHeader;
            try {
                mailId = jwtService.extractUserMailId(jwtToken);
            } catch (Exception e) {
                logger.error("Unable to get JWT Token or JWT Token has expired");
            }
        }

        // Once we get the token validate it.
        if (mailId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(mailId);

            // if token is valid configure Spring Security to manually set authentication
            if (jwtService.validateToken(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);


                // --- REFRESH COOKIE LOGIC START ---
                // We re-issue the same token (or a newly signed one) to reset the browser timer
                ResponseCookie refreshCookie = ResponseCookie.from("AUTH-TOKEN", jwtToken)
                        .httpOnly(true)
                        .secure(true)    // Match your production/local setup
                        .path("/")
                        .maxAge(10 * 60 * 60) // Reset to 10 hours
                        .sameSite("None")
                        .build();

                response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
                // --- REFRESH COOKIE LOGIC END ---


            }
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("AUTH-TOKEN".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
