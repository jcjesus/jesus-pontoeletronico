package br.com.akme.api.pontoeletronico.repositories;

import br.com.akme.api.pontoeletronico.entities.Empresa;
import br.com.akme.api.pontoeletronico.entities.Funcionario;
import br.com.akme.api.pontoeletronico.enums.PerfilEnum;
import br.com.akme.api.pontoeletronico.utils.PasswordUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioRepositoryTest {

    public static final String FUNALO_DE_TAL = "Funalo de Tal";
    public static final String SENHA = "123456";
    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    public static final String EMAIL = "email@email.com";
    public static final String CPF = "24291173474";

    @Before
    public void setUp() throws Exception {
        Empresa empresa = this.empresaRepository.save(this.obterDadosEmpresa());
        this.funcionarioRepository.save(obterDadosFuncionario(empresa));
    }

    @After
    public void tearDown() {
        this.funcionarioRepository.deleteAll();
    }

    @Test
    public void testBuscarFuncionarioPorEmail() {
        Funcionario funcionario = this.funcionarioRepository.findByEmail(EMAIL);

        assertEquals(EMAIL, funcionario.getEmail());
    }

    @Test
    public void testBuscarFuncionarioPorCPF() {
        Funcionario funcionario = this.funcionarioRepository.findByCpf(CPF);

        assertEquals(CPF, funcionario.getCpf());
    }

    @Test
    public void testBuscarFuncionarioPorEmailECpf() {
        Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(CPF,EMAIL);

        assertNotNull(funcionario);
    }

    @Test
    public void testBuscarFuncionarioPorEmailOuCpfParaEmailInvalido() {
        Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail(CPF, "email@invalido.com");

        assertNotNull(funcionario);
    }

    @Test
    public void testBuscarFuncionarioPorEmailOuCpfParaCpfInvalido() {
        Funcionario funcionario = this.funcionarioRepository.findByCpfOrEmail("4123412341234", EMAIL);

        assertNotNull(funcionario);
    }

    private Funcionario obterDadosFuncionario(Empresa empresa){
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(FUNALO_DE_TAL);
        funcionario.setPerfil(PerfilEnum.ROLE_USER);
        funcionario.setSenha(PasswordUtils.gerarBCrypt(SENHA));
        funcionario.setCpf(CPF);
        funcionario.setEmail(EMAIL);
        funcionario.setEmpresa(empresa);
        return funcionario;
    }

    private Empresa obterDadosEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial(EmpresaRepositoryTest.EMPRESA_DE_EXEMPLO);
        empresa.setCnpj(EmpresaRepositoryTest.CNPJ);
        return empresa;
    }
}
