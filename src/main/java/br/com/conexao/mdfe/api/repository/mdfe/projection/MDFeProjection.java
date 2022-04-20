package br.com.conexao.mdfe.api.repository.mdfe.projection;

import br.com.conexao.mdfe.api.model.mdfe.SituacaoMDFe;
import br.com.conexao.mdfe.api.model.veiculo.Veiculo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MDFeProjection {

    Long idmdfe;

    String retornorecibo;
    String nmdf;
    Integer serie;
    String chave;

    SituacaoMDFe situacao;

    Veiculo veiculotracao;

    public MDFeProjection(Long idmdfe, String retornorecibo, String nmdf, Integer serie, Veiculo veiculotracao, SituacaoMDFe situacao, String chave) {
        this.idmdfe = idmdfe;
        this.retornorecibo = retornorecibo;
        this.nmdf = nmdf;
        this.serie = serie;
        this.veiculotracao = veiculotracao;
        this.situacao = situacao;
        this.chave = chave;
    }
}
