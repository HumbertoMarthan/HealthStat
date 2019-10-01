package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.financeiro.ParcelaContasPagar;
import br.com.clinica.repository.interfaces.RepositoryParcelaContasPagar;
@Repository
public class DaoParcelaContasPagar extends ImplementacaoCrud<ParcelaContasPagar> implements RepositoryParcelaContasPagar{
	
	private static final long serialVersionUID = 1L;

}
