package com.guisebastiao.springbootauth.security;

import com.guisebastiao.springbootauth.exceptions.EntityNotFoundException;
import com.guisebastiao.springbootauth.models.User;
import com.guisebastiao.springbootauth.repositories.UserRepository;
import com.guisebastiao.springbootauth.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = this.recoverToken(request, response);
        String login = this.tokenService.validateToken(token);

        if (login != null) {
            UUID uuid = UUID.fromString(login);

            Optional<User> user = this.userRepository.findById(uuid);

            if (user.isEmpty()) {
                this.removeCookie(response);
                chain.doFilter(request, response);
                return;
            }

            User presentUser = user.get();

            var authentication = new UsernamePasswordAuthenticationToken(presentUser, true, presentUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            request.setAttribute("userId", presentUser.getId());
        }

        chain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request, HttpServletResponse response) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "Authenticate".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }

    private void removeCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("Authenticate", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        response.addCookie(cookie);
    }
}
