package br.com.conexao.mdfe.api.exceptionhandler;

public class MdfeEventoDuplicadoException extends MdfeException {
    public MdfeEventoDuplicadoException(ValidaErro validaErro) {
        super(validaErro);
    }
}
