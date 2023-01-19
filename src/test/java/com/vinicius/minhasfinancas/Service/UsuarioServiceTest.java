package com.vinicius.minhasfinancas.Service;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.vinicius.minhasfinancas.Service.UsuarioService;
import com.vinicius.minhasfinancas.Service.implementation.UsuarioServiceImplementation;
import com.vinicius.minhasfinancas.exception.ErroAutenticacao;
import com.vinicius.minhasfinancas.exception.RegraNegocioException;
import com.vinicius.minhasfinancas.model.entity.Usuario;
import com.vinicius.minhasfinancas.model.repository.UsuarioRepository;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {


	
	
	@Autowired
	@SpyBean 
	UsuarioServiceImplementation usuarioService;
	
	@Autowired
	@MockBean // <-- ele serve so para a gente poder criar uma instancia de usuarioRepositoryfake, doq simplesmente utilizar a instancia real
	UsuarioRepository usuarioRepository;

//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------//

	/* 
	 * 
	 * 
	 * 
	 * 					|-----------------------------------------------------------------------------------------------------------------------------------|
	 * 					| ESSA FORMA É A FORMA PADRÃO DE SE IMPLEMENTAR O MÉTODO {SPY}, 																	|
	 * 					| MAS COMO SABEMOS,O MÉTODO {SPY} PODE SER IMPLEMENTADO COM O CONTEXTO SPRING. QUE NEM O @MockBean dentro de UsuarioRepository	    |
	 * 					|-----------------------------------------------------------------------------------------------------------------------------------|
	 * 
	 * 
	 * 				    |-------------------------------------------------------------------------------------------------------------------------------------|
	 * 					|  																																	  |
	 *					|																																	  |
	 *					|@BeforeEach																														  |
	 *					|public void setUp() {																												  |
	 *					|																																	  |
	 *					|			|-----------------------------------------------------------------------------------------------------------|             |
	 *                  |           |   ESTAVA USANDO O {SPY} DA SEGUNTE FORMA:                                                                 |             |
	 *					|			|	usuarioSerice = Mockito.spy(UsuarioServiceImplementation.class);                                        |             |
	 *					|			|   *** Agora a gente vai utilizar o método {SPY} doq simplesmente utilizar á anotação @MockBean       ***  |             |
	 *				 	|			|   *** o {SPY} UTILIZA O MÉTODO (ORIGINAL) MAS NÃO O REAL, ENQUANTO A @MockBean utiliza uma instância ***  |             |															   
	 *					|			|-----------------------------------------------------------------------------------------------------------|             |
	 *					|																																	  |
	 *					|			|-------------------------------------------------------------------------------------------------------------------|     |			
	 *					|			|       COMO ERA ANTES DO{Mockito.spy} --->    usuarioService = new UsuarioServiceImplementation(usuarioRepository);|     |
	 *                  |   		|       *** Aqui eu posso utilizar metodos reais do meu usuarioService ***                                          |     |
	 *					|   		|                                                                                                                   |     |
	 *					|			|       COMO ERA ANTES DO{@MockBean}   --->    usuarioRepository =Mockito.mock(UsuarioRepository.class);            |     |
	 *					|			|		*** atráves do mokito posso fazer chamadas fakes para o meu repository ***                                  |     |
	 *					|			|	          																									    |     |
 	 *					|   		|-------------------------------------------------------------------------------------------------------------------|     |
     *                  |																																	  |
     *                  |          }																														  |
     *                  |-------------------------------------------------------------------------------------------------------------------------------------|
	 */
	
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
																				  /*01
																					02
																					03
																					04
																					05
																					06
																					07
																					08
																					09
																					10*/
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
	//------------------------------------------------------------------------------------------------------------------------------------------------//
	@Test
	public void deveSalvarUsuario() {
		Assertions.assertDoesNotThrow(() -> {
		// CENÁRIO   <- para salvar usuario é preciso mokar o metodo que está dentro de usuarioServiceImplmentation chamado salvarUsuario
	/* { 1° -> } */	   Mockito.doNothing().when(usuarioService).validarEmail(Mockito.anyString());								
	/* { 2° -> } */	   Usuario usuarioBuildMetodoSalvar = Usuario.builder().id(1l).nome("nome").email("email@emial.com").senha("senha").build();
	/* { 3° -> } */	   Mockito.when(usuarioRepository.save(Mockito.any(Usuario.class))).thenReturn(usuarioBuildMetodoSalvar);  
																												/*
		 																										 * Mas como a gente vai mokar o método, se quando a gente criar o 
		|-----------------------------------------------------------------------|								 * seu mockito a gente não vai está testanto o Método Real 
		|   Como esse Método funciona?											|								 * dentro de usuarioService? E além disso todos os outros métodos iriam dar 
		| 1° - Mockito.doNothing().when(usuarioService)                         |							     * erro pois simplesmente ele utilizam e testam métodos reais 
		| 1° - Diferente do MockBean, o SpyBean "moka"	primeiro moka a class   |								 * dentro de usuarioService.
		| 1° - Ele chama o método da class e depois "moka" o método, mas        | 								 * 
		| 1° - ele utiliza o método orginal do que uma instância do método		|								 * Simplesmente para resolver esse problema é só utilizar 
		|-----------------------------------------------------------------------|								 * o método {spy} 
		|3° - Quando Chamar o método salvarRepository, Mockito.anyUsuario(passa |								 *
		|qualquer que for usuario salvo), e retorna o usuario buildado:  		|								 *
		| usuarioBuildMetodoSalvar.										        |								 *
		|-----------------------------------------------------------------------|								 *
																												 */
	
		//AÇÃO
		Usuario usuarioSalvo = usuarioService.salvarUsuario(new Usuario());
		
		//VERIFICAÇÃO
		Assertions.assertNotNull(usuarioSalvo);
		Assertions.assertNotNull(usuarioSalvo.getId().equals(1l));
		Assertions.assertNotNull(usuarioSalvo.getNome().equals("nome"));
		Assertions.assertNotNull(usuarioSalvo.getEmail().equals("email@email.com"));
		Assertions.assertNotNull(usuarioSalvo.getSenha().equals("senha"));
	});	
	}
	//----------------------------------------------------------	--------------------------------------------------------------------------------------//
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
																			  /*01
																				02
																				03
																				04
																				05
																				06
																				07
																				08
																				09
																				10*/
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
	//------------------------------------------------------------------------------------------------------------------------------------------------//
	@Test
	public void naoSalvarUmUsuarioComEmailJaCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {
		//CENÁRIO
			String email = "email@email.com";
			Usuario usuarioBuildNaoSalvarEmailCadastrado = Usuario.builder().email("email@email.com").build(); 
				Mockito.doThrow(RegraNegocioException.class).when(usuarioService).validarEmail(email);
				
		//AÇÃO
			usuarioService.salvarUsuario(usuarioBuildNaoSalvarEmailCadastrado);
			 /*                 |---------------------------------------------------------------------------------------------------| 
			 *					|										     AÇÃO    												|
			 *					|---------------------------------------------------------------------------------------------------|
			 *					| Estou dizendo para usar o método de SalvarUsuario dentro de usuarioBuildNaoSalvarEmailCadatrado   |
			 *                  |---------------------------------------------------------------------------------------------------|
			 */                  
			
		//VERIFICAÇÃO
			Mockito.verify(usuarioRepository, Mockito.never()).save(usuarioBuildNaoSalvarEmailCadastrado);
			 /*                 |---------------------------------------------------------------------------------------------------| 
			 *					|										VERIFICAÇÃO   												|
			 *					|---------------------------------------------------------------------------------------------------|
			 *					| Simplesmente estou dizendo que eu quero que ele nunca chame o método de salvar e por isso estou   |
		     *					| usando a seguinte linha de código: 																|
		     *					| {Mockito.never()).save(usuarioBuildNaoSalvarEmailCadastrado}   									|
		     *					| Mockito Nunca Chame o Método .save de {salvarUsuario} dentro de usarioBuildNaoSalvarEmailCad...   |
		     *					|                                                                                                   |
		     *					| - EU ESCREVO ISSO PORQUE EU NÃO QUERO QUE ELE SALVE AQUELE EMAIL QUE JÁ ESTÁ CADASTRADO, PQ IRIA  |
		     *					|   FODER MINHA APLICAÇÃO, POIS MÉTODO É SÓ PARA "VERIFICAR" QUE AQUELE EMAIL JÁ ESTÁ CADASTRADO.   |
			 *                  |---------------------------------------------------------------------------------------------------|
			 */                  
		
		});
	}
	//------------------------------------------------------------------------------------------------------------------------------------------------//
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
																				  /*01
																					02
																					03
																					04
																					05
																					06
																					07
																					08
																					09
																					10*/
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
	//------------------------------------------------------------------------------------------------------------------------------------------------//
	@Test
	public void deveAutenticarUmUsuarioComSucesso() {
		Assertions.assertDoesNotThrow(() -> {
			// cenario
			String email = "email@email.com";
			String senha = "senha";

			Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
			Mockito.when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

			// acao
			Usuario result = usuarioService.autenticar(email, senha);

			// verificacao
			Assertions.assertNotNull(result);
		});

	}
	//------------------------------------------------------------------------------------------------------------------------------------------------//
	
	//------------------------------------------------------------------------------------------------------------------------------------------------//
	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		
		String email = "EMAILERRADO.VAIDARERRO!!!!@email.com";
		Usuario usuario = Usuario.builder().email("email@email.com").senha("senha").build();
		
		//CENÁRIO
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

		//AÇÃO/EXECUÇÃO
		Throwable throwable = Assertions.assertThrows(ErroAutenticacao.class, ()-> 
		usuarioService.autenticar(email, "senha"));
 
		//VERIFICAÇÃO
		consolePrintEmail("deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado", throwable.getMessage());
}
	//------------------------------------------------------------------------------------------------------------------------------------------------//

	//------------------------------------------------------------------------------------------------------------------------------------------------//
		public void consolePrintEmail(String nomeMetodo, String mensagem) {
			System.out.println("\u001B[40m\u001B[37m");
			System.out.println(nomeMetodo + ": " + mensagem);
			System.out.println("\u001B[0m");
			}
	//------------------------------------------------------------------------------------------------------------------------------------------------//
	
	//------------------------------------------------------------------------------------------------------------------------------------------------//
	/*
	 * 
	 * 				----------------------------------------------------------------------------------------------------------------
	 * 		------------------   CENARIO (ANTIGO){deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComOEmailInformado}:   ------------------					
	 * 				----------------------------------------------------------------------------------------------------------------
	 * 
	 *                  |---------------------------------------------------------------------------------------------------| 
	 *					|	Assertions.assertThrows(ErroAutenticacao.class, () -> {											|
	 *					|	// cenario																						|
	 *					|	Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());  |
     *                  |																									|
     *					|	// acao																							|
     *					|	usuarioService.autenticar("email@gmail.com", "senha");											|
	 *					|		});																							|
	 *   				|	}																								|
	 *                  |---------------------------------------------------------------------------------------------------|
	 *
	 */
	//------------------------------------------------------------------------------------------------------------------------------------------------//

