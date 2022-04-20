package br.com.conexao.mdfe.api.model.empresa;

public enum TipoEmitente {
    PRESTADOR_SERVICO(1),
    CARGA_PROPRIA(2);

    private Integer codigo;

    TipoEmitente(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        return codigo;
    }
}
