package br.com.springbootapi;

import br.com.springbootapi.model.Medico;
import br.com.springbootapi.model.Paciente;
import br.com.springbootapi.model.Usuario;
import br.com.springbootapi.model.request.AtendimentoRequest;
import br.com.springbootapi.model.request.CadastroRequest;
import br.com.springbootapi.security.model.response.JwtTokenResponse;
import br.com.springbootapi.security.utils.JwtTokenUtils;
import br.com.springbootapi.service.MedicoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
class SpringbootApiApplicationTests {

    private String login;
    private String senha;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    MedicoService medicoService;

    @BeforeEach
    public void setUp() {
        this.login = "medico@email.com";
        this.senha = "1234";
    }

    @Test
    public void testeCadastro() throws Exception {
        String confirmacaoSenha = "1234";
        String especialidade = "Cardiologista";
        String cpf = "101.202.303-11";
        String dataNascimento = "10/03/1980";
        String telefone = "(21) 3232-6565";
        CadastroRequest cadastroRequest = new CadastroRequest(this.login, this.senha, confirmacaoSenha, especialidade, cpf, dataNascimento, telefone);

        MvcResult resposta = mockMvc.perform(post("/signup")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cadastroRequest)))
                        .andReturn();

        Assertions.assertEquals(resposta.getResponse().getStatus(), 200, 0d);
    }

    @Test
    public void testeLogin() throws Exception {
        Usuario medico = new Medico(this.login, this.senha);

        MvcResult resposta = mockMvc.perform(post("/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(medico)))
                        .andReturn();

        Assertions.assertEquals(resposta.getResponse().getStatus(), 200, 0d);

        String token = new ObjectMapper().readValue(resposta.getResponse().getContentAsString(), JwtTokenResponse.class).getToken();
        Assertions.assertTrue(JwtTokenUtils.isTokenValid(token));
    }

    @Test
    public void testeCadastroAtendimento() throws Exception {
        String dataAtendimento = "2022-12-25 09:00:00";

        String nomePaciente = "João Castro";
        String cpfPaciente = "101.202.303-11";
        Paciente paciente = new Paciente(nomePaciente, cpfPaciente);

        AtendimentoRequest atendimentoRequest = new AtendimentoRequest(dataAtendimento, paciente);

        MvcResult resposta = mockMvc.perform(post("/attendance")
                        .contentType("application/json")
                        .header("token_jwt", this.generateTokenForTests())
                        .content(objectMapper.writeValueAsString(atendimentoRequest)))
                        .andReturn();

        Assertions.assertEquals(resposta.getResponse().getStatus(), 200, 0d);
    }

    @Test
    public void testeLogoff() throws Exception {
        MvcResult resposta = mockMvc.perform(post("/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new Medico("medico@email.com", "1234"))))
                        .andReturn();

        String token = resposta.getRequest().getHeader("token_jwt");
        JwtTokenUtils.unvalidateToken(token);

        Assertions.assertFalse(JwtTokenUtils.isTokenValid(token));
    }

    /*
        Necessário cadastro e login antes do teste.
    */
    private String generateTokenForTests() {
        Usuario usuario = (Usuario) medicoService.loadUserByUsername(this.login);

        return JwtTokenUtils.generateToken(usuario);
    }
}