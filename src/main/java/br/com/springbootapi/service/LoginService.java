package br.com.springbootapi.service;

import br.com.springbootapi.security.utils.JwtTokenUtils;
import br.com.springbootapi.exception.RequestException;
import br.com.springbootapi.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    MedicoRepository medicoRepository;

    public int login(String login, String senha) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, senha));

            return medicoRepository.findOneByEmail(login).getId();

        } catch (BadCredentialsException e) {
            throw new RequestException("Credenciais Inválidas");
        }
    }

    public void logoff(String token) {
        JwtTokenUtils.unvalidateToken(token);
    }
}