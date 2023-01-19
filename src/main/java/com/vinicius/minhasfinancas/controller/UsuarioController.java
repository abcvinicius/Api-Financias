package com.vinicius.minhasfinancas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vinicius.minhasfinancas.Service.UsuarioService;
import com.vinicius.minhasfinancas.api.dto.UsuarioDTO;
import com.vinicius.minhasfinancas.exception.ErroAutenticacao;
import com.vinicius.minhasfinancas.exception.RegraNegocioException;
import com.vinicius.minhasfinancas.model.entity.Usuario;

@RestController
@RequestMapping("/api/usuarios") // <- aqui estou dizendo aqui: todas as requisições que começarem com /api/usuarios vão está dentro desse conteudo
public class UsuarioController {

//----------------------------------------------------------------------------------------------------------------------------//

	private UsuarioService usuarioService;
	
//----------------------------------------------------------------------------------------------------------------------------//
//----------------------------------------------------------------------------------------------------------------------------//
	
	public UsuarioController(UsuarioService usuarioService) {
		this.usuarioService = usuarioService;
	}
//----------------------------------------------------------------------------------------------------------------------------//

	
	
//----------------------------------------------------------------------------------------------------------------------------//
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {
		try {
		Usuario usuarioAutenticado = usuarioService.autenticar(dto.getEmail(), dto.getSenha());
		return ResponseEntity.ok(usuarioAutenticado);
		}catch(ErroAutenticacao e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
//----------------------------------------------------------------------------------------------------------------------------//		
	
	
	
//----------------------------------------------------------------------------------------------------------------------------//
   /*
	* --------------------------------------------------------------------------------------------
	* PARA QUE SERVE O @RequestBody??
	* --------------------------------------------------------------------------------------------
	* O RequestBody vai dizer que O objeto que vem da requisição Json com os dados do usuário, 
	* seja transformado no Objeto passado, que esse objeto é exatamente {***UsuarioDTO***}
	* -------------------------------------------------------------------------------------------
	* Além disso ele deixa claro que o Objeto passado tem que ser igual o parametro que está dentro de UsuarioDto,
	* caso não seja ele irá dar erro.
	* -------------------------------------------------------------------------------------------
	*/
	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {
		Usuario usuario = Usuario.builder().nome(dto.getNome()).email(dto.getEmail()).senha(dto.getSenha()).build();
		try {
			Usuario usuarioSalvo = usuarioService.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);   //<- Aqui está dessa forma porque eu quero enviar o Objeto ao invés da URL do Objeto
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
//----------------------------------------------------------------------------------------------------------------------------//
}
