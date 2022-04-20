package br.com.conexao.mdfe.api.model.licenca;

import br.com.conexao.mdfe.api.model.pessoa.Pessoa;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MdfeRecebimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idmdferecebimento;

    @OneToOne
    @JoinColumn(name = "idmensalidade")
    private Mensalidade mensalidade;

    @OneToOne
    @JoinColumn(name = "idpessoa")
    private Pessoa pessoa;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DiasLicenca qtddiasdelicenca;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dtrecebimento;

    @NotNull
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal valor;

    private Boolean flaggerounota = false;

    private String chavenfse;

    /*@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "ordem_pagamento_mdfe_recebimento",
            joinColumns = {@JoinColumn(name = "idmdferecebimento", referencedColumnName = "idmdferecebimento")},
            inverseJoinColumns = {@JoinColumn(name = "idordempagamento", referencedColumnName = "idordempagamento")})
    private OrdemPagamento ordenspagamento;*/

    @PrePersist
    private void prePersist() {
        setDtrecebimento(LocalDateTime.now());
    }

    /*@JsonIgnore
    public OrdemPagamento getOrdenspagamento() {
        return ordenspagamento;
    }

    public void setOrdenspagamento(OrdemPagamento ordenspagamento) {
        this.ordenspagamento = ordenspagamento;
    }*/
}