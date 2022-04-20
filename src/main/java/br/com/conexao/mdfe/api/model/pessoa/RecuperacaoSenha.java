package br.com.conexao.mdfe.api.model.pessoa;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
public class RecuperacaoSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idrecuperacaosenha;

    @Size(max = 40)
    private String email;

    @Size(max = 100)
    private String hash;

    @Transient
    private String senhausuario;

    @Transient
    private String senhausuarioconfirma;

    @Size(max = 25)
    private String tenantid;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dthorasolicitacao = LocalDateTime.now();

    public Long getIdrecuperacaosenha() {
        return idrecuperacaosenha;
    }

    public void setIdrecuperacaosenha(Long idrecuperacaosenha) {
        this.idrecuperacaosenha = idrecuperacaosenha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getTenantid() {
        return tenantid;
    }

    public void setTenantid(String tenantid) {
        this.tenantid = tenantid;
    }

    public String getSenhausuario() {
        return senhausuario;
    }

    public void setSenhausuario(String senhausuario) {
        this.senhausuario = senhausuario;
    }

    public String getSenhausuarioconfirma() {
        return senhausuarioconfirma;
    }

    public void setSenhausuarioconfirma(String senhausuarioconfirma) {
        this.senhausuarioconfirma = senhausuarioconfirma;
    }

    public LocalDateTime getDthorasolicitacao() {
        return dthorasolicitacao;
    }

    public void setDthorasolicitacao(LocalDateTime dthorasolicitacao) {
        this.dthorasolicitacao = dthorasolicitacao;
    }
}
