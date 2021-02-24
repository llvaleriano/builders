package br.com.leovaleriano.cadastro.web.rest.model;

import br.com.leovaleriano.cadastro.domain.Sexo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;
import java.time.Period;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Relation(collectionRelation = "clientes", itemRelation = "cliente")
@JsonInclude(Include.NON_NULL)
public class ClienteDTOModel extends RepresentationModel<ClienteDTOModel> {

    private Integer id;
    private String nome;
    private String email;
    private LocalDate nascimento;
    private Sexo sexo;

    public Integer getIdade() {
        return Period.between(nascimento, LocalDate.now()).getYears();
    }

}
