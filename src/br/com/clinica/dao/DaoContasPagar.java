package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.financeiro.ContasPagar;
import br.com.clinica.repository.interfaces.RepositoryContasPagar;
@Repository
public class DaoContasPagar extends ImplementacaoCrud<ContasPagar> implements RepositoryContasPagar{
	
	private static final long serialVersionUID = 1L;

}
