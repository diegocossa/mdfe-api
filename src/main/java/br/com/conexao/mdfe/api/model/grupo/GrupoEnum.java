package br.com.conexao.mdfe.api.model.grupo;

public enum GrupoEnum {

    G3BRASIL(1l),
    ADMINISTRADORES(2l),
    MOTORISTAS(3l),
    OPERADORES(4l);

    private Long codigo;

    GrupoEnum(Long codigo) {
        this.codigo = codigo;
    }

    public Long getCodigo() {
        return this.codigo;
    }
}
