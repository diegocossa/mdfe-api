package br.com.conexao.mdfe.api.model.mdfe;

import lombok.Getter;

@Getter
public enum SituacaoMDFe {
    GRAVADO(0, "Gravado"),
    PROCESSAMENTO(1, "Processamento"),
    AUTORIZADO(2, "Autorizado"),
    REJEITADO(3, "Rejeitado"),
    ENCERRADO(4, "Encerrado"),
    CANCELADO(5, "Cancelado");

    private final Integer id;
    private final String descricao;

    SituacaoMDFe(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }
}
