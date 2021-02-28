package br.com.leovaleriano.cadastro.web.rest;

import br.com.leovaleriano.cadastro.domain.Cliente;
import br.com.leovaleriano.cadastro.domain.Endereco;
import br.com.leovaleriano.cadastro.domain.Sexo;
import br.com.leovaleriano.cadastro.domain.Telefone;
import br.com.leovaleriano.cadastro.domain.UF;
import br.com.leovaleriano.cadastro.service.ClienteService;
import br.com.leovaleriano.cadastro.service.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteController clienteController;

    @MockBean
    private ClienteService clienteService;

    @Test
    public void contextLoads() {
        assertThat(clienteController).isNotNull();
    }

    @Test
    public void buscarClienteComIdValidoDeveRetornarOk() throws Exception {
        Cliente cliente = mock(Cliente.class);
        when(cliente.getNome()).thenReturn("Cliente teste");
        when(clienteService.buscarClientePorId(1)).thenReturn(cliente);

        this.mockMvc.perform(get("/api/clientes/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Cliente teste")));
    }

    @Test
    public void buscarClientComIdInvalidoDeveRetornarErroTratado() throws Exception {
        when(clienteService.buscarClientePorId(0)).thenThrow(new EntityNotFoundException(Cliente.class, "id", "0"));

        this.mockMvc.perform(get("/api/clientes/0"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("NOT_FOUND")));

    }

    @Test
    public void inserirCliente() throws Exception {
        Cliente clienteMock = getClienteMock();
        String clienteJSon = getClienteJSon(clienteMock);

        when(clienteService.salvar(clienteMock)).thenReturn(clienteMock);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/clientes/")
                .accept(MediaType.APPLICATION_JSON).content(clienteJSon)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertNotNull(response.getHeader(HttpHeaders.LOCATION));
    }

    @Test
    public void alterarCliente() throws Exception {
        Cliente clienteMock = getClienteMock();
        String clienteJSon = getClienteJSon(clienteMock);

        when(clienteService.salvar(clienteMock)).thenReturn(clienteMock);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/clientes/1")
                .accept(MediaType.APPLICATION_JSON).content(clienteJSon)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }

    private Cliente getClienteMock() {
        Cliente clienteMock = new Cliente() ;
        clienteMock.setId(1);
        clienteMock.setNome("Cliente de teste");
        clienteMock.setEmail("cliente@mail.com");
        clienteMock.setNascimento(LocalDate.of(2000, 1, 1));
        clienteMock.setSexo(Sexo.MASC);

        Endereco endereco = new Endereco("Rua 1", "100", "Casa", "Centro", "Cocalzinho", UF.GO, "12345678");
        Telefone telefone = new Telefone("61", "5551234");
        clienteMock.setEndereco(endereco);
        clienteMock.setTelefone(telefone);
        return clienteMock;
    }

    private String getClienteJSon(Cliente cliente) {
        String clienteJSon = "{" +
                "\"nome\":\"" + cliente.getNome() + "\"," +
                "\"email\":\""+ cliente.getEmail() + "\"," +
                "\"nascimento\":\"" + cliente.getNascimento() + "\"," +
                "\"sexo\":\"" + cliente.getSexo().name() + "\"}";
        return clienteJSon;
    }


}
