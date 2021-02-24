package br.com.leovaleriano.cadastro.web.rest.apierror;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ApiValidationError extends ApiSubError {
    private String objeto;
    private String atributo;
    private Object valorInvalido;
    private String mensagem;

    ApiValidationError(String objeto, String mensagem) {
        this.objeto = objeto;
        this.mensagem = mensagem;
    }
}