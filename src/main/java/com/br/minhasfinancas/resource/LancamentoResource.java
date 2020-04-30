package com.br.minhasfinancas.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

	@PostMapping
	public ResponseEntity salvar(@RequestBody LancamentoDTO dto) {
		return null;

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
		lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
	
		return lancamento;
	}

}
