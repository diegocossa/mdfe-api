package br.com.conexao.mdfe.api.model.licenca;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Mensalidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idmensalidade;

    @NotNull
    @NumberFormat(pattern = "#,##0.00")
    private BigDecimal valor;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dtcadastro;

    @PrePersist
    private void prePersist(){
        setDtcadastro(LocalDate.now());
    }
}
