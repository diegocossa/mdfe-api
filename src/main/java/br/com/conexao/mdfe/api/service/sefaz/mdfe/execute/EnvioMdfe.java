package br.com.conexao.mdfe.api.service.sefaz.mdfe.execute;

import br.com.conexao.mdfe.api.exceptionhandler.MdfeException;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.service.mdfe.MdfeLancamentoService;
import br.com.conexao.mdfe.api.service.sefaz.mdfe.ConexaoMDFeConfig;
import com.fincatto.documentofiscal.mdfe3.classes.lote.envio.MDFEnvioLoteRetornoDados;
import com.fincatto.documentofiscal.mdfe3.webservices.WSFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyStoreException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
public class EnvioMdfe {

    @Autowired
    private ValidaErro validaErro;

    @Autowired
    private ConexaoMDFeConfig conexaoMDFeConfig;

    @Autowired
    private ConstroiMdfeRodovService constroiMdfeRodovService;

    @Autowired
    private MdfeLancamentoService mdfeLancamentoService;

    @Autowired
    private ConsultaRecibo consultaRecibo;

    private static final Logger logger = LoggerFactory.getLogger(EnvioMdfe.class);

    public MDFe enviar(MDFe mdfe) {

        Map<String, String> resultado = new HashMap<>();

        try {

            MDFEnvioLoteRetornoDados retorno = new WSFacade(conexaoMDFeConfig.configurar()).envioRecepcaoLote(constroiMdfeRodovService.construir(mdfe));

            inserirRetornoMdfe(mdfe, retorno);

            resultado.put("Lote", retorno.getLoteAssinado().getIdLote());
            resultado.put("Ambiente", retorno.getRetorno().getAmbiente().getDescricao());
            resultado.put("Status", retorno.getRetorno().getStatus());
            resultado.put("Motivo", retorno.getRetorno().getMotivo());

            if (!isNull(retorno.getLoteAssinado())) {
                resultado.put("ChaveMDFe", retorno.getLoteAssinado().getMdfe().getInfo().getChaveAcesso());
            }

            if (!isNull(retorno.getRetorno().getInfoRecebimento())) {
                resultado.put("NumRecibo", retorno.getRetorno().getInfoRecebimento().getNumeroRecibo());
            }

            logger.debug("Consulta de status realizada com sucesso!");

            Thread.sleep (1000);
            consultaRecibo.consultar(mdfe);

            return mdfe;

        } catch (KeyStoreException e){
            validaErro.addErro("Falha ao gerar a cadeia de certificados. Tente reimportar o certificado no cadastro de empresas", e.getMessage());
            throw new MdfeException(validaErro);
        } catch (Exception ex) {
            logger.error("Erro ao enviar lote MDF-e " + this.getClass().getName() + " ERRO: " + ex.toString());

            validaErro.addErro("erro.mdfe-envio", "Erro: " + ex.toString() +
                    " Stacktrace: " + Optional.ofNullable(ex.getMessage()).orElse(ex.getStackTrace()[0].toString()));

            throw new MdfeException(validaErro);
        }
    }

    private void inserirRetornoMdfe(MDFe mdfe, MDFEnvioLoteRetornoDados retorno) {

        //Chaves
        mdfe.setChave(retorno.getLoteAssinado().getMdfe().getInfo().getChaveAcesso());
        mdfe.setNumrecibo(retorno.getRetorno().getInfoRecebimento().getNumeroRecibo());

        mdfe.setNmdf(retorno.getLoteAssinado().getMdfe().getInfo().getIdentificacao().getNumero().toString());

        //Complementares
        mdfe.setIdlote(retorno.getLoteAssinado().getIdLote());
        mdfe.setAmbienteenvio(retorno.getRetorno().getAmbiente().getDescricao());
        mdfe.setStatusenvio(retorno.getRetorno().getStatus());
        mdfe.setMotivoenvio(retorno.getRetorno().getMotivo());

        //Arquivo XML
        byte[] arquivo = retorno.getLoteAssinado().getMdfe().toString().getBytes();
        mdfe.setXml(arquivo);

        mdfeLancamentoService.atualizaStatusMdfe(mdfe);
    }
}
