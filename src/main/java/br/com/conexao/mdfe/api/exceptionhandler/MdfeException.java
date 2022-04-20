package br.com.conexao.mdfe.api.exceptionhandler;

import java.util.List;

public class MdfeException extends RuntimeException{

    private ValidaErro validaErro;

    public MdfeException(ValidaErro validaErro) {
        this.validaErro = validaErro;
    }

    public List<Erro> getValidaErro() {
        return validaErro.getErros();
    }

    public void limparErros(){
        validaErro.limparErros();
    }
}
