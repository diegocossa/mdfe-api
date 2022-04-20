package br.com.conexao.mdfe.api.service.documento;

import br.com.conexao.mdfe.api.async.AsyncEmailComponent;
import br.com.conexao.mdfe.api.exceptionhandler.ValidaErro;
import br.com.conexao.mdfe.api.model.mdfe.MDFe;
import br.com.conexao.mdfe.api.service.mdfe.MdfeLancamentoService;
import br.com.conexao.mdfe.api.tenant.TenancyInterceptor;
import br.com.conexao.mdfe.api.tenant.TenantContext;
import br.com.conexao.mdfe.api.util.GeraUrlQrCode;
import br.com.conexao.mdfe.api.util.ValidaEmailValido;
import com.fincatto.documentofiscal.DFAmbiente;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.nio.file.FileSystems.getDefault;
import static java.util.Objects.isNull;

@Service
public class DocDAMDFEService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ValidaErro validaErro;

    @Autowired
    private AsyncEmailComponent asyncEmailComponent;

    @Autowired
    private MdfeLancamentoService mdfeLancamentoService;

    @Autowired
    private GeraUrlQrCode geraUrlQrCode;


    public void mdfeValida(Long idmdfe){
        MDFe rodov = mdfeLancamentoService.findByIdMdfe(Optional.ofNullable(idmdfe).orElse(0L));

        if (isNull(rodov)){
            validaErro.addErro("damdfe.invalido","MDF-e não existe para o ID enviado. - Método: mdfeValida - Objeto: DocumentoService.java");
            validaErro.trataErros();
        }
    }

    public void validarEmails(List<String> emails){
        for (String e : emails) {
            if (!ValidaEmailValido.isValido(e)){
                validaErro.addErro("email.invalido","Um ou mais e-mails informados na lista são inválidos - Método: gerarDAMDFE - Objeto: DocumentoResource.java");
                validaErro.trataErros();
            }
        }
    }

    public Map<String, Object> postDAMDFE(Long idmdfe) throws SQLException {

        Map<String, Object> parametros = new HashMap<>();

        parametros.put("format", "pdf");

        parametros.put("idmdfe", idmdfe);

        URL url = getClass().getResource("/documentos");

        //Sub Relatorios
        parametros.put("subMotorista", url+"/documento_Motoristas.jasper");
        parametros.put("subValePedagio", url+"/documento_Vale_Pedagio.jasper");
        parametros.put("subVeiculoTracao", url+"/documento_Veiculo_Tracao.jasper");

        Connection con = dataSource.getConnection();
        con.setSchema(TenancyInterceptor.getTenantId());
        parametros.put("REPORT_CONNECTION", con);

        MDFe rodov = mdfeLancamentoService.findByIdMdfe(idmdfe);
        parametros.put("ambiente_prod", rodov.getAmbienteenvio().equalsIgnoreCase("Produção"));
        parametros.put("tipo_emissao_normal", rodov.getTpemis().equalsIgnoreCase("Normal"));

        String ambiente;
        if ("Produção".equals(rodov.getAmbienteenvio())) {
            ambiente = "1";
        } else {
            ambiente = "2";
        }

        parametros.put("chave_qrcode", geraUrlQrCode.gerar(rodov.getChave(), ambiente));

        return parametros;
    }

    public byte[] gerarBytesDAMDFE(Long idMdfe) throws Exception {
        InputStream resources = getClass().getResourceAsStream("/documentos/" + "documento_DAMDFE.jasper");
        JasperPrint jasperPrint = JasperFillManager.fillReport(resources, postDAMDFE(idMdfe));

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public void gerarPDFEnviarEmail(Long idmdfe, List<String> email) throws SQLException, JRException {
        InputStream resources = getClass().getResourceAsStream("/documentos/" + "documento_DAMDFE.jasper");
        JasperPrint jasperPrint = JasperFillManager.fillReport(resources, postDAMDFE(idmdfe));

        String destino = this.local + "/" +TenantContext.getCurrentTenant()+"_DAMDFE_"+idmdfe+".pdf";
        JasperExportManager.exportReportToPdfFile(jasperPrint, destino);

        asyncEmailComponent.asyncEnviarEmailDAMDFE(destino, email);
    }

    private static final Logger logger = LoggerFactory.getLogger(DocDAMDFEService.class);

    public Path local;

    public DocDAMDFEService() {
        //Pasta para gerar os documentos para enviar por e-mail
        this(getDefault().getPath(".documentos_pdf"));
    }

    public DocDAMDFEService(Path path) {
        this.local = path;
        criarPastas();
    }


    private void criarPastas() {
        try {
            Files.createDirectories(this.local);

            if (logger.isDebugEnabled()) {
                logger.debug("Pasta criada para salvar relatórios em PDF.");
                logger.debug("Pasta default: " + this.local.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro criando pasta para salvar PDF", e);
        }
    }

    public Path getLocal() {
        return local;
    }

}
