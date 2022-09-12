package br.com.springbootapi.security.filter;

import br.com.springbootapi.security.utils.JwtTokenUtils;
import br.com.springbootapi.service.MedicoService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private MedicoService medicoService;

    public JwtTokenFilter(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token_jwt");
        if (JwtTokenUtils.isTokenValid(token)) {
            this.autenticar(request, token);
            response.addHeader("token_jwt", token);
        }

        filterChain.doFilter(request, response);
    }

    private void autenticar(HttpServletRequest request, String token) {
        String username = JwtTokenUtils.getUsernameFromToken(token);
        UserDetails usuario = medicoService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(usuario, null, null);
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }
}