package com.vinicius.minhasfinancas.Service.implementation;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vinicius.minhasfinancas.Service.LancamemtoService;
import com.vinicius.minhasfinancas.exception.RegraNegocioException;
import com.vinicius.minhasfinancas.model.entity.Lancamento;
import com.vinicius.minhasfinancas.model.enums.StatusLancamento;
import com.vinicius.minhasfinancas.model.repository.LancamentoRepository;



@Service //<- adicionando para garantir que ele vai esta dentro da injeção de dependecias com "Serviço"
public class LancamentoServiceImplementation implements LancamemtoService{

	private LancamentoRepository lancamentoRepository;
	
	public LancamentoServiceImplementation(LancamentoRepository lancamentoRepository) {
		this.lancamentoRepository = lancamentoRepository;
	}
	
	
	
	@Override
	@Transactional //<- O spring vai abrir uma transação, executar o conteudo do método, ao final vai fazer um commit se deu sucesso ou erro 
	public Lancamento salvar(Lancamento lancamento) {
		validar(lancamento);
		lancamento.setStatus(StatusLancamento.PEDENTE);
		return lancamentoRepository.save(lancamento);
	}

	//-----------------------------------------------------------------------------//
	
	@Override
	@Transactional
	public Lancamento atualizar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());  //<- estou garantindo que ele vai passar lancamento com id, 
													// se não ele vai lançar uma exception(Objects.requireNonNull);
		validar(lancamento);
		return lancamentoRepository.save(lancamento);
	}

	//-----------------------------------------------------------------------------//	
	
	@Override
	@Transactional
	public void deletar(Lancamento lancamento) {
		Objects.requireNonNull(lancamento.getId());
		 lancamentoRepository.delete(lancamento);
	}

	//-----------------------------------------------------------------------------//
	
	/*
	 *  //-----------------------------------------------------------------------//
	 * 		 -----------------------------------------------------------------
	 * 		 		    Explicando como funciona o Método Buscar()
	 * 		 -----------------------------------------------------------------
	 * 	  
	 * 	  através da API -> "Riquered.By.Example" da INTERFACE -> org.springframework.data.domain,
	 * 	  ele vai pegar uma instancia do objeto passado("lancamentoFiltro") com os dados prenchidos, 
	 * 	  a gente vai passar os dados para o Example, e lá no Repository ele vai pegar o Example e ele vai 
	 * 	  fazer a consulta nas propiedades que foram preenchidas no "LancamentoFiltro" 
	 * 
	 */
	
	@Override
	@Transactional(readOnly = true)
	public List<Lancamento> buscar(Lancamento lancamentoFiltro) {           
		Example example = Example.of(lancamentoFiltro, 
				ExampleMatcher.matching()
				.withIgnoreCase()
				.withStringMatcher(StringMatcher.CONTAINING));   // <- CONTAINING: ELE FUNCIONA DA SEGUINTE MANEIRA -> PASSE TODAS AS DESCRIÇÕES QUE TEM A LETRA "A" NO MEIO.
		
		return lancamentoRepository.findAll(example);	
	}

	//-----------------------------------------------------------------------------//
	
	@Override
	public void atualizarStatus(Lancamento lancamento, StatusLancamento statusLancamento) {
		lancamento.setStatus(statusLancamento);
		atualizar(lancamento);
	}



	@Override
	public void validar(Lancamento lancamento) {
		
		if(lancamento.getDescricao() == null || lancamento.getDescricao().trim().equals("")) {
			throw new RegraNegocioException("Informe uma Descrição válida");
		}
		if(lancamento.getMes() == null || lancamento.getMes() < 1 || lancamento.getMes() > 12) {
			throw new RegraNegocioException("Informe um Mês válido");
		}
		if(lancamento.getAno() == null || lancamento.getAno().toString().length() != 4) {
			throw new RegraNegocioException("Informe um Ano válido");
		}
		if(lancamento.getUsuario() == null ||  lancamento.getUsuario().getId() == null ) {
			throw new RegraNegocioException("Informe um Usúario");
		}
		if(lancamento.getValor() == null || lancamento.getValor().compareTo(BigDecimal.ZERO) < 1) {
			throw new RegraNegocioException("Informe um Valor válido");
		}
		if(lancamento.getTipo() == null) {
			throw new RegraNegocioException("Informe um tipo de Lancamento");
		}

	}

}
