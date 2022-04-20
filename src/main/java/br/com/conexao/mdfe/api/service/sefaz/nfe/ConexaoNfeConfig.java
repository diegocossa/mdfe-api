package br.com.conexao.mdfe.api.service.sefaz.nfe;

import br.com.conexao.mdfe.api.async.GeradorCadeiaCertificadoService;
import br.com.conexao.mdfe.api.model.certificado.Certificado;
import br.com.conexao.mdfe.api.model.empresa.Empresa;
import br.com.conexao.mdfe.api.service.certificado.CertificadoService;
import br.com.conexao.mdfe.api.service.empresa.EmpresaService;
import br.com.conexao.mdfe.api.service.sefaz.Validacoes;
import br.com.conexao.mdfe.api.tenant.TenantIdEmpresaContext;
import br.com.conexao.mdfe.api.util.CaminhoRaizApp;
import com.fincatto.documentofiscal.DFAmbiente;
import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.nfe.NFeConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Configuration
public class ConexaoNfeConfig extends NFeConfig {

    @Autowired
    private GeradorCadeiaCertificadoService geradorCadeiaCertificado;

    @Autowired
    private CertificadoService certificadoService;

    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private Validacoes validacoes;

    private KeyStore keyStoreCertificado = null;
    private KeyStore keyStoreCadeia = null;

    private String senhaCadeiaCertificado;
    private String senhaCertificado;
    private DFUnidadeFederativa unidadeFederativa;

    @Override
    public DFUnidadeFederativa getCUF() {
        return getUnidadeFederativa();
    }

    @Override
    public String getCertificadoSenha() {
        return getSenhaCertificado();
    }

    @Override
    public String getCadeiaCertificadosSenha() {
        return getSenhaCadeiaCertificado();
    }

    @Override
    public DFAmbiente getAmbiente() {

        Long idEmpresa = TenantIdEmpresaContext.getCurrentTenant();
        validacoes.validarEmpresaTenant(idEmpresa);
        Empresa empresa = empresaService.findByIdEmpresa(idEmpresa);

        return empresa.getTipoambiente();
    }

    @Override
    public String getVersao() {
        return "4.00";
    }

    public ConexaoNfeConfig configurar() {
        Long idEmpresa = TenantIdEmpresaContext.getCurrentTenant();

        validacoes.validarEmpresaTenant(idEmpresa);

        Certificado certificado = certificadoService.findUltimoCertificadoInserido(idEmpresa);
        Empresa empresa = empresaService.findByIdEmpresa(idEmpresa);

        validacoes.validarDadosInternos(idEmpresa, certificado, empresa);

        this.senhaCadeiaCertificado = geradorCadeiaCertificado.getCNXSENHA();
        this.senhaCertificado = certificado.getSenha();
        this.unidadeFederativa = empresa.getUf();

        return this;
    }

    @Override
    public KeyStore getCertificadoKeyStore() throws KeyStoreException {
        if (this.keyStoreCertificado == null) {
            this.keyStoreCertificado = KeyStore.getInstance("PKCS12");

            //String local = CaminhoRaizApp.getCaminhoRaizApp();
            //try (InputStream certificadoStream = new FileInputStream(local+"/tmp/EDJOTRANSPORTES ARAUJO LTDA00580560000162.pfx")) {

            Long idEmpresa = TenantIdEmpresaContext.getCurrentTenant();
            validacoes.validarEmpresaTenant(idEmpresa);

            Certificado certificadoBanco = certificadoService.findUltimoCertificadoInserido(TenantIdEmpresaContext.getCurrentTenant());

            try (InputStream certificadoStream = new ByteArrayInputStream(certificadoBanco.getArquivo())) {
                this.keyStoreCertificado.load(certificadoStream, this.getCertificadoSenha().toCharArray());
            } catch (CertificateException | NoSuchAlgorithmException | IOException e) {
                this.keyStoreCadeia = null;
                throw new KeyStoreException("Nao foi possível montar o KeyStore com a cadeia de certificados", e);
            }
        }
        return this.keyStoreCertificado;
    }

    @Override
    public KeyStore getCadeiaCertificadosKeyStore() throws KeyStoreException {
        if (this.keyStoreCadeia == null) {
            this.keyStoreCadeia = KeyStore.getInstance("JKS");

            //String local = CaminhoRaizApp.getCaminhoRaizApp();

            Long idEmpresa = TenantIdEmpresaContext.getCurrentTenant();
            validacoes.validarEmpresaTenant(idEmpresa);
            Empresa empresa = empresaService.findByIdEmpresa(idEmpresa);

            String localArquivoCacerts = "";

            if (empresa.getTipoambiente().equals(DFAmbiente.HOMOLOGACAO)) {
                //localArquivoCacerts = local+"/cacerts/homologacao.cacerts";
                localArquivoCacerts = "/etc/cacerts/homologacao.cacerts";
            } else if (empresa.getTipoambiente().equals(DFAmbiente.PRODUCAO)) {
                //localArquivoCacerts = local+"/cacerts/producao.cacerts";
                localArquivoCacerts = "/etc/cacerts/producao.cacerts";
            }

            try (InputStream cadeia = new FileInputStream(localArquivoCacerts)) {
                this.keyStoreCadeia.load(cadeia, this.getCadeiaCertificadosSenha().toCharArray());
            } catch (CertificateException | NoSuchAlgorithmException | IOException e) {
                this.keyStoreCadeia = null;
                throw new KeyStoreException("Nao foi possível montar o KeyStore com o certificado", e);
            }
        }
        return this.keyStoreCadeia;
    }

    private String getSenhaCadeiaCertificado() {
        return senhaCadeiaCertificado;
    }

    private String getSenhaCertificado() {
        return senhaCertificado;
    }

    private DFUnidadeFederativa getUnidadeFederativa() {
        return unidadeFederativa;
    }
}
