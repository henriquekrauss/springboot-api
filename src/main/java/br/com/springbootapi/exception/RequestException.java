package br.com.springbootapi.exception;

public class RequestException extends Exception {

    public RequestException(String mensagemErro) {
        super(mensagemErro);
    }
}