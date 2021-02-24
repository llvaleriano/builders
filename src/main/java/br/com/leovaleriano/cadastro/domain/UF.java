package br.com.leovaleriano.cadastro.domain;

public enum UF {
    AC("Acre"),
    DF("Distrito Federal");

    public final String nome;

    private UF(String nome) {
        this.nome = nome;
    }

}
