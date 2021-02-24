package br.com.leovaleriano.cadastro.web.rest;

import br.com.leovaleriano.cadastro.domain.Cliente;
import br.com.leovaleriano.cadastro.domain.Telefone;
import br.com.leovaleriano.cadastro.domain.dto.ClienteDTO;
import br.com.leovaleriano.cadastro.service.ClienteService;
import br.com.leovaleriano.cadastro.service.exception.EntityNotFoundException;
import br.com.leovaleriano.cadastro.web.rest.hateoas.assemblers.ClienteModelAssembler;
import br.com.leovaleriano.cadastro.web.rest.model.ClienteDTOModel;
import br.com.leovaleriano.cadastro.web.rest.resources.ClienteResource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteModelAssembler clienteModelAssembler;
    private final PagedResourcesAssembler<ClienteDTO> pagedResourcesAssembler;

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResource> buscar(@PathVariable Integer id) throws EntityNotFoundException {
        Cliente cliente = clienteService.buscarClientePorId(id);
        return ResponseEntity.ok(new ClienteResource(cliente));
    }

    @GetMapping()
    public ResponseEntity<PagedModel<ClienteDTOModel>> listarTodosResumido() {
        return listarTodosResumido(0, 10);
    }

    /**
     * Para habilitar a criação de links de navegação nos dados paginados de forma automática, o endpoint retorna um
     * PagedModel de ClienteModel.
     * A classe ClienteDTOModel representa um cliente e possibilita a adição dos links para cada resource
     * @param page
     * @param size
     * @return
     */
    @GetMapping(params = {"page", "size"})
    public ResponseEntity<PagedModel<ClienteDTOModel>> listarTodosResumido(@RequestParam Integer page, @RequestParam Integer size) {
        Page<ClienteDTO> pagina = clienteService.listarTodos(page, size);

        if (page > pagina.getTotalPages()) {
            throw new EntityNotFoundException(Cliente.class, null);
        }

        PagedModel<ClienteDTOModel> clienteDTOModels = pagedResourcesAssembler.toModel(pagina, clienteModelAssembler);

        return new ResponseEntity<>(clienteDTOModels, HttpStatus.OK);
    }


    @GetMapping(params = {"nome", "email", "cidade"})
    public List<Cliente> pesquisarPorEspecificacao(@RequestParam String nome, @RequestParam String email, @RequestParam String cidade) {
        return clienteService.pesquisarPorEspecificacao(nome, email, cidade);
    }


    @PostMapping
    public ResponseEntity<ClienteResource> salvar(@RequestBody final Cliente cliente) throws ConstraintViolationException {
        clienteService.salvar(cliente);
        final URI uri = MvcUriComponentsBuilder.fromController(getClass())
                .path("/{id}")
                .buildAndExpand(cliente.getId())
                .toUri();
        return ResponseEntity.created(uri).body(new ClienteResource(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResource> alterar(@PathVariable Integer id, @RequestBody final Cliente cliente) {
        cliente.setId(id);
        clienteService.salvar(cliente);
        return ResponseEntity.ok(new ClienteResource(cliente));
    }

    @PatchMapping("/{idCliente}/telefone")
    public ResponseEntity<ClienteResource> alterarTelefone(@PathVariable Integer idCliente, @RequestBody Telefone telefone) {
        clienteService.alterarTelefone(telefone, idCliente);
        Cliente cliente = clienteService.buscarClientePorId(idCliente);
        return ResponseEntity.ok(new ClienteResource(cliente));
    }
}
