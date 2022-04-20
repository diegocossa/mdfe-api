package br.com.conexao.mdfe.api.repository.mdfe.filter;

import br.com.conexao.mdfe.api.model.mdfe.SituacaoMDFe;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class MdfeTotalizadorFilter {

    public MdfeTotalizadorFilter(Long total, SituacaoMDFe situacaomdfe) {
        this.total = total;
        this.situacaomdfe = situacaomdfe;
    }

    @Column(name = "total")
    private Long total;

    @Column(name = "situacaomdfe")
    private SituacaoMDFe situacaomdfe;

}
