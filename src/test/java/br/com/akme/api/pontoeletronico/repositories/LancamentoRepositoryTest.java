package br.com.akme.api.pontoeletronico.repositories;

import br.com.akme.api.pontoeletronico.entities.Empresa;
import br.com.akme.api.pontoeletronico.entities.Funcionario;
import br.com.akme.api.pontoeletronico.entities.Lancamento;
import br.com.akme.api.pontoeletronico.enums.PerfilEnum;
import br.com.akme.api.pontoeletronico.enums.TipoLancamentoEnum;
import br.com.akme.api.pontoeletronico.utils.PasswordUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.data.domain.PageRequest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {

    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private FuncionarioRepository funcionarioRepository;
    @Autowired
    private LancamentoRepository lancamentoRepository;

    private Long funcionarioId;

    @Before
    public void setUp() {
        Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());

        Funcionario funcionario = this.funcionarioRepository.save(obterDadosFuncionario(empresa));
        this.funcionarioId = funcionario.getId();

        this.lancamentoRepository.save(obterDadosLancamento(funcionario));
        this.lancamentoRepository.save(obterDadosLancamento(funcionario));

    }

    @After
    public void tearDown() throws Exception {
        this.empresaRepository.deleteAll();
    }

    @Test
    public void testBuscarLancamentoPorFuncionarioId() {
        List<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId);

        assertEquals(2,lancamentos.size());
    }

    @Test
    public void testBuscarLancamentosPorFuncionarioIdPaginado() {
        PageRequest page = new PageRequest(0, 10);
        Page<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId,page);

        assertEquals(2, lancamentos.getTotalElements());
    }

    private Lancamento obterDadosLancamento(Funcionario funcionario) {
        Lancamento lancamento = new Lancamento();
        lancamento.setData(new Date());
        lancamento.setTipoLancamentoEnum(TipoLancamentoEnum.START_LAUNCH);
        lancamento.setFuncionario(funcionario);
        return lancamento;
    }

    private Funcionario obterDadosFuncionario(Empresa empresa) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(FuncionarioRepositoryTest.FUNALO_DE_TAL);
        funcionario.setPerfil(PerfilEnum.ROLE_USER);
        funcionario.setSenha(PasswordUtils.gerarBCrypt(FuncionarioRepositoryTest.SENHA));
        funcionario.setCpf(FuncionarioRepositoryTest.CPF);
        funcionario.setEmail(FuncionarioRepositoryTest.EMAIL);
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
