package br.com.conexao.mdfe.api.model.certificado;

import br.com.conexao.mdfe.api.model.tenancy.TenancyFilter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Certificado extends TenancyFilter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idcertificado;

    @NotNull
    @Size(max = 100)
    private String senha;

    @Lob
    @Column(name = "arquivo")
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] arquivo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dtiniciovalidade;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dtfimvalidade;
}
