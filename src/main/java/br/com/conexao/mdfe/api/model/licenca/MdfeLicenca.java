package br.com.conexao.mdfe.api.model.licenca;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class MdfeLicenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idmdfelicenca;

    @OneToOne
    @JoinColumn(name = "idmdferecebimento")
    private MdfeRecebimento mdferecebimento;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dtinicio;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime dtfim;
}
