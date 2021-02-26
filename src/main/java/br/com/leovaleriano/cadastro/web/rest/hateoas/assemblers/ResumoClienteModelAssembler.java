package br.com.leovaleriano.cadastro.web.rest.hateoas.assemblers;

import br.com.leovaleriano.cadastro.domain.dto.ClienteDTO;
import br.com.leovaleriano.cadastro.web.rest.ClienteController;
import br.com.leovaleriano.cadastro.web.rest.hateoas.model.ResumoClienteModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class ResumoClienteModelAssembler extends RepresentationModelAssemblerSupport<ClienteDTO, ResumoClienteModel> {

    public ResumoClienteModelAssembler() {
        super(ClienteController.class, ResumoClienteModel.class);
    }

    @Override
    public ResumoClienteModel toModel(ClienteDTO entity) {
        ResumoClienteModel clienteDTOModel = instantiateModel(entity);

        clienteDTOModel.add(linkTo(methodOn(ClienteController.class).buscarPorId(entity.getId())).withSelfRel());

        clienteDTOModel.setId(entity.getId());
        clienteDTOModel.setNome(entity.getNome());
        clienteDTOModel.setEmail(entity.getEmail());
        clienteDTOModel.setSexo(entity.getSexo());
        clienteDTOModel.setNascimento(entity.getNascimento());
        return clienteDTOModel;
    }

    @Override
    public CollectionModel<ResumoClienteModel> toCollectionModel(Iterable<? extends ClienteDTO> clientes) {
        CollectionModel<ResumoClienteModel> clientesModels = super.toCollectionModel(clientes);
        clientesModels.add(linkTo(methodOn(ClienteController.class).listarResumosClientes(null, null)).withSelfRel());
        return clientesModels;
    }
}
