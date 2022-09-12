package br.com.springbootapi.controller;

import br.com.springbootapi.exception.RequestException;
import br.com.springbootapi.model.request.CadastroRequest;
import br.com.springbootapi.model.response.SucessResponse;
import br.com.springbootapi.service.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @PostMapping("/signup")
    public ResponseEntity<Object> cadastro(@RequestBody CadastroRequest cadastroRequest) throws RequestException {
        try {
            medicoService.salvar(cadastroRequest);

            return ResponseEntity.ok().body(new SucessResponse("Médico cadastrado com sucesso!"));
        } catch (ParseException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}