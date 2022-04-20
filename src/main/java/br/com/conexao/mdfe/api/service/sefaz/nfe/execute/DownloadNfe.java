package br.com.conexao.mdfe.api.service.sefaz.nfe.execute;

import br.com.conexao.mdfe.api.exceptionhandler.MdfeException;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.service.sefaz.nfe.ConexaoNfeConfig;
import com.fincatto.documentofiscal.nfe.classes.distribuicao.NFDistribuicaoIntRetorno;
import com.fincatto.documentofiscal.nfe400.webservices.WSFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class DownloadNfe {

    @Autowired
    private ConexaoNfeConfig conexaoNfeConfig;

    @Autowired
    private ValidaErro validaErro;

    private static final Logger logger = LoggerFactory.getLogger(DownloadNfe.class);

    public Map<String, String> download(String cnpj, String chaveNfe) {

        Map<String, String> resultado = new HashMap<>();

        try {

            NFDistribuicaoIntRetorno retorno = new WSFacade(conexaoNfeConfig.configurar()).consultarDistribuicaoDFe(cnpj, conexaoNfeConfig.getCUF(),chaveNfe, null, null);

            System.out.println(retorno.getLote().getDocZip().get(1).getValue());

            logger.debug("Downlaod de NF-e realizado com sucesso!");

            return resultado;

        } catch (Exception ex) {
            logger.error("Erro ao efetuar download da NF-e " + this.getClass().getName());

            validaErro.addErro("erro.nfe-consulta", "Erro: " + ex.toString() +
                    " Stacktrace: " + Optional.ofNullable(ex.getMessage()).orElse(ex.getStackTrace()[0].toString()));
            throw new MdfeException(validaErro);
        }
    }

}
