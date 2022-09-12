package br.com.springbootapi.controller;

import br.com.springbootapi.model.Medico;
import br.com.springbootapi.model.response.SucessResponse;
import br.com.springbootapi.security.model.response.JwtTokenResponse;
import br.com.springbootapi.security.utils.JwtTokenUtils;
import br.com.springbootapi.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponse> login(@RequestBody Medico medico) throws Exception {
        medico.setId(loginService.login(medico.getUsername(), medico.getPassword()));
        String jwtToken = JwtTokenUtils.generateToken(medico);

        return ResponseEntity.ok(new JwtTokenResponse(jwtToken));
    }

    @PostMapping("/logoff")
    public ResponseEntity<SucessResponse> logoff(@RequestHeader("token_jwt") String token) {
        loginService.logoff(token);

        return ResponseEntity.ok().body(new SucessResponse("Logoff realizado com sucesso!"));
    }
}