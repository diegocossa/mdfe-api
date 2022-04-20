package br.com.conexao.mdfe.api.service.sefaz.nfe.execute;

import br.com.conexao.mdfe.api.exceptionhandler.MdfeException;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.service.sefaz.nfe.ConexaoNfeConfig;
import com.fincatto.documentofiscal.nfe400.classes.nota.consulta.NFNotaConsultaRetorno;
import com.fincatto.documentofiscal.nfe400.webservices.WSFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class ConsultarNotaNfe {

    @Autowired
    private ConexaoNfeConfig conexaoNfeConfig;

    @Autowired
    private ValidaErro validaErro;

    private static final Logger logger = LoggerFactory.getLogger(ConsultarNotaNfe.class);

    public Map<String, String> consultar(String chaveNfe) {

        Map<String, String> resultado = new HashMap<>();

        try {

            NFNotaConsultaRetorno retorno = new WSFacade(conexaoNfeConfig.configurar()).consultaNota(chaveNfe);

            resultado.put("Ambiente", retorno.getAmbiente().getDescricao());
            resultado.put("Status", retorno.getStatus());
            resultado.put("Motivo",retorno.getMotivo());
            resultado.put("UF", retorno.getUf().getCodigo());

            if (!isNull(retorno.getProtocolo())){
                resultado.put("NumeroProtocolo",retorno.getProtocolo().getProtocoloInfo().getNumeroProtocolo());

                // TODO - Utilizar o número do protocolo para buscar os dados da nota.
            }

            logger.debug("Consulta de NF-e realizada com sucesso!");

            return resultado;

        } catch (Exception ex) {
            logger.error("Erro ao consultar NF-e " + this.getClass().getName());

            validaErro.addErro("erro.nfe-consulta", "Erro: " + ex.toString() +
                    " Stacktrace: " + Optional.ofNullable(ex.getMessage()).orElse(ex.getStackTrace()[0].toString()));
            throw new MdfeException(validaErro);
        }
    }

}
