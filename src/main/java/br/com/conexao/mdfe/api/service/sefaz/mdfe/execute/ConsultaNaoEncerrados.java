package br.com.conexao.mdfe.api.service.sefaz.mdfe.execute;

import br.com.conexao.mdfe.api.exceptionhandler.MdfeException;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.service.sefaz.mdfe.ConexaoMDFeConfig;
import com.fincatto.documentofiscal.mdfe3.classes.consultanaoencerrados.MDFeConsultaNaoEncerradosRetorno;
import com.fincatto.documentofiscal.mdfe3.classes.consultanaoencerrados.MDFeConsultaNaoEncerradosRetornoInfMDFe;
import com.fincatto.documentofiscal.mdfe3.webservices.WSFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.isNull;

@Service
public class ConsultaNaoEncerrados {

    @Autowired
    private ValidaErro validaErro;

    @Autowired
    private ConexaoMDFeConfig conexaoMDFeConfig;

    private static final Logger logger = LoggerFactory.getLogger(ConsultaNaoEncerrados.class);

    public List<Map<String, String>> consultar(String cnpj) {

        List<Map<String, String>> resultado = new ArrayList<>();

        try {

            MDFeConsultaNaoEncerradosRetorno retorno = new WSFacade(conexaoMDFeConfig.configurar()).consultaNaoEncerrados(cnpj);

            List<MDFeConsultaNaoEncerradosRetornoInfMDFe> listRetorno = retorno.getInfMDFe();

            if (!isNull(listRetorno) && !listRetorno.isEmpty()) {
                for (MDFeConsultaNaoEncerradosRetornoInfMDFe ret : listRetorno) {
                    Map<String, String> manifesto = new HashMap<>();

                    manifesto.put("chave", ret.getChave());
                    manifesto.put("numprotocolo", ret.getNumeroProtocolo());

                    resultado.add(manifesto);
                }
            }

            logger.debug("Consulta de manifestos não encerrados realizada com sucesso!");

            return resultado;

        } catch (Exception ex) {
            logger.error("Erro ao consultar manifestos não encerrados da MDF-e " + this.getClass().getName() + " ERRO: " + ex.toString());

            validaErro.addErro("erro.mdfe-consulta-naoencerrados", "Erro: " + ex.toString() +
                    " Stacktrace: " + Optional.ofNullable(ex.getMessage()).orElse(ex.getStackTrace()[0].toString()));
            throw new MdfeException(validaErro);
        }
    }

}
