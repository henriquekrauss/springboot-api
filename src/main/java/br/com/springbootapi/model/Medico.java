package br.com.springbootapi.model;


import javax.persistence.Entity;
import java.util.Date;

@Entity
public class Medico extends Usuario {

    private String especialidade;
    private Date dataNascimento;
    private String telefone;

    public Medico() {
    }

    public Medico(String email, String senha) {
        super(email, senha);
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}