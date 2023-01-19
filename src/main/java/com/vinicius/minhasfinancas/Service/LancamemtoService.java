package com.vinicius.minhasfinancas.Service;

import java.util.List;

import com.vinicius.minhasfinancas.model.entity.Lancamento;
import com.vinicius.minhasfinancas.model.enums.StatusLancamento;

public interface LancamemtoService {

	Lancamento salvar(Lancamento lancamento);
	
	Lancamento atualizar(Lancamento lancamento);
	
	void deletar(Lancamento lancamento);
	
	List<Lancamento> buscar(Lancamento lancamentoFiltro);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento statusLancamento);
	
	void validar(Lancamento lancamento);
}
