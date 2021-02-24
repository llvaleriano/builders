package br.com.leovaleriano.cadastro.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class Telefone {

    private String ddd;
    private String numeroTelefone;

}
