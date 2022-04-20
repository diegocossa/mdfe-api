package br.com.conexao.mdfe.api.service.certificado;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Enumeration;

public class CertificadoDataInicioFimValidade {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private MultipartFile file;
    private String senha;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    public CertificadoDataInicioFimValidade(MultipartFile file, String senha, LocalDate dataInicio, LocalDate dataFim) {
        this.file = file;
        this.senha = senha;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public CertificadoDataInicioFimValidade invoke() {
        try {

            KeyStore keystore = KeyStore.getInstance(("PKCS12"));

            InputStream certificadoBytes = new ByteArrayInputStream(file.getBytes());

            keystore.load(certificadoBytes, senha.toCharArray());

            Enumeration<String> eAliases = keystore.aliases();

            while (eAliases.hasMoreElements()) {
                String alias = (String) eAliases.nextElement();
                Certificate certificado = (Certificate) keystore.getCertificate(alias);

                X509Certificate cert = (X509Certificate) certificado;

                dataInicio = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(cert.getNotBefore()));
                dataFim = LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(cert.getNotAfter()));
            }
        } catch (Exception e) {
            log.error("não foi possível salvar o certificado.", e);
            e.printStackTrace();
        }
        return this;
    }
}