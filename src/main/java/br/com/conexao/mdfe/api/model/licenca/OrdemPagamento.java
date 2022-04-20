package br.com.conexao.mdfe.api.model.licenca;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrdemPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idordempagamento;

    @NotNull
    @Size(max = 60)
    private String identificadortransacao;//código gerado no moip ou no picpay

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dtemissao;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoPagamentoEnum tipopagamento;

    @NotNull
    @Enumerated(EnumType.STRING)
    private IntegradoraEnum integradora;

    @Size(max = 100)
    private String tokenboletofacil;

    //Usado para gravar o codigo da cobrança dos sites de integracao (Boleto Facil)
    @Size(max = 35)
    private String identificadorexterno;

    private String codigobarras;

    @NotNull
    private String orderstatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "ordem_pagamento_mdfe_recebimento",
            joinColumns = {@JoinColumn(name = "idordempagamento", referencedColumnName = "idordempagamento", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "idmdferecebimento", referencedColumnName = "idmdferecebimento")})
    private MdfeRecebimento mdfeRecebimento;

    @NotNull
    @Size(max = 200)
    private String url;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dtvencimento;

    private boolean flagativo = true;

    @JsonIgnore
    public MdfeRecebimento getMdfeRecebimento() {
        return mdfeRecebimento;
    }

    public void setMdfeRecebimento(MdfeRecebimento mdfeRecebimento) {
        this.mdfeRecebimento = mdfeRecebimento;
    }
}
