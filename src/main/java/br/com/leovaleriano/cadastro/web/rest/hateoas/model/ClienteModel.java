package br.com.leovaleriano.cadastro.web.rest.hateoas.model;

import br.com.leovaleriano.cadastro.domain.Cliente;
import br.com.leovaleriano.cadastro.domain.Endereco;
import br.com.leovaleriano.cadastro.domain.Sexo;
import br.com.leovaleriano.cadastro.domain.Telefone;
import br.com.leovaleriano.cadastro.web.rest.ClienteController;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Classe que representa um resource rest. retornado para o cliente do serviço.
 * Empacota um cliente e adiciona os links do hateoas
 */
@Getter
public class ClienteModel extends RepresentationModel<ClienteModel> {

    private Integer id;
    private String nome;
    private String email;
    private LocalDate nascimento;
    private Integer idade;
    private Sexo sexo;
    private Endereco endereco;
    private Telefone telefone;

    public ClienteModel(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
        this.nascimento = cliente.getNascimento();
        this.idade = cliente.getIdade();
        this.sexo = cliente.getSexo();
        this.endereco = cliente.getEndereco();
        this.telefone = cliente.getTelefone();

        this.add(linkTo(ClienteController.class)
                .slash(cliente.getId())
                .withSelfRel());
        this.add(linkTo(methodOn(ClienteController.class)
                .alterar(cliente.getId(), cliente))
                .withRel("alterar"));
        this.add(linkTo(methodOn(ClienteController.class)
                .alterarTelefone(cliente.getId(), cliente.getTelefone()))
                .withRel("alterar-telefone"));
    }

}
