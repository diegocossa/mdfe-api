package br.com.conexao.mdfe.api.service.sefaz.nfe.execute;

import br.com.conexao.mdfe.api.exceptionhandler.MdfeException;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.service.sefaz.nfe.ConexaoNfeConfig;
import com.fincatto.documentofiscal.DFModelo;
import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.nfe400.classes.statusservico.consulta.NFStatusServicoConsultaRetorno;
import com.fincatto.documentofiscal.nfe400.webservices.WSFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ConsultaStatusSefazNfe {

    @Autowired
    private ValidaErro validaErro;

    @Autowired
    private ConexaoNfeConfig conexaoNfeConfig;

    private static final Logger logger = LoggerFactory.getLogger(ConsultaStatusSefazNfe.class);

    public Map<String, String> consultar() {

        Map<String, String> resultado = new HashMap<>();

        try {

            NFStatusServicoConsultaRetorno retorno = new WSFacade(conexaoNfeConfig.configurar()).consultaStatus(DFUnidadeFederativa.PR, DFModelo.NFE);

            resultado.put("Ambiente", retorno.getAmbiente().getDescricao());
            resultado.put("Status", retorno.getStatus());
            resultado.put("Motivo",retorno.getMotivo());
            resultado.put("Data recebimento", retorno.getDataRecebimento().toString());
            resultado.put("UF", retorno.getUf().getCodigo());

            logger.debug("Consulta de status do servidor NF-e realizada com sucesso!");

            return resultado;

        } catch (Exception ex) {
            logger.error("Erro ao consultar status da NF-e " + this.getClass().getName());

            validaErro.addErro("erro.nfe-consulta-status", "Erro: " + ex.toString() +
                    " Stacktrace: " + Optional.ofNullable(ex.getMessage()).orElse(ex.getStackTrace()[0].toString()));
            throw new MdfeException(validaErro);
        }
    }
}


