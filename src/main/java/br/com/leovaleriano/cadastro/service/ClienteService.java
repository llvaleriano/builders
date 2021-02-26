package br.com.leovaleriano.cadastro.service;

import br.com.leovaleriano.cadastro.domain.Cliente;
import br.com.leovaleriano.cadastro.domain.Telefone;
import br.com.leovaleriano.cadastro.domain.dto.ClienteDTO;
import br.com.leovaleriano.cadastro.repository.ClienteRepository;
import br.com.leovaleriano.cadastro.repository.SearchCriteria;
import br.com.leovaleriano.cadastro.repository.ClienteSpecification;
import br.com.leovaleriano.cadastro.service.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@AllArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public Cliente buscarClientePorId(Integer id) {
        return clienteRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Cliente.class, "id", id.toString()));
    }

    public Page<ClienteDTO> listarTodos(int page, int size) {
        Pageable pagina = PageRequest.of(page, size);
        return clienteRepository.listarTodos(pagina);
    }

    public List<Cliente> pesquisarPorCriterios(List<SearchCriteria> criterios) {
        if (criterios == null || criterios.size() == 0) {
            return clienteRepository.findAll();
        }

        Specification<Cliente> condicoes = where(new ClienteSpecification(criterios.get(0)));

        for (int i = 1; i < criterios.size(); i++) {
            condicoes = Specification.where(condicoes).and(new ClienteSpecification(criterios.get(i)));
        }

        List<Cliente> clientes = clienteRepository.findAll(condicoes);
        if (clientes.size() == 0) {
            throw new EntityNotFoundException(Cliente.class);
        }
        return clientes;
    }

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void alterarTelefone(Telefone telefone, Integer idCliente) {
        clienteRepository.alterarTelefone(telefone.getDdd(), telefone.getNumeroTelefone(), idCliente);
    }
}
