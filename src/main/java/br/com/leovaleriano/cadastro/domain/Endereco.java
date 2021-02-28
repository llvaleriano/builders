package br.com.leovaleriano.cadastro.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Endereco {

    private String logradouro;
    private String numeroEndereco;
    private String complemento;
    private String bairro;
    private String cidade;
    private UF uf;
    private String cep;

}
