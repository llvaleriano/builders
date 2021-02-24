package br.com.leovaleriano.cadastro.web.rest.hateoas.assemblers;

import br.com.leovaleriano.cadastro.domain.dto.ClienteDTO;
import br.com.leovaleriano.cadastro.web.rest.ClienteController;
import br.com.leovaleriano.cadastro.web.rest.model.ClienteDTOModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class ClienteModelAssembler extends RepresentationModelAssemblerSupport<ClienteDTO, ClienteDTOModel> {

    public ClienteModelAssembler() {
        super(ClienteController.class, ClienteDTOModel.class);
    }

    @Override
    public ClienteDTOModel toModel(ClienteDTO entity) {
        ClienteDTOModel clienteDTOModel = instantiateModel(entity);

        clienteDTOModel.add(linkTo(methodOn(ClienteController.class).buscar(entity.getId())).withSelfRel());

        clienteDTOModel.setId(entity.getId());
        clienteDTOModel.setNome(entity.getNome());
        clienteDTOModel.setEmail(entity.getEmail());
        clienteDTOModel.setSexo(entity.getSexo());
        clienteDTOModel.setNascimento(entity.getNascimento());
        return clienteDTOModel;
    }

    @Override
    public CollectionModel<ClienteDTOModel> toCollectionModel(Iterable<? extends ClienteDTO> entities) {
        CollectionModel<ClienteDTOModel> clientesModels = super.toCollectionModel(entities);
        clientesModels.add(linkTo(methodOn(ClienteController.class).listarTodosResumido(null, null)).withSelfRel());
        return clientesModels;
    }
}
