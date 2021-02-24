package br.com.leovaleriano.cadastro.web.rest.resources;

import br.com.leovaleriano.cadastro.domain.Cliente;
import br.com.leovaleriano.cadastro.web.rest.ClienteController;
import lombok.Getter;
import org.springframework.hateoas.Link;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Classe que representa um resource retornado para o cliente do serviço.
 * Empacota um cliente e adiciona os links do hateoas
 */
@Getter
public class ClienteResource  {

    private Cliente cliente;
    private List<Link> links;

    public ClienteResource(Cliente cliente) {
        this.cliente = cliente;
        links = new ArrayList<>();
        links.add(linkTo(ClienteController.class)
                .slash(cliente.getId())
                .withSelfRel());
        links.add(linkTo(methodOn(ClienteController.class)
                .alterar(cliente.getId(), cliente))
                .withRel("alterar"));
        links.add(linkTo(methodOn(ClienteController.class)
                .alterarTelefone(cliente.getId(), cliente.getTelefone()))
                .withRel("alterar-telefone"));
    }
}
