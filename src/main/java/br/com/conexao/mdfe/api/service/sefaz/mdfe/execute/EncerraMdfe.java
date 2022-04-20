package br.com.conexao.mdfe.api.service.sefaz.mdfe.execute;

import br.com.conexao.mdfe.api.exceptionhandler.MdfeException;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.mdfe.EncerramentoMdfe;
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

import static java.util.Objects.nonNull;

@Service
public class EncerraMdfe {

    @Autowired
    private ValidaErro validaErro;

    @Autowired
    private ConexaoMDFeConfig conexaoMDFeConfig;

    @Autowired
    private MdfeLancamentoService mdfeLancamentoService;

    private static final Logger logger = LoggerFactory.getLogger(EncerraMdfe.class);

    public Map<String, String> encerrar(EncerramentoMdfe encerramentoMdfe, MDFe rodov) {

        Map<String, String> resultado = new HashMap<>();

        try {

            MDFeRetorno mdFeRetorno = new WSFacade(conexaoMDFeConfig.configurar())
                    .encerramento(encerramentoMdfe.getChave(),
                            encerramentoMdfe.getNumprotocolo(),
                            encerramentoMdfe.getCodigomunicipio(),
                            encerramentoMdfe.getDataencerramento(),
                            encerramentoMdfe.getUf());

            MDFeInfoEventoRetorno retornoEncerramento = mdFeRetorno.getEventoRetorno();

            resultado.put("versao", mdFeRetorno.getVersao());
            resultado.put("Chave", retornoEncerramento.getChave());
            resultado.put("descrEvento", retornoEncerramento.getDescricaoEvento());
            resultado.put("Id", retornoEncerramento.getId());
            resultado.put("Motivo", retornoEncerramento.getMotivo());
            resultado.put("numProtocolo", retornoEncerramento.getNumeroProtocolo());
            resultado.put("tipoEvento", retornoEncerramento.getTipoEvento());
            resultado.put("versaoAplicativo", retornoEncerramento.getVersaoAplicativo());
            resultado.put("versaoEvento", retornoEncerramento.getVersaoEvento());
            resultado.put("Ambiente", retornoEncerramento.getAmbiente().getDescricao());
            resultado.put("Status", retornoEncerramento.getCodigoStatus().toString());
            resultado.put("Orgao", retornoEncerramento.getOrgao().getDescricao());

            if (rodov != null) {
                inserirRetornoMdfe(rodov, retornoEncerramento);
            } else {
                MDFe manifestoSalvo = mdfeLancamentoService.findByChave(encerramentoMdfe.getChave());

                if (nonNull(manifestoSalvo)) {
                    inserirRetornoMdfe(manifestoSalvo, retornoEncerramento);
                }
            }

            logger.debug("Encerramendo de MDF-e realizada com sucesso!");

            return resultado;

        } catch (Exception ex) {
            logger.error("Erro ao efetuar encerramento da MDF-e " + this.getClass().getName() + ex.toString() + " ERRO: " + ex.toString());

            validaErro.addErro("erro.mdfe-encerramento", "Erro: " + ex.toString() +
                    " Stacktrace: " + Optional.ofNullable(ex.getMessage()).orElse(ex.getStackTrace()[0].toString()));
            throw new MdfeException(validaErro);
        }
    }

    private void inserirRetornoMdfe(MDFe mdfe, MDFeInfoEventoRetorno retorno) {

        if (retorno.getCodigoStatus() == 135) {
            mdfe.setSituacao(SituacaoMDFe.ENCERRADO);
        } else {
            mdfe.setSituacao(SituacaoMDFe.AUTORIZADO);
        }

        mdfe.setStatusmdfe(retorno.getCodigoStatus().toString());

        mdfeLancamentoService.atualizaStatusMdfe(mdfe);
    }

}
