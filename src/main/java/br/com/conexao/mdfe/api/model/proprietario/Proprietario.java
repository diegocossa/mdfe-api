package br.com.conexao.mdfe.api.model.proprietario;

import com.fincatto.documentofiscal.DFUnidadeFederativa;
import com.fincatto.documentofiscal.mdfe3.classes.def.MDFTipoProprietario;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
public class Proprietario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idproprietario;

    @Size(min = 11, max = 14)
    private String cpfcnpj;

    private String rntrc;

    private String nome;

    private String ie;

    @Enumerated(value = EnumType.STRING)
    private DFUnidadeFederativa uf;

    @Enumerated(value = EnumType.STRING)
    private MDFTipoProprietario tpProp;

    private Boolean flagdel=false;
}
