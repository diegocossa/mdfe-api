package br.com.conexao.mdfe.api.model.seguradora;

import com.fincatto.documentofiscal.mdfe3.classes.def.MDFTipoResponsavelSeguro;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
public class Seguradora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idseguradora")
    private Long idSeguradora;

    @Size(min = 11, max = 14)
    private String cpfcnpjresponsavel;

    /*
    @OneToMany
    @JoinTable(name = "seguradora_empresa",
            joinColumns = @JoinColumn(name = "idseguradora"), inverseJoinColumns = @JoinColumn(name = "idempresa"))
    private List<Empresa> empresasRelacionadas;
    */

    @NotBlank
    private String nomeseguradora;

    private String numeroapolice;

    @NotNull
    private MDFTipoResponsavelSeguro responsavel;

    @Size(min = 14, max = 14)
    private String cnpjseguradora;

    private Boolean flagdel=false;

}
