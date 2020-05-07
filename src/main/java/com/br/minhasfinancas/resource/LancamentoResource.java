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

import com.br.minhasfinancas.api.dto.AtualizaStatusDTO;
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
			//@RequestParam usado para passar os parametros de busca pela url.
			//descrição, mês e ano serão parametros opcionais.
			@RequestParam(value="descricao", required= false) String descricao,
			@RequestParam(value="mes", required= false) Integer mes,
			@RequestParam(value="ano", required= false) Integer ano,
			//aqui eu coloco o usuario sendo passado como parametro para saber o lançamento desse usuario.
			@RequestParam(value="usuario") Long idUsuario //essa variavel ira receber o id do usuario.
			
			) {
		
		Lancamento lancamentoFiltro = new Lancamento();
		
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
        
		//recuperando id do usuario em uma variavel "usuario".
		Optional<Usuario>usuario = usuarioservice.obterPorId(idUsuario);
	    if(!usuario.isPresent()) {
	    	//se nao existir o id passado por parametro, ira retornar uma mensagem de erro.
	    	return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para a consulta.");
	    }else {
	    	//caso o id fornecido existe para um usuario cadastrado. Entao eu retorno o id e vou buscar o lancamento dele na tabela lancamentos.
	    	lancamentoFiltro.setUsuario(usuario.get());
	    }
	    //buscando uma lista de lancamentos para esse ususario.
	   List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
	   //retornando o corpo json com os lancamentos encontrados para esse usuario.
	   return ResponseEntity.ok(lancamentos);
	}
	
	
	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {

		try {
			//aqui ele converte o dto e retorna o meu obj lançamento.
			Lancamento obj = converter(dto);
			//aqui o obj recebe um lancamennto obj salvo
			obj = service.salvar(obj);
			//aqui eu retorno o corpo do obj salvo na base de dados com o 202 created.
			return new ResponseEntity(obj, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
             //aqui recebe uma mensagem de erro com o badrequest.
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("{id}")
	public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
        //buscando um lançamento na base por obterPorId
		return service.obterPorId(id).map(entity -> {
        //o map passando a entity como parametro que nada mais é o retorno no meu optionao obterporid.
			try {
				//convertendo o dto em lançamento
				Lancamento obj = converter(dto);
				//aqui eu vou setar o id do obj lançamento, com a entity que eu achei na base de dados.
				obj.setId(entity.getId());
				//assim agora eu posso chamar o método de atulizar.
				service.atualizar(obj);
				//aora eu retorno o corpo do lançamento atualizado.
				return ResponseEntity.ok(obj);

			} catch (RegraNegocioException e) {
				//se esse lançamento nao for encontrado ele retorna a mensagem de erro com o número 400.
				return ResponseEntity.badRequest().body(e.getMessage());
			}

		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));

	}
	
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto ) {
		 //buscando um lançamento na base por obterPorId
		return service.obterPorId(id).map(entity -> {
		    //o metodo value.of() vai buscar na classe enumarada StatusLancamento a enumercao referente a String que eu passsar dentro dele.
			//entao ele verifica se existe alguma enumeracao na classe referente a que foi passada e dai retorna.
			StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
		if(statusSelecionado == null) {
			return ResponseEntity.badRequest().body("Não foi possível atualizar o status do lançamento, envie um status válido.");
		}
		try {
			//se for encontrado a entidade vai setar o novo status atualizado.
			entity.setStatus(statusSelecionado);
			//aplicando o metodo atualizar
			service.atualizar(entity);
			//retornando o corpo em formato json.
			return ResponseEntity.ok(entity);
		}
		catch (RegraNegocioException e) {
			//aqui retorna uma mensagem de erro personalizada que foi feita na validação de status.
			return ResponseEntity.badRequest().body(e.getMessage());
		}
			
		}).orElseGet(() -> 
		new ResponseEntity("Lançamento não encontrado na base de dados", HttpStatus.BAD_REQUEST));
	}
	

	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		//Da mesma forma retorno um lançamento na base por obterPorId
		return service.obterPorId(id).map(entidade -> {
        //Obs: aqui eu não preciso converter para um DTO porque eu nao recebo um corpo json do lançamento deletado.
			//aqui eu deleto a entidade encontrada na base pelo id.
			service.deletar(entidade);
			//como aqui eu estou só deletando eu já nao passo o corpo do lançamento
			//so mostro o numero 204 referente ao NO_CONTENT que diz que foi deletado.
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		//da mesma forma passo a mensagem de erro caso a entidade não for encontrada na base de dados.
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
                 //obterPorId método implementado em UsuarioImpService
				//que busca por id um usuario.
				.obterPorId(dto.getUsuario())
				//se ele não conseguir encontrar o usuario retornamos uma mensagem de erro.
				.orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o id informado"));

		lancamento.setUsuario(usuario);
		//precisa verificar se eles nao sao nulos para setar.
		if(dto.getTipo()!= null) {
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		}

		if(dto.getStatus() != null) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}
		

		return lancamento;
	}

}
