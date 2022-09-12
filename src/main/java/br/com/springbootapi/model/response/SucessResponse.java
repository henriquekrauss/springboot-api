package br.com.springbootapi.model.response;

public class SucessResponse {

    private String mensagem;

    public SucessResponse(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}