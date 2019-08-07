package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.financeiro.ParcelaPagar;
import br.com.clinica.repository.interfaces.RepositoryParcelaPagar;
@Repository
public class DaoParcelaPagar extends ImplementacaoCrud<ParcelaPagar> implements RepositoryParcelaPagar{
	
	private static final long serialVersionUID = 1L;

}
