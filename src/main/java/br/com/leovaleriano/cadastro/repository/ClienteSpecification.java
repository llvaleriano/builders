package br.com.leovaleriano.cadastro.repository;

import br.com.leovaleriano.cadastro.domain.Cliente;
import br.com.leovaleriano.cadastro.domain.Cliente_;
import br.com.leovaleriano.cadastro.domain.Endereco;
import br.com.leovaleriano.cadastro.domain.Endereco_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class ClienteSpecification{

    public static Specification<Cliente> nome(String nome) {
        return (root, criteriaQuery, criteriaBuilder) ->
                StringUtils.hasLength(nome) ?
                    criteriaBuilder.like(root.get(Cliente_.nome), "%" + nome + "%") :
                    criteriaBuilder.conjunction();
    }

    public static Specification<Cliente> email(String email) {
        return (root, criteriaQuery, criteriaBuilder) ->
                StringUtils.hasLength(email) ?
                    criteriaBuilder.like(root.get(Cliente_.email), "%" + email + "%") :
                    criteriaBuilder.conjunction();
    }

    public static Specification<Endereco> cidade(String cidade) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(Endereco_.cidade), cidade);
    }
}
