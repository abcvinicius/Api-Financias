package com.vinicius.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.vinicius.minhasfinancas.model.entity.Usuario;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE) // <- ela está dentro da anotacao DataJpaTest, e tem por objetivo de
													// configurar, criar, adicionar, gerar tabelas, intancias dentro do
													// banco em mémora. Como a gentr não quer isso simplesmente a gente
													// chama esse anotação para poder escificar ela e dizer que "None"
													// nehuma alteração a gente quer que sejá feita, até pq lá no h2 o
													// banco já está configurado
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	TestEntityManager entityManager; // <- dentro do repository existe entityManager, mas a gente está simplelsmente
										// chamando entityManager porque isso tudo aqui é um teste então a gente não
										// quer usar diretamente a Entidade Repository, mas sim só testar as nossas
										// funções, e para isso vamos especificar a entityManager.
										// tudo oq houver usuarioRepository, a gente vai sustituir por entityManager.

										// Deixar claro que esse entityManager não é o ORIGINAL, mas sim é um feito para
										// Test, até porque ele vem de dentro de dataTESTjpa, então já entedemos que ele
										// é feito somente para teste.
	
	
	
	
	//--------------------------------------------------------------------------------------------------------------------//
	
	
	
	// Esse Método aqui é feito para verificar um Email:

	// CENARIO -> ESTAMOS CRIANDO E SALVANDO UM USUARIO DENTRO DO BANCO

	// ACAO -> ESTAMOS USANDO .existByEmail PARA SABER SE ESSE USUARIO EXISTE DENTRO
	// DO BANCO

	// VERIFICAÇÃO -> ESTAMOS DIZENDO QUE: SE CASO EXISTA ESSE EMAIL DENTRO DO BANCO
	// RETORNE TRUE
	
	@Test
	public void deveVerificarExistenciaDeUmEmail() {
		// cenario
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);  // <--- substituição por entityManagar pq nao queremos usar diretamente a
										//       	entidade de Repository -> usuarioRepository.save(usuario);

		// acao
		boolean result = usuarioRepository.existsByEmail("usuario@email.com");

		// verificacao
		Assertions.assertThat(result).isTrue();
	}

	
	// Esse Método aqui Retorna FALSE caso não exista tal email cadastrado dentro do
	// Banco:

	// CENARIO -> ESTÁ DELETANDO TODOS OS USUARIO CASDASTRADOS

	// ACAO -> ESTÁ PERGUNTANDO SE EXISTE ALGUM EMAIL COMO OQUE FOI PASSADO, DENTRO
	// DO BANCO

	// VERIFICACAO -> ESTÁ DIZENDO: CASO NÃO EXISTA ESSE EMAIL DENTRO DO BANCO
	// RETORNE FALSE
	
	
	
	//----------------------------------------------------------------------------------------//
	
	
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		// cenario
		//usuarioRepository.deleteAll();
		
		 // """usuarioRepository.deleteAll();""" Está deletado pq simplesmente a gente não precisa utiliza-lo,
		// 1° - porque não queremos ligação direta com o repository através dos teste
		// 2° - porque ele não existe mais função para ele, como estamos utilizando """entityManager""", então sempre que demos 
		// um Relauncher a tudo oq está no banco em mémoria será apagado. <- então ele não tem mais função
		
		
		// """usuarioRepository.deleteAll();"""  <- isso aqui está simplesmente deletando tudo q existe dentro do
		// Repository(Banco) para garantir que não exite nada dentro de usuario

		// acao
		boolean result = usuarioRepository.existsByEmail("usuario@email.com");   // <- aqui estamos mandando ele procurarse existe 
																				//     algum email como esse cadastrado no Banco

		// verificacao
		Assertions.assertThat(result).isFalse(); // <- aqui vamos procurar se existe algum Email cadastrado na base, mas
													// é obv que não existe, então logicamente ele tem que retornar falso
	}
	
	
	
	//----------------------------------------------------------------------------------------//

	
	
	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		//cenario
		Usuario usuario = criarUsuario();
		
		//acao
		Usuario usuarioSalvo = usuarioRepository.save(usuario);
		
		//verificacao	
		Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		//cenario
		Usuario usuario = criarUsuario();
		entityManager.persist(usuario);
		
		//verificacao
		Optional<Usuario> result = usuarioRepository.findByEmail("usuario@email.com");
		
		Assertions.assertThat(result.isPresent()).isTrue();//<- verifica se o "result" esta "is.Present"(presente), se sim "is.True"
	}
	
	
	
	@Test
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
		//cenario
		
		//verificacao
		Optional<Usuario> result = usuarioRepository.findByEmail("usuario@email.com");
		
		Assertions.assertThat(result.isPresent()).isFalse();//<- verifica se o "result" esta "is.Present"(presente), se não "is.False"
	}
	
	
	
	public static Usuario criarUsuario() {
		return Usuario
				.builder()
				.nome("usuario")
				.email("usuario@email.com")
				.senha("senha")
				.build();
	}	
}
