package br.com.conexao.mdfe.api.service.sefaz.mdfe.execute;

import br.com.conexao.mdfe.api.exceptionhandler.MdfeEventoDuplicadoException;
import br.com.conexao.mdfe.api.exceptionhandler.MdfeException;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.model.mdfe.modal_rodoviario.Condutor;
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
public class IncluirCondutor {

    @Autowired
    private ValidaErro validaErro;

    @Autowired
    private ConexaoMDFeConfig conexaoMDFeConfig;

    @Autowired
    private MdfeLancamentoService mdfeLancamentoService;

    private static final Logger logger = LoggerFactory.getLogger(IncluirCondutor.class);

    public Map<String, String> incluir(MDFe rodov, Condutor novoCondutor) {

        Map<String, String> resultado = new HashMap<>();

        try {

            MDFeRetorno retorno = new WSFacade(conexaoMDFeConfig.configurar()).incluirCondutor(rodov.getChave(), novoCondutor.getMotorista().getNome(), novoCondutor.getMotorista().getCpf());

            MDFeInfoEventoRetorno retornoInclusao = retorno.getEventoRetorno();

            resultado.put("Versão", retorno.getVersao());
            resultado.put("Chave", retornoInclusao.getChave());
            resultado.put("Descrição evento", retornoInclusao.getDescricaoEvento());
            resultado.put("Id", retornoInclusao.getId());
            resultado.put("Motivo", retornoInclusao.getMotivo());
            resultado.put("Número protocolo", retornoInclusao.getNumeroProtocolo());
            resultado.put("Tipo evento", retornoInclusao.getTipoEvento());
            resultado.put("Versão aplicativo", retornoInclusao.getVersaoAplicativo());
            resultado.put("Versão evento", retornoInclusao.getVersaoEvento());
            resultado.put("Ambiente", retornoInclusao.getAmbiente().getDescricao());
            resultado.put("Código status", retornoInclusao.getCodigoStatus() != null ? retornoInclusao.getCodigoStatus().toString() : "");
            resultado.put("Número sequencial evento", retornoInclusao.getNumeroSequencialEvento() != null ? retornoInclusao.getNumeroSequencialEvento().toString() : "");
            resultado.put("Data e hora registro", retornoInclusao.getDataHoraRegistro() != null ? retornoInclusao.getDataHoraRegistro().toString() : "");
            resultado.put("Orgão", retornoInclusao.getOrgao().getDescricao());

            // Evento não registrado pela SEFAZ, condutor não foi incluido remover-lo da lista.
            if (!retornoInclusao.getCodigoStatus().equals(135)) {
                rodov.getCondutorlist().remove(novoCondutor);
            }

            if (retornoInclusao.getCodigoStatus().equals(631)) {
                logger.error("Erro ao incluir condutor MDF-e " + this.getClass().getName() + " ERRO: Status Code 631");

                validaErro.addErro("erro.evento-ja-registrado", "O evento já fora registrado para este manifesto.");
                throw new MdfeEventoDuplicadoException(validaErro);
            }

            inserirRetornoMdfe(rodov, retornoInclusao);

            logger.debug("Inclusão de condutor realizada com sucesso!");

            return resultado;

        } catch (Exception ex) {
            logger.error("Erro ao incluir condutor MDF-e " + this.getClass().getName() + " ERRO: " + ex.toString());

            validaErro.addErro("erro.mdfe-inclui-condutor", "Erro: " + ex.toString() +
                    " Stacktrace: " + Optional.ofNullable(ex.getMessage()).orElse(ex.getStackTrace()[0].toString()));
            throw new MdfeException(validaErro);
        }
    }

    private void inserirRetornoMdfe(MDFe mdfe, MDFeInfoEventoRetorno retorno) {
        mdfe.setStatusmdfe(retorno.getCodigoStatus().toString());

        mdfeLancamentoService.atualizaStatusMdfe(mdfe);
    }

}
