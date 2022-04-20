package br.com.conexao.mdfe.api.model.empresa;

import com.fincatto.documentofiscal.DFAmbiente;
import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.mdfe3.classes.def.MDFTipoEmissao;
import com.fincatto.documentofiscal.mdfe3.classes.def.MDFTipoTranportador;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idempresa;

    private String cnpj;

    private String inscricaoestadual;

    @NotBlank
    private String razaosocial;

    @NotBlank
    private String fantasia;

    @Email
    @Size(max = 200)
    private String email;

    private Boolean inscricaoisenta;

    @Enumerated(value = EnumType.STRING)
    private DFAmbiente tipoambiente;

    @Enumerated(value = EnumType.STRING)
    private MDFTipoEmissao tpemis;

    private Long rntrc;

    @Enumerated(value = EnumType.STRING)
    private TipoEmitente tipoemitente;

    @Enumerated(value = EnumType.STRING)
    private MDFTipoTranportador tpTransp;

    @NotBlank
    private String pais;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private DFUnidadeFederativa uf;

    @NotBlank
    private String logradouro;

    private String complemento;

    private Long cep;

    @NotBlank
    private String municipio;

    @NotNull
    private Long codigomunicipio;

    private String numero;

    private String bairro;

    private String observacao;

    private Boolean flagcanalverde;

    private Boolean flagdel=false;

    private String fone;
}
