package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.financeiro.PagamentoEspecial;
import br.com.clinica.repository.interfaces.RepositoryPagamentoEspecial;
@Repository
public class DaoPagamentoEspecial extends ImplementacaoCrud<PagamentoEspecial> implements RepositoryPagamentoEspecial{

	
	private static final long serialVersionUID = 1L;

}
