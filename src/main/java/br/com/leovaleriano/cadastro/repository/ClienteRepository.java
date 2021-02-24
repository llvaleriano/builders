package br.com.leovaleriano.cadastro.repository;

import br.com.leovaleriano.cadastro.domain.Cliente;
import br.com.leovaleriano.cadastro.domain.dto.ClienteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>, JpaSpecificationExecutor<Cliente> {

    @Modifying
    @Query("update Cliente c set c.telefone.ddd = :ddd, c.telefone.numeroTelefone = :numeroTelefone where c.id = :id")
    void alterarTelefone(@Param(value = "ddd") String ddd,
                         @Param(value = "numeroTelefone") String numeroTelefone,
                         @Param(value = "id") Integer id);

    @Query("select new br.com.leovaleriano.cadastro.domain.dto.ClienteDTO(c.id, c.nome, c.email, c.nascimento, c.sexo) from Cliente c ")
    Page<ClienteDTO> listarTodos(Pageable pageable);
}
