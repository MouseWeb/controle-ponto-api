package br.com.mouseweb.api;

import br.com.mouseweb.api.entities.Empresa;
import br.com.mouseweb.api.entities.Funcionario;
import br.com.mouseweb.api.entities.Lancamento;
import br.com.mouseweb.api.enums.PerfilEnum;
import br.com.mouseweb.api.enums.TipoEnum;
import br.com.mouseweb.api.repositories.FuncionarioRepository;
import br.com.mouseweb.api.repositories.LancamentoRepository;
import br.com.mouseweb.api.repositories.EmpresaRepository;
import br.com.mouseweb.api.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Component;

import java.util.Date;

@SpringBootApplication
@EnableCaching
public class PontoInteligenteApplication {

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private LancamentoRepository lancamentoRepository;

	public static void main(String[] args) {
		SpringApplication.run(PontoInteligenteApplication.class, args);
	}

	@Component
	public class CommandLineAppStartupRunner implements CommandLineRunner {

		@Override
		public void run(String...args) throws Exception {
			Empresa empresa = new Empresa();
			empresa.setRazaoSocial("Empresa Teste");
			empresa.setCnpj("36763168000104");
			empresaRepository.save(empresa);

			Funcionario funcionarioAdmin = new Funcionario();
			funcionarioAdmin.setCpf("25164061422");
			funcionarioAdmin.setEmail("admin@empresa.com");
			funcionarioAdmin.setNome("Administrador");
			funcionarioAdmin.setPerfil(PerfilEnum.ROLE_ADMIN);
			funcionarioAdmin.setSenha(PasswordUtils.gerarBCrypt("123456"));
			funcionarioAdmin.setEmpresa(empresa);
			funcionarioRepository.save(funcionarioAdmin);

			Funcionario funcionario = new Funcionario();
			funcionario.setCpf("09943636211");
			funcionario.setEmail("funcionario@empresa.com");
			funcionario.setNome("Funcionário");
			funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
			funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
			funcionario.setEmpresa(empresa);
			funcionarioRepository.save(funcionario);

			empresaRepository.findAll().forEach(System.out::println);
			//funcionarioRepository.findAll().forEach(System.out::println);
			funcionarioRepository.findByEmpresaId(empresa.getId()).forEach(System.out::println);

			gerarLancamentos(funcionario, 20);
		}
	}

	private void gerarLancamentos(Funcionario funcionario, int numLancamentos) {
		int tipoPos = 0;
		TipoEnum[] tipos = TipoEnum.values();

		Lancamento lancamento;
		for (int i=0; i<numLancamentos; i++) {
			lancamento = new Lancamento();
			lancamento.setData(new Date());
			lancamento.setTipo(tipos[tipoPos++]);
			lancamento.setLocalizacao("53.4546692,-2.2221622");
			lancamento.setFuncionario(funcionario);
			lancamentoRepository.save(lancamento);
			if (tipoPos == tipos.length) {
				tipoPos = 0;
			}
		}
	}

}
