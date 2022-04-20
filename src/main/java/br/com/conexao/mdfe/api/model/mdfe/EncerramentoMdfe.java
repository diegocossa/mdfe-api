package br.com.conexao.mdfe.api.model.mdfe;

import com.fincatto.documentofiscal.DFUnidadeFederativa;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class EncerramentoMdfe {
    private String chave;
    private String numprotocolo;

    @NotBlank
    private String codigomunicipio;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataencerramento = LocalDate.now();

    @NotNull
    @Enumerated(EnumType.STRING)
    private DFUnidadeFederativa uf;
}
