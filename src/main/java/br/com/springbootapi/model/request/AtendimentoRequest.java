package br.com.springbootapi.model.request;

import br.com.springbootapi.model.Paciente;

public class AtendimentoRequest {
    private String dataHora;
    private Paciente paciente;

    public AtendimentoRequest() {
    }

    public AtendimentoRequest(String dataHora, Paciente paciente) {
        this.dataHora = dataHora;
        this.paciente = paciente;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}