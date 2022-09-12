package br.com.springbootapi.service;

import br.com.springbootapi.security.utils.JwtTokenUtils;
import br.com.springbootapi.exception.RequestException;
import br.com.springbootapi.model.Atendimento;
import br.com.springbootapi.model.Paciente;
import br.com.springbootapi.model.request.AtendimentoRequest;
import br.com.springbootapi.repository.AtendimentoRepository;
import br.com.springbootapi.repository.PacienteRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final PacienteRepository pacienteRepository;

    public AtendimentoService(AtendimentoRepository atendimentoRepository, PacienteRepository pacienteRepository) {
        this.atendimentoRepository = atendimentoRepository;
        this.pacienteRepository = pacienteRepository;
    }

    public Atendimento cadastrarAtendimento(String token, AtendimentoRequest atendimentoRequest) throws RequestException, ParseException {
        Atendimento atendimento = this.validarDadosAtendimento(atendimentoRequest);
        Paciente paciente = atendimentoRequest.getPaciente();

        int idPaciente = getIdByCpf(paciente.getCpf());
        if (idPaciente == 0) {
            paciente.setCpf(paciente.getCpf().replaceAll("\\D", ""));
            idPaciente = pacienteRepository.save(paciente).getId();
        }

        atendimento.setIdMedico(JwtTokenUtils.getIdFromToken(token));
        atendimento.setIdPaciente(idPaciente);

        return atendimentoRepository.save(atendimento);
    }

    private int getIdByCpf(String cpf) {
        Paciente pacienteCadastrado = pacienteRepository.findOneByCpf(cpf.replaceAll("\\D", ""));

        if (pacienteCadastrado != null) {
            return pacienteCadastrado.getId();
        }

        return 0;
    }

    private Atendimento validarDadosAtendimento(AtendimentoRequest atendimentoRequest) throws RequestException, ParseException {
        Pattern patternData = Pattern.compile("^\\d{2}\\/\\d{2}\\/\\d{4}\\h+\\d{2}\\:\\d{2}\\:\\d{2}$");
        Pattern patternData2 = Pattern.compile("^\\d{4}\\-\\d{2}\\-\\d{2}\\h+\\d{2}\\:\\d{2}\\:\\d{2}$");
        Pattern patternCpf = Pattern.compile("^(\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}|\\d{11})$");
        Matcher matcher;

        Atendimento atendimento = new Atendimento();

        if (StringUtils.isNotBlank(atendimentoRequest.getDataHora())) {
            matcher = patternData.matcher(atendimentoRequest.getDataHora());
            if (matcher.find()) {
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                atendimento.setDataAtendimento(dateFormat.parse(atendimentoRequest.getDataHora()));
            } else {
                matcher = patternData2.matcher(atendimentoRequest.getDataHora());
                if (matcher.find()) {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    atendimento.setDataAtendimento(dateFormat.parse(atendimentoRequest.getDataHora()));
                } else {
                    throw new RequestException("Data está em um formato inválido");
                }
            }

            if (atendimento.getDataAtendimento().before(new Date())) {
                throw new RequestException("O atendimento só pode ser agendado para o futuro");
            }
        } else {
            throw new RequestException("Favor preencher o campo de Data");
        }

        if (!StringUtils.isNotBlank(atendimentoRequest.getPaciente().getNome())) {
            throw new RequestException("Campo de nome do paciente é obrigatório");
        }

        matcher = patternCpf.matcher(atendimentoRequest.getPaciente().getCpf());
        if (!StringUtils.isNotBlank(atendimentoRequest.getPaciente().getCpf()) || !matcher.find()) {
            throw new RequestException("CPF está em um formato inválido ou está vazio");
        }

        return atendimento;
    }
}