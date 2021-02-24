package br.com.leovaleriano.cadastro.domain.dto;

import br.com.leovaleriano.cadastro.domain.Sexo;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ClienteDTO {
    private Integer id;
    private String nome;
    private String email;
    private LocalDate nascimento;
    private Sexo sexo;
}
