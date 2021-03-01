package br.com.leovaleriano.cadastro.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(min = 5, max = 100)
    private String nome;

    @NotNull
    @Email(message = "Informe um e-mail válido")
    private String email;

    @NotNull
    @Past(message = "Não pode ser uma data futura")
    private LocalDate nascimento;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @Embedded
    private Endereco endereco;

    @Embedded
    private Telefone telefone;

    public Integer getIdade() {
        return Period.between(nascimento, LocalDate.now()).getYears();
    }
}
