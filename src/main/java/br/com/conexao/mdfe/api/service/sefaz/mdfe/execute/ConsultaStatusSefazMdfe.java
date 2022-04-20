package br.com.conexao.mdfe.api.service.sefaz.mdfe.execute;

import br.com.conexao.mdfe.api.exceptionhandler.MdfeException;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.service.sefaz.mdfe.ConexaoMDFeConfig;
import com.fincatto.documentofiscal.mdfe3.classes.consultastatusservico.MDFeConsStatServRet;
import com.fincatto.documentofiscal.mdfe3.webservices.WSFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ConsultaStatusSefazMdfe {

    @Autowired
    private ValidaErro validaErro;

    @Autowired
    private ConexaoMDFeConfig conexaoMDFeConfig;

    private static final Logger logger = LoggerFactory.getLogger(ConsultaStatusSefazMdfe.class);

    private String codigoRetornoStatus=null;

    public Map<String, String> consultar() {

        Map<String, String> resultado = new HashMap<>();

        try {

            MDFeConsStatServRet retorno = new WSFacade(conexaoMDFeConfig.configurar()).consultaStatus(conexaoMDFeConfig.getCUF());

            resultado.put("Ambiente", retorno.getAmbiente().getDescricao());
            resultado.put("Status", retorno.getCodigoStatus());
            resultado.put("Motivo", retorno.getMotivo());
            resultado.put("dtRecebimento", retorno.getDataRecebimento());
            resultado.put("observacao", retorno.getObservacao());
            resultado.put("uf", retorno.getUf().getCodigo());

            codigoRetornoStatus = retorno.getCodigoStatus();

            logger.debug("Consulta de status realizada com sucesso!");

            return resultado;

        } catch (Exception ex) {
            logger.error("Erro ao consultar status da SEFAZ MDF-e " + this.getClass().getName() + " ERRO: " + ex.toString());

            validaErro.addErro("erro.mdfe-consulta-status", "Erro: " + ex.toString() +
                    " Stacktrace: " + Optional.ofNullable(ex.getMessage()).orElse(ex.getStackTrace()[0].toString()));
            throw new MdfeException(validaErro);
        }
    }

    public Boolean emOperacao(){
        consultar();

        return codigoRetornoStatus.equals("107");
    }
}
