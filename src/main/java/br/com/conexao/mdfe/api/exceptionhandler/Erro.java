package br.com.conexao.mdfe.api.exceptionhandler;

public class Erro {
    private String mensagemUsuario;
    private String mensagemDesenvolvedor;

    public Erro(String mensagemUsuario, String mensagemDesenvolvedor) {
        this.mensagemUsuario = mensagemUsuario;
        this.mensagemDesenvolvedor = mensagemDesenvolvedor;
    }

    public String getMensagemDesenvolvedor() {
        return mensagemDesenvolvedor;
    }

    public String getMensagemUsuario() {
        return mensagemUsuario;
    }
}