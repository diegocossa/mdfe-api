package br.com.conexao.mdfe.api.exceptionhandler;

public class MdfeEvitaConsumoIndevidoException extends MdfeException {

    public MdfeEvitaConsumoIndevidoException(ValidaErro validaErro) {
        super(validaErro);
    }
}
