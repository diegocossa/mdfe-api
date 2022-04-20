package br.com.conexao.mdfe.api.exceptionhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValidaErro {

    @Autowired
    private MessageSource messageSource;

    private List<Erro> erros = new ArrayList<>();

    public void addErro(String chaveMessageProperties, String msgDesenvolvedor) {
        erros.add(new Erro(messageSource.getMessage(chaveMessageProperties, null, LocaleContextHolder.getLocale()),
                msgDesenvolvedor));
    }

    /**
     * Adiciona erro com mensagem parametrizada, exemplo na chave certificado.expirado=Certificado expirada na data {0}.
     * o {0} é substituido pelo valor que vem no parametroMsg.
     * @param chaveMessageProperties chave do messages.properties
     * @param msgDesenvolvedor mensagem do desenvolvedor
     * @param parametrosMsg valor a ser substituido pelos {0},{1}
     */
    public void addErro(String chaveMessageProperties, String msgDesenvolvedor, Object... parametrosMsg) {
        erros.add(new Erro(messageSource.getMessage(chaveMessageProperties, parametrosMsg, LocaleContextHolder.getLocale()),
                msgDesenvolvedor));
    }

    public List<Erro> getErros() {
        return erros;
    }

    public Boolean hasError() {
        return !erros.isEmpty();
    }

    public void limparErros() {
        this.erros.clear();
    }

    public void trataErros() {
        if (hasError()) {
            throw new MdfeException(this);
        }
    }
}
