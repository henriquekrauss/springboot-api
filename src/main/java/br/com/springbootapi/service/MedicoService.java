package br.com.springbootapi.service;

import br.com.springbootapi.exception.RequestException;
import br.com.springbootapi.model.Medico;
import br.com.springbootapi.model.request.CadastroRequest;
import br.com.springbootapi.repository.MedicoRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MedicoService implements UserDetailsService {

    private final MedicoRepository medicoRepository;

    public MedicoService(MedicoRepository medicoRepository) {
        this.medicoRepository = medicoRepository;
    }

    public void salvar(CadastroRequest cadastroRequest) throws RequestException, ParseException {
        Medico medico = this.validarDadosCadastro(cadastroRequest);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        medico.setSenha(passwordEncoder.encode(medico.getPassword()));

        medicoRepository.save(medico);
    }

    private Medico validarDadosCadastro(CadastroRequest cadastroRequest) throws RequestException, ParseException {
        Pattern patternEmail = Pattern.compile("^[\\w\\.]+\\@[\\w\\.]+$");
        Pattern patternCpf = Pattern.compile("^(\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}|\\d{11})$");
        Pattern patternData = Pattern.compile("^\\d{2}\\/\\d{2}\\/\\d{4}$");
        Pattern patternData2 = Pattern.compile("^\\d{4}\\-\\d{2}\\-\\d{2}$");
        Pattern patternTelefone = Pattern.compile("^\\(?\\d{2}\\)?\\h*\\d?\\h*\\d{4}\\-?\\d{4}$");
        Matcher matcher;

        Medico medico = new Medico();

        matcher = patternEmail.matcher(cadastroRequest.getEmail());
        if (StringUtils.isNotBlank(cadastroRequest.getEmail()) && matcher.find()) {
            medico.setEmail(cadastroRequest.getEmail());
        } else {
            throw new RequestException("E-mail está em um formato inválido ou está vazio");
        }

        if (StringUtils.isNotBlank(cadastroRequest.getSenha()) && StringUtils.isNotBlank(cadastroRequest.getConfirmacaoSenha())) {
            if (cadastroRequest.getSenha().equalsIgnoreCase(cadastroRequest.getConfirmacaoSenha())) {
                medico.setSenha(cadastroRequest.getSenha());
            } else {
                throw new RequestException("Os campos de senha são diferentes");
            }
        } else {
            throw new RequestException("Favor preencher os campos de senha");
        }

        matcher = patternCpf.matcher(cadastroRequest.getCpf());
        if (StringUtils.isNotBlank(cadastroRequest.getCpf()) && matcher.find()) {
            medico.setCpf(cadastroRequest.getCpf().replaceAll("\\D", ""));
        } else {
            throw new RequestException("CPF está em um formato inválido ou está vazio");
        }

        if (StringUtils.isNotBlank(cadastroRequest.getEspecialidade())) {
            medico.setEspecialidade(cadastroRequest.getEspecialidade());
        } else {
            throw new RequestException("Especialidade está em um formato inválido ou está vazio");
        }

        if (StringUtils.isNotBlank(cadastroRequest.getDataNascimento())) {
            matcher = patternData.matcher(cadastroRequest.getDataNascimento());
            if (matcher.find()) {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                medico.setDataNascimento(dateFormat.parse(cadastroRequest.getDataNascimento()));
            } else {
                matcher = patternData2.matcher(cadastroRequest.getDataNascimento());
                if (matcher.find()) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    medico.setDataNascimento(dateFormat.parse(cadastroRequest.getDataNascimento()));
                } else {
                    throw new RequestException("Data está em um formato inválido");
                }
            }
        } else {
            throw new RequestException("Favor preencher o campo de Data");
        }

        matcher = patternTelefone.matcher(cadastroRequest.getTelefone());
        if (StringUtils.isNotBlank(cadastroRequest.getTelefone()) && matcher.find()) {
            medico.setTelefone(cadastroRequest.getTelefone().replaceAll("\\D", ""));
        } else {
            throw new RequestException("Telefone está em um formato inválido ou está vazio");
        }

        return medico;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Medico medicoCadastrado = medicoRepository.findOneByEmail(username);
        if (medicoCadastrado != null) {
            return medicoCadastrado;
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado");
        }
    }
}