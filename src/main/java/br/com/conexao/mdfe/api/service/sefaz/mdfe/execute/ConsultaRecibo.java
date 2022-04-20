package br.com.conexao.mdfe.api.service.sefaz.mdfe.execute;

import br.com.conexao.mdfe.api.exceptionhandler.MdfeEvitaConsumoIndevidoException;
import br.com.conexao.mdfe.api.exceptionhandler.MdfeException;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.model.mdfe.SituacaoMDFe;
import br.com.conexao.mdfe.api.service.mdfe.MdfeLancamentoService;
import br.com.conexao.mdfe.api.service.sefaz.Validacoes;
import br.com.conexao.mdfe.api.service.sefaz.mdfe.ConexaoMDFeConfig;
import com.fincatto.documentofiscal.mdfe3.classes.consultaRecibo.MDFeConsultaReciboRetorno;
import com.fincatto.documentofiscal.mdfe3.classes.nota.consulta.MDFeNotaConsultaRetorno;
import com.fincatto.documentofiscal.mdfe3.webservices.WSFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Service
public class ConsultaRecibo {

    @Autowired
    private ValidaErro validaErro;

    @Autowired
    private ConexaoMDFeConfig conexaoMDFeConfig;

    @Autowired
    private MdfeLancamentoService mdfeLancamentoService;

    @Autowired
    private Validacoes validacoes;

    private static final Logger logger = LoggerFactory.getLogger(ConsultaRecibo.class);

    public MDFe consultar(MDFe mdFe) {

        Map<String, String> resultado = new HashMap<>();

        try {

            validacoes.validarLimiteConsultaRecibo(mdFe);

            if (mdFe.getNumprotocolo() != null) {
                MDFeNotaConsultaRetorno retornoMdfe = new WSFacade(conexaoMDFeConfig.configurar()).consultaMdfe(mdFe.getChave());

                resultado.put("Status", retornoMdfe.getStatus());

                if (!isNull(retornoMdfe.getProtocolo())) {
                    resultado.put("Chave", retornoMdfe.getProtocolo().getProtocoloInfo().getChave());
                    resultado.put("Retorno", retornoMdfe.getProtocolo().getProtocoloInfo().getMotivo());
                    resultado.put("Protocolo", retornoMdfe.getProtocolo().getProtocoloInfo().getNumeroProtocolo());
                }

                resultado.put("Ambiente", retornoMdfe.getAmbiente().getDescricao());
                resultado.put("Motivo", retornoMdfe.getMotivo());

                inserirRetornoMdfe(mdFe, retornoMdfe);
            } else {
                MDFeConsultaReciboRetorno retornoRecibo = new WSFacade(conexaoMDFeConfig.configurar()).consultaRecibo(mdFe.getNumrecibo());
                resultado.put("Status", retornoRecibo.getCodigoStatus());
                resultado.put("Recibo", retornoRecibo.getNumeroRecibo());

                if (!isNull(retornoRecibo.getMdfProtocolo())) {
                    resultado.put("Chave", retornoRecibo.getMdfProtocolo().getProtocoloInfo().getChave());
                    resultado.put("Retorno", retornoRecibo.getMdfProtocolo().getProtocoloInfo().getMotivo());
                    resultado.put("Protocolo", retornoRecibo.getMdfProtocolo().getProtocoloInfo().getNumeroProtocolo());
                }

                resultado.put("Ambiente", retornoRecibo.getAmbiente().getDescricao());
                resultado.put("Motivo", retornoRecibo.getMotivo());

                inserirRetornoMdfe(mdFe, retornoRecibo);
            }



            logger.debug("Consulta de recibo de MDF-e realizada com sucesso!");
            logger.info(resultado.toString());

            return mdFe;

        } catch (MdfeEvitaConsumoIndevidoException ex) {
            mdFe.setSituacao(SituacaoMDFe.PROCESSAMENTO);
            mdfeLancamentoService.salvar(mdFe);

            logger.error("Erro ao consultar recibo da MDF-e " + this.getClass().getName() + " ERRO: " + ex.toString());
            ex.printStackTrace();

            validaErro.addErro("erro.mdfe-consulta-recibo", "Erro: " + ex.toString() +
                    " Stacktrace: " + Optional.ofNullable(ex.getMessage()).orElse(ex.getStackTrace()[0].toString()));
            throw new MdfeException(validaErro);
        } catch (Exception ex) {
            logger.error("Erro ao consultar recibo da MDF-e " + this.getClass().getName() + " ERRO: " + ex.toString());
            ex.printStackTrace();

            validaErro.addErro("erro.mdfe-consulta-recibo", "Erro: " + ex.toString() +
                    " Stacktrace: " + Optional.ofNullable(ex.getMessage()).orElse(ex.getStackTrace()[0].toString()));
            throw new MdfeException(validaErro);
        }
    }

