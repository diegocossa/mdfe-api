package br.com.conexao.mdfe.api.service.sefaz.mdfe.execute;

import br.com.conexao.mdfe.api.exceptionhandler.MdfeException;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.mdfe.CancelamentoMdfe;
import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.model.mdfe.SituacaoMDFe;
import br.com.conexao.mdfe.api.service.mdfe.MdfeLancamentoService;
import br.com.conexao.mdfe.api.service.sefaz.mdfe.ConexaoMDFeConfig;
import com.fincatto.documentofiscal.mdfe3.classes.nota.evento.MDFeInfoEventoRetorno;
import com.fincatto.documentofiscal.mdfe3.classes.nota.evento.MDFeRetorno;
import com.fincatto.documentofiscal.mdfe3.webservices.WSFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class CancelaMdfe {

    @Autowired
    private ValidaErro validaErro;

    @Autowired
    private ConexaoMDFeConfig conexaoMDFeConfig;

    @Autowired
    private MdfeLancamentoService mdfeLancamentoService;

    private static final Logger logger = LoggerFactory.getLogger(CancelaMdfe.class);

    public Map<String, String> cancelar(CancelamentoMdfe cancelamentoMdfe, MDFe mdfe) {

        Map<String, String> resultado = new HashMap<>();

        try {

            MDFeRetorno retorno = new WSFacade(conexaoMDFeConfig.configurar())
                    .cancelaMdfe(cancelamentoMdfe.getChave(),
                            cancelamentoMdfe.getProtocolo(),
                            cancelamentoMdfe.getMotivo());

            MDFeInfoEventoRetorno retornoCancelamento = retorno.getEventoRetorno();

            resultado.put("versao", retorno.getVersao());
            resultado.put("Chave", retornoCancelamento.getChave());
            resultado.put("DescEvento", retornoCancelamento.getDescricaoEvento());
            resultado.put("Id", retornoCancelamento.getId());
            resultado.put("Motivo", retornoCancelamento.getMotivo());
            resultado.put("numProtocolo", retornoCancelamento.getNumeroProtocolo());
            resultado.put("tipoEvento", retornoCancelamento.getTipoEvento());
            resultado.put("VersaoAplicativo", retornoCancelamento.getVersaoAplicativo());
            resultado.put("VersaoEvento", retornoCancelamento.getVersaoEvento());
            resultado.put("Ambiente", retornoCancelamento.getAmbiente().getDescricao());
            resultado.put("Status", retornoCancelamento.getCodigoStatus().toString());
            resultado.put("numSequenciaEvento", retornoCancelamento.getNumeroSequencialEvento().toString());
            resultado.put("DtHoraRegistro", retornoCancelamento.getDataHoraRegistro().toString());
            resultado.put("Orgao", retornoCancelamento.getOrgao().getDescricao());

            inserirRetornoMdfe(mdfe, retornoCancelamento);

            logger.debug("Cancelamento de MDF-e realizado com sucesso!");

            return resultado;

        } catch (Exception ex) {
            logger.error("Erro ao efetuar cancelamento da MDF-e " + this.getClass().getName() + " ERRO: " + ex.toString());

            validaErro.addErro("erro.mdfe-cancelamento", "Erro: " + ex.toString() +
                    " Stacktrace: " + Optional.ofNullable(ex.getMessage()).orElse(ex.getStackTrace()[0].toString()));
            throw new MdfeException(validaErro);
        }
    }

    private void inserirRetornoMdfe(MDFe mdfe, MDFeInfoEventoRetorno retorno) {

        if (retorno.getCodigoStatus() == 135) {
            mdfe.setSituacao(SituacaoMDFe.CANCELADO);
        } else {
            mdfe.setSituacao(SituacaoMDFe.AUTORIZADO);
        }

        mdfe.setStatusmdfe(retorno.getCodigoStatus().toString());

        mdfeLancamentoService.atualizaStatusMdfe(mdfe);
    }

}
