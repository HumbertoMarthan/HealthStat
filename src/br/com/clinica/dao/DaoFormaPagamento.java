package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.financeiro.FormaPagamento;
import br.com.clinica.repository.interfaces.RepositoryFormaPagamento;
@Repository
public class DaoFormaPagamento extends ImplementacaoCrud<FormaPagamento> implements RepositoryFormaPagamento{

	
	private static final long serialVersionUID = 1L;

}
