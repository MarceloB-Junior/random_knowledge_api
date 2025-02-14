package com.api.random_knowledge.configs.security;

import com.api.random_knowledge.repositories.UserRepository;
import com.api.random_knowledge.services.AuthenticationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = extractTokenHeader(request);

        if(jwtToken != null){
            String login = authenticationService.validateJwtToken(jwtToken);
            var userModel = userRepository.findByEmail(login)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + login));
            var authentication = new UsernamePasswordAuthenticationToken(userModel,
                    null, userModel.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request,response);
    }

    public String extractTokenHeader(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
            return null;
        }
        return authorizationHeader.split(" ")[1];
    }

}