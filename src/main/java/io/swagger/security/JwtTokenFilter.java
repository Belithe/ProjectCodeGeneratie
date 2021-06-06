package io.swagger.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Security;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = jwtTokenProvider.resolveToken(httpServletRequest);

        try {
//            if (token == null) {
//                httpServletResponse.setContentType("application/json");
//                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                httpServletResponse.getOutputStream().println("{ \"code\": 401, \"message\": \"No authentication token was given.\" }");
//                return;
//            }

            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        catch (ResponseStatusException ex)
        {
            SecurityContextHolder.clearContext();
            throw ex;
        }


        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}