//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
																			  /*01
																				02
																				03
																				04
																				05
																				06
																				07
																				08
																				09
																				10*/
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
	//------------------------------------------------------------------------------------------------------------------------------------------------//
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
	
		//CENÁRIO
		String senha = "1234errado";
		Usuario usuario = criarUsuario();
		Mockito.when(usuarioRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
	
		//AÇÃO/EXECUÇÃO
		Throwable throwable = Assertions.assertThrows(ErroAutenticacao.class, ()-> 
		usuarioService.autenticar("email@email.com", senha));		
	 
		//VERIFICAÇÃO
		consolePrint("deveLancarErroQuandoSenhaNaoBater", throwable.getMessage());
	}
	//------------------------------------------------------------------------------------------------------------------------------------------------//
	
	//------------------------------------------------------------------------------------------------------------------------------------------------//

			public static Usuario criarUsuario() {
				return Usuario.builder().email("email@email.com").senha("1234").build();  //<--- podemos ver que a senha está dando 
				}																		 //errada pela pura lógica, aqui estamos
				 																		//passando senha = "1234" e lá a senha é = "senhaErrada"
			public void consolePrint(String nomeMetodo, String mensagem) {
				System.out.println("\u001B[40m\u001B[37m");
				System.out.println(nomeMetodo + ": " + mensagem);
				System.out.println("\u001B[0m");
				}
	//------------------------------------------------------------------------------------------------------------------------------------------------//	
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
																				  /*01
																					02
																					03
																					04
																					05
																					06
																					07
																					08
																					09
																					10*/
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
	//------------------------------------------------------------------------------------------------------------------------------------------------//
	@Test
	public void deveValidarEmail() {
		Assertions.assertDoesNotThrow(() -> {
			// cenario
			Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(false); // <- mockito(chamda
																									// fake)
																									// thenReturn(false)
																									// quando
																									// <- string(email)
																									// qualquer for
																									// passada dentro do
																									// banco seja
																									// diferente
																									// existsByEmail:
																									// verifique se o
																									// email existe
																									// <- caso for
																									// diferente da
																									// String(email) que
																									// já foi passado
																									// ele ira retornar
																									// false
			
			// acao
			usuarioService.validarEmail("email@email.com");
		});
	}
	//------------------------------------------------------------------------------------------------------------------------------------------------//

	//------------------------------------------------------------------------------------------------------------------------------------------------//
			/*
			 * 
			 * 
			 *  			----------------------------------------------------------------------------------------------------------------
			 * 									------------------   CENARIO -> //CENARIO (ANTIGO):   ------------------					
			 * 				----------------------------------------------------------------------------------------------------------------
			 *  							|-------------------------------------------------------------------------------------|
			 * 								|																					  |
			 * 								|   usuarioRepository.deleteAll(); 													  |
			 * 								|	Isso aqui está simplesmente deletando tudo que existe dentro do	                  |
			 *  							|	Repository(Banco) para garantir que não exite nada dentro de usuario           	  |
			 * 								|																					  |
			 * 								|-------------------------------------------------------------------------------------|
			 * 								|																					  |
			 *  							|    // acao																		  |
			 * 								|																					  |
			 *  							|-------------------------------------------------------------------------------------|
			 * 
			 * 
			 * 
			 */
	//------------------------------------------------------------------------------------------------------------------------------------------------//
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
																				  /*01
																					02
																					03
																					04
																					05
																					06
																					07
																					08
																					09
																					10*/
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
	//------------------------------------------------------------------------------------------------------------------------------------------------//
	@Test
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			// cenario
			Mockito.when(usuarioRepository.existsByEmail(Mockito.anyString())).thenReturn(true);

			// acao
			usuarioService.validarEmail("email@email.com");

		});
	}
}
	//------------------------------------------------------------------------------------------------------------------------------------------------//
//--------------------------------------------------------------------------------------------------------------------------------------------------------------------------//