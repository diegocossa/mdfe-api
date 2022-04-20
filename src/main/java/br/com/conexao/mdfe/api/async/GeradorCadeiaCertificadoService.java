package br.com.conexao.mdfe.api.async;

import br.com.conexao.mdfe.api.service.sefaz.mdfe.execute.ConsultaStatusSefazMdfe;
import br.com.conexao.mdfe.api.util.CaminhoRaizApp;
import com.fincatto.documentofiscal.DFAmbiente;
import com.fincatto.documentofiscal.utils.GeraCadeiaCertificados;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class GeradorCadeiaCertificadoService {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaStatusSefazMdfe.class);

    private static final String CNXSENHA = "CNX33#77$99%11&11";

    //HOMOLOGAÇÃO
    public void gerarCadeiaHomologacao() throws Exception {
        String local = CaminhoRaizApp.getCaminhoRaizApp();

        byte[] bytes = GeraCadeiaCertificados.geraCadeiaCertificados(DFAmbiente.HOMOLOGACAO,CNXSENHA);

        FileUtils.writeByteArrayToFile(new File(local+"/cacerts/homologacao.cacerts"), bytes);

        logger.info("Arquivo cacerts de homologação gerado com sucesso!");
    }

    //PRODUÇÃO - Somente produção async pois é chamado primeiro, e para que fique aguardando terminar para ver se
    // ocorre algum erro para retornar ao front.
    @Async
    public void gerarCadeiaProducao() throws Exception {
        String local = CaminhoRaizApp.getCaminhoRaizApp();

        byte[] bytes = GeraCadeiaCertificados.geraCadeiaCertificados(DFAmbiente.PRODUCAO,CNXSENHA);

        FileUtils.writeByteArrayToFile(new File(local+"/cacerts/producao.cacerts"), bytes);

        logger.info("Arquivo cacerts de produção gerado com sucesso!");
    }

    public String getCNXSENHA() {
        return CNXSENHA;
    }
}
