package com.br.minhasfinancas.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.minhasfinancas.api.dto.LancamentoDTO;
import com.br.minhasfinancas.model.Lancamento;
import com.br.minhasfinancas.model.Usuario;
import com.br.minhasfinancas.model.enums.StatusLancamento;
import com.br.minhasfinancas.model.enums.TipoLancamento;
import com.br.minhasfinancas.services.LancamentoService;
import com.br.minhasfinancas.services.UsuarioService;
import com.br.minhasfinancas.services.exception.RegraNegocioException;

@RestController
@RequestMapping("/api/lancamentos")
public class LancamentoResource {

	@Autowired
	private LancamentoService service;

	@Autowired
	private UsuarioService usuarioservice;

	@GetMapping
	public ResponseEntity buscar(
			@RequestParam(value="descricao", required= false) String descricao,
			@RequestParam(value="mes", required= false) Integer mes,
			@RequestParam(value="ano", required= false) Integer ano,
			@RequestParam(value="usuario") Long idUsuario
			
			) {
		
		Lancamento lancamentoFiltro = new Lancamento();
		
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
        
		//recuperando id do usuario em uma variavel.
		
		Optional<Usuario>usuario = usuarioservice.obterPorId(idUsuario);
	    if(!usuario.isPresent()) {
	    	return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para a consulta.");
	    }else {
	    	lancamentoFiltro.setUsuario(usuario.get());
	    }
	   List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
	   return ResponseEntity.ok(lancamentos);
	}
	
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {

		try {
			Lancamento obj = converter(dto);
			obj = service.salvar(obj);
			return new ResponseEntity(obj, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {

			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {

		return service.obterPorId(id).map(entity -> {

			try {
				Lancamento obj = converter(dto);
				obj.setId(entity.getId());
				service.atualizar(obj);
				return ResponseEntity.ok(obj);

			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}

		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));

	}

	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return service.obterPorId(id).map(entidade -> {

			service.deletar(entidade);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}

	// esse método converte um objeto para um objetoDto
	// Obs: esse metodo pode ser implementado na classe de servico tb.
	// em outro projeto eu implementei na classe de serviço.
	private Lancamento converter(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());

		Usuario usuario = usuarioservice

				.obterPorId(dto.getUsuario())
				.orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o id informado"));

		lancamento.setUsuario(usuario);
		if(dto.getTipo()!= null) {
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		}

		if(dto.getStatus() != null) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}
		

		return lancamento;
	}

}
