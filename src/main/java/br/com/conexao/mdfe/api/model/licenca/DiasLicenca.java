package br.com.conexao.mdfe.api.model.licenca;

public enum DiasLicenca {

    MES(1),
    SEISMESES(6),
    DOZEMESES(12);

    private int value;

    DiasLicenca(int val) {
        this.value = val;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
