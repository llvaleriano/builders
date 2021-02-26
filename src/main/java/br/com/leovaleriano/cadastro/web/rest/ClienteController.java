package br.com.leovaleriano.cadastro.web.rest;

import br.com.leovaleriano.cadastro.domain.Cliente;
import br.com.leovaleriano.cadastro.domain.Telefone;
import br.com.leovaleriano.cadastro.domain.dto.ClienteDTO;
import br.com.leovaleriano.cadastro.repository.SearchCriteria;
import br.com.leovaleriano.cadastro.service.ClienteService;
import br.com.leovaleriano.cadastro.service.exception.EntityNotFoundException;
import br.com.leovaleriano.cadastro.web.rest.hateoas.assemblers.ResumoClienteModelAssembler;
import br.com.leovaleriano.cadastro.web.rest.hateoas.model.ResumoClienteModel;
import br.com.leovaleriano.cadastro.web.rest.hateoas.model.ClienteModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ResumoClienteModelAssembler resumoClienteModelAssembler;
    private final PagedResourcesAssembler<ClienteDTO> pagedResourcesAssembler;

    @Operation(summary = "Buscar cliente por id",
            description = "Pesquisa o cliente no BD usando o parâmetro id para pesquisar pela chave primária")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna um único cliente"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "500", description = "Retorna uma mensagem de erro detalhada"),
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClienteModel> buscarPorId(@PathVariable Integer id) throws EntityNotFoundException {
        Cliente cliente = clienteService.buscarClientePorId(id);
        return ResponseEntity.ok(new ClienteModel(cliente));
    }

    /**
     * Para habilitar a criação de links de navegação nos dados paginados de forma automática, o endpoint retorna um
     * PagedModel de ClienteModel.
     */
    @Operation(summary = "Listar todos",
            description = "Lista todos os clientes e apresenta o resumo de suas informações em páginas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma página de cadastro de clientes"),
            @ApiResponse(responseCode = "500", description = "Retorna uma mensagem de erro detalhada"),
    })
    @RequestMapping(method = RequestMethod.GET, params = {"page", "size"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<PagedModel<ResumoClienteModel>> listarResumosClientes(@RequestParam("page") Optional<Integer> page,
                                                                                @RequestParam("size") Optional<Integer> size) {
        Integer pageNumber = page.orElse(0);
        Integer pageSize = size.orElse(10);
        Page<ClienteDTO> pagina = clienteService.listarTodos(pageNumber, pageSize);

        if (pageNumber > pagina.getTotalPages()) {
            throw new EntityNotFoundException(Cliente.class, null);
        }

        PagedModel<ResumoClienteModel> clienteDTOModels = pagedResourcesAssembler.toModel(pagina, resumoClienteModelAssembler);

        return new ResponseEntity<>(clienteDTOModels, HttpStatus.OK);
    }

    @Operation(summary = "Pesquisar por parâmetros",
            description = "Recebe uma lista de parametros através do atributo 'search' e pesquisa os clientes que " +
                    "atendem aos critérios da pesquisa. Os parêmtros desem seguir o padrão [nome_paramtro]:[valor]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna uma página de cadastro de clientes"),
            @ApiResponse(responseCode = "404", description = "Pesquisa sem resultado"),
            @ApiResponse(responseCode = "500", description = "Retorna uma mensagem de erro detalhada"),
    })
    @RequestMapping(method = RequestMethod.GET, params = {"search"}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Cliente> pesquisarPorParametros(@RequestParam(value = "search") String search) {
        Pattern pattern = Pattern.compile("([\\w\\[.\\]\\\\]+)(:|>|<)([\\w\\[ \\]\\\\]+),");
        Matcher matcher = pattern.matcher(search + ",");
        List<SearchCriteria> criterios = new ArrayList<>();
        while (matcher.find()) {
            criterios.add(new SearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
        }

        return clienteService.pesquisarPorCriterios(criterios);
    }

    @Operation(summary = "Inserir cliente",
            description = "Recebe as informações do cliente e salva no banco de dados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "O cliente foi salvo"),
            @ApiResponse(responseCode = "500", description = "Retorna uma mensagem de erro detalhada"),
    })
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<ClienteModel> inserir(@RequestBody final Cliente cliente) throws ConstraintViolationException {
        clienteService.salvar(cliente);
        final URI uri = MvcUriComponentsBuilder.fromController(getClass())
                .path("/{id}")
                .buildAndExpand(cliente.getId())
                .toUri();
        return ResponseEntity.created(uri).body(new ClienteModel(cliente));
    }

    @Operation(summary = "Alterar cliente",
            description = "Altera todos os dados do cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20o", description = "O cliente alterado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Retorna uma mensagem de erro detalhada"),
    })
    @RequestMapping(name = "/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClienteModel> alterar(@PathVariable Integer id, @RequestBody final Cliente cliente) {
        cliente.setId(id);
        clienteService.salvar(cliente);
        return ResponseEntity.ok(new ClienteModel(cliente));
    }

    @Operation(summary = "Alterar telefone do cliente",
            description = "Altera apenas o telefone do cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Telefone alterado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Retorna uma mensagem de erro detalhada"),
    })
    @RequestMapping(name = "/{idCliente}/telefone", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClienteModel> alterarTelefone(@PathVariable Integer idCliente, @RequestBody Telefone telefone) {
        clienteService.alterarTelefone(telefone, idCliente);
        Cliente cliente = clienteService.buscarClientePorId(idCliente);
        return ResponseEntity.ok(new ClienteModel(cliente));
    }
}
