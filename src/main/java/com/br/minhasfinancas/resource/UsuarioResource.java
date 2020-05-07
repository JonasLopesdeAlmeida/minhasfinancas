package com.br.minhasfinancas.resource;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.minhasfinancas.api.dto.UsuarioDTO;
import com.br.minhasfinancas.model.Usuario;
import com.br.minhasfinancas.services.LancamentoService;
import com.br.minhasfinancas.services.UsuarioService;
import com.br.minhasfinancas.services.exception.ErrodeAutenticacao;
import com.br.minhasfinancas.services.exception.RegraNegocioException;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

	// como e um bean gerenciavel eu nao preciso colocar o @AutoWired
	@Autowired
	private UsuarioService service;
	@Autowired
	private LancamentoService lancamentoservice;
	// construtor para a injeção de dependencia.
//	public UsuarioResource(UsuarioService service) {
//		this.service = service;
//	}

	// aqui um detalhe quanto ao POST.
	// EU JA TENHO UM POST PARA SALVAR UM USUARIO. PRECISO DE OUTRO POST PARA
	// AUTENTICAR.
	// DESSA FORMA EU PRECISO DAR UM ENDPOINT PARA ESSE POST COMO ABAIXO:
	// @PostMapping("/autenticar")
	// ASSIM EU DEVEREI CHAMAR ESSE POST DESSA FORMA: /api/usuarios/autenticar
	@PostMapping("/autenticar")
	public ResponseEntity autenticar(@RequestBody UsuarioDTO dto) {

		try {
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			// aqui retornando o codigo 200 e o corpo da requisicao.
			return ResponseEntity.ok(usuarioAutenticado);
		} catch (ErrodeAutenticacao e) {
			// aqui pode mostrar a mensagem tanto de email quanto a de senha invalida.
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	// ResponseEntity representa o corpo da resposta
	// @RequestBody exigindo que os dados do usuario venham com as propriedades
	// passadas no usuarioDTO.
	@PostMapping
	public ResponseEntity salvar(@RequestBody UsuarioDTO dto) {

		// transformando um usuario em dto.
		// OBS:no outro projeto esse cosntrutor foi criado na classe de servico e aqui
		// eu chamava o metodo com esse construtor.
		Usuario usuario = Usuario.builder().nome(dto.getNome()).email(dto.getEmail()).senha(dto.getSenha()).build();

		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}

	}

	@GetMapping("{id}/saldo")
	public ResponseEntity obterSaldo(@PathVariable("id") Long id) {

		Optional<Usuario> usuario = service.obterPorId(id);

		if (!usuario.isPresent()) {
            //verificando se existe o usuario refeente ao id passado com parametro.
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
        //recebendo id como parametro para obter o saldo.
		BigDecimal saldo = lancamentoservice.obterSaldoPorUsuario(id);
		//retornando o corpo do saldo em json.
		return ResponseEntity.ok(saldo);
	}

}
