package com.br.minhasfinancas.services.implementacoes;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.minhasfinancas.model.Usuario;
import com.br.minhasfinancas.repositories.UsuarioRepository;
import com.br.minhasfinancas.services.UsuarioService;
import com.br.minhasfinancas.services.exception.ErrodeAutenticacao;
import com.br.minhasfinancas.services.exception.RegraNegocioException;
@Service
public class UsuarioImpService implements UsuarioService{
	
	
	UsuarioRepository repository;
	
    //aqui diz que o usuario so vai acessar com uma instancia de usuariorepository.
	public UsuarioImpService(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario =  repository.findByEmail(email);
	
		// verificando agora se esse usuario veio nulo ou preenchido.
		if(!usuario.isPresent()) {
			throw new ErrodeAutenticacao("Usuário não encontrado para o email informado!");		
		}
		//o metodo get da classe optional retorna o objeto que está lá dentro.
		//tb verificando se a senha nao for igual.
		if(!usuario.get().getSenha().equals(senha)) {
			throw new ErrodeAutenticacao("Senha inválida!");
		}	
		
		return usuario.get();
	}

	
	
	@Override
	@Transactional //vai abrir uma transacao, vai escutar o metodo de salvar o usuario e depois que ele salvar ele vai comitar.
	public Usuario salvarUsuario(Usuario usuario) {
		//Antes de salvar um usuario eu vou precisar validar o email.
		validarEmail(usuario.getEmail());
		//aqui ele ja retona a instancia de usuario ja persistida no banco.
		return repository.save(usuario);
	}

	@Override
     public void validarEmail(String email) {
		boolean existe = repository.existsByEmail(email);
		if(existe){
			
			throw new RegraNegocioException("Ja existe um usuario cadastrado com este email");
		}
	
	}

	@Override
	public Optional<Usuario>obterPorId(Long id) {		
		return repository.findById(id);
	}

}
