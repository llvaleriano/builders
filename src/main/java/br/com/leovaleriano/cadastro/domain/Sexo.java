package br.com.leovaleriano.cadastro.domain;

public enum Sexo {

    MASC("Masculino"),
    FEM("Feminino");

    public final String descricao;

    private Sexo(String descricao) {
        this.descricao = descricao;
    }

}
