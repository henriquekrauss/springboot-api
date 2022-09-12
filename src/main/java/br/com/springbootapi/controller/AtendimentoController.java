package br.com.springbootapi.controller;

import br.com.springbootapi.model.request.AtendimentoRequest;
import br.com.springbootapi.model.response.SucessResponse;
import br.com.springbootapi.service.AtendimentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AtendimentoController {

    @Autowired
    AtendimentoService atendimentoService;

    @PostMapping("/attendance")
    public ResponseEntity<SucessResponse> criarAtendimento(@RequestHeader("token_jwt") String token, @RequestBody AtendimentoRequest atendimentoRequest) throws Exception {
        atendimentoService.cadastrarAtendimento(token, atendimentoRequest);

        return ResponseEntity.ok().body(new SucessResponse("Atendimento criado com sucesso!"));
    }
}