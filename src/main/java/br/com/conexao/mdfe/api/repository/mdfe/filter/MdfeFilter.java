package br.com.conexao.mdfe.api.repository.mdfe.filter;

import br.com.conexao.mdfe.api.model.mdfe.SituacaoMDFe;
import com.fincatto.documentofiscal.DFUnidadeFederativa;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MdfeFilter {

    private SituacaoMDFe situacao;

    private String nmdf;
    private String chave;
    private String numprotocolo;
    private String serie;

    private DFUnidadeFederativa ufini;
    private DFUnidadeFederativa uffim;

    private String placa;

    private String ambienteenvio;

}