    private void inserirRetornoMdfe(MDFe mdfe, MDFeConsultaReciboRetorno retorno) {

        //Data da ultima consulta;
        mdfe.setDtultimaconsultarecibo(LocalDateTime.now());

        //Chaves
        if (nonNull(retorno.getMdfProtocolo())) {

            if (nonNull(retorno.getMdfProtocolo().getProtocoloInfo())) {
                if (nonNull(retorno.getMdfProtocolo().getProtocoloInfo().getChave())) {
                    mdfe.setChave(retorno.getMdfProtocolo().getProtocoloInfo().getChave());
                }

                //Protocolo
                if (nonNull(retorno.getMdfProtocolo().getProtocoloInfo().getNumeroProtocolo())) {
                    mdfe.setNumprotocolo(retorno.getMdfProtocolo().getProtocoloInfo().getNumeroProtocolo());
                }

                if (nonNull(retorno.getMdfProtocolo().getProtocoloInfo().getMotivo())) {
                    mdfe.setRetornorecibo(retorno.getMdfProtocolo().getProtocoloInfo().getMotivo());
                }

                if (nonNull(retorno.getMdfProtocolo().getProtocoloInfo().getStatus())) {
                    mdfe.setStatusmdfe(retorno.getMdfProtocolo().getProtocoloInfo().getStatus());
                }
            }
        }

        //Complementares
        mdfe.setAmbienterecibo(retorno.getAmbiente().getDescricao());
        mdfe.setStatusrecibo(retorno.getCodigoStatus());
        mdfe.setMotivorecibo(retorno.getMotivo());

        atualizarStatusMdfe(mdfe);

        mdfeLancamentoService.atualizaStatusMdfe(mdfe);
    }

    private void inserirRetornoMdfe(MDFe mdfe, MDFeNotaConsultaRetorno retorno) {

        //Data da ultima consulta;
        mdfe.setDtultimaconsultarecibo(LocalDateTime.now());

        //Chaves
        if (nonNull(retorno.getProtocolo())) {

            if (nonNull(retorno.getProtocolo().getProtocoloInfo())) {
                if (nonNull(retorno.getProtocolo().getProtocoloInfo().getChave())) {
                    mdfe.setChave(retorno.getProtocolo().getProtocoloInfo().getChave());
                }

                //Protocolo
                if (nonNull(retorno.getProtocolo().getProtocoloInfo().getNumeroProtocolo())) {
                    mdfe.setNumprotocolo(retorno.getProtocolo().getProtocoloInfo().getNumeroProtocolo());
                }

                if (nonNull(retorno.getProtocolo().getProtocoloInfo().getMotivo())) {
                    mdfe.setRetornorecibo(retorno.getProtocolo().getProtocoloInfo().getMotivo());
                }

                if (nonNull(retorno.getStatus())) {
                    mdfe.setStatusmdfe(retorno.getStatus());
                }
            }
        }

        //Complementares
        mdfe.setAmbienterecibo(retorno.getAmbiente().getDescricao());
        mdfe.setStatusrecibo(retorno.getStatus());
        mdfe.setMotivorecibo(retorno.getMotivo());

        atualizarStatusMdfe(mdfe);

        mdfeLancamentoService.atualizaStatusMdfe(mdfe);
    }

    private void atualizarStatusMdfe(MDFe mdfe) {
        try {
            if (Integer.parseInt(mdfe.getStatusmdfe()) == 100) {
                mdfe.setSituacao(SituacaoMDFe.AUTORIZADO);
            } else if (Integer.parseInt(mdfe.getStatusmdfe()) >= 203) {
                mdfe.setSituacao(SituacaoMDFe.REJEITADO);
            } else if (Integer.parseInt(mdfe.getStatusmdfe()) == 101) {
                mdfe.setSituacao(SituacaoMDFe.CANCELADO);
            } else if (Integer.parseInt(mdfe.getStatusmdfe()) == 100) {
                mdfe.setSituacao(SituacaoMDFe.AUTORIZADO);
            } else if (Integer.parseInt(mdfe.getStatusmdfe()) == 132) {
                mdfe.setSituacao(SituacaoMDFe.ENCERRADO);
            } else {
                mdfe.setSituacao(SituacaoMDFe.PROCESSAMENTO);
            }
        } catch (NumberFormatException e) {
            mdfe.setSituacao(SituacaoMDFe.PROCESSAMENTO);
        }
    }
}
