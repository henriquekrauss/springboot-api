package br.com.springbootapi.exception.response;

public class ExceptionResponse {

    private String erro;

    public ExceptionResponse(String erro) {
        this.erro = erro;
    }

    public String getErro() {
        return erro;
    }

    public void setErro(String erro) {
        this.erro = erro;
    }
}