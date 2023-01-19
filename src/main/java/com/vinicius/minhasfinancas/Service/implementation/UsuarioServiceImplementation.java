package com.vinicius.minhasfinancas.Service.implementation;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vinicius.minhasfinancas.exception.ErroAutenticacao;
import com.vinicius.minhasfinancas.exception.RegraNegocioException;
import com.vinicius.minhasfinancas.model.entity.Usuario;
import com.vinicius.minhasfinancas.model.repository.UsuarioRepository;
import com.vinicius.minhasfinancas.Service.UsuarioService;

@Service
public class UsuarioServiceImplementation implements UsuarioService {

	private UsuarioRepository usuarioRepository;

	public UsuarioServiceImplementation(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}
	
	
	
	//---------------------------------------------------------------------------------------------//
	
	
	
	// esse método simplesmente vai ver se existe tal email no banco, caso não existe
	// vai lançar uma mensagem de erro, mas caso exista ele vai comparar a senha que
	// foi passada para ai depois ser logado dentro do Banco	
	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
		
		if (!usuario.isPresent()) {
			throw new ErroAutenticacao("Email inválido!"); 
		}
		
		if (!usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida!");	
		}
		
		return usuario.get();
	}

	
	
	//---------------------------------------------------------------------------------------------//
	
	
	
	@Override
	@Transactional // <- vai abrir uma transação dentro do Banco, vai executar o Método
					// usuario.save(<- de salvar usuario) e depois que ele salvar ele vai comitar;
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return usuarioRepository.save(usuario);

	}

	
	
	//---------------------------------------------------------------------------------------------//
	
	
	
		
	@Override
	public void validarEmail(String email) {
		boolean existe = usuarioRepository.existsByEmail(email);
		if (existe) {
			throw new RegraNegocioException("Já existe um usuário cadastrado com esse email.");
		}

	}

}