package br.com.conexao.mdfe.api.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("mdfe")
public class MdfeProperty {

    private final Seguranca seguranca = new Seguranca();

    public Seguranca getSeguranca() {
        return seguranca;
    }

    public static class Seguranca{
        private Boolean enableHttps=false;
        private String[] origemPermitida = {"http://localhost:80"};

        public Boolean isEnableHttps(){
            return enableHttps;
        }

        public String[] getOrigemPermitida() {
            return origemPermitida;
        }

        public void setOrigemPermitida(String[] origemPermitida) {
            this.origemPermitida = origemPermitida;
        }

        public void setEnableHttps(Boolean enableHttps) {
            this.enableHttps = enableHttps;
        }
    }

    private final cacerts caCerts = new cacerts();

    public cacerts getCaCerts() {
        return caCerts;
    }

    public static class cacerts {
        private Boolean gerarCacerts =false;

        public Boolean getGerarCacerts() {
            return gerarCacerts;
        }

        public void setGerarCacerts(Boolean gerarCacerts) {
            this.gerarCacerts = gerarCacerts;
        }
    }

    private final ambiente ambiente = new ambiente();

    public ambiente getAmbiente() {
        return ambiente;
    }

    public static class ambiente {
        private Boolean producao =false;

        public Boolean getProducao() {
            return producao;
        }

        public void setProducao(Boolean producao) {
            this.producao = producao;
        }
    }

    private final urlqrcode urlQrCode = new urlqrcode();

    public urlqrcode getUrlQrCode() {
        return urlQrCode;
    }

    public static class urlqrcode {
        private String URL = "https://dfe-portal.svrs.rs.gov.br/mdfe/QRCode?chMDFe=";

        public String getURL() {
            return URL;
        }

        public void setURL(String URL) {
            this.URL = URL;
        }
    }
}
