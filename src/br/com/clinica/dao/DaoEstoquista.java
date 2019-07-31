package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.pessoa.Estoquista;
import br.com.clinica.repository.interfaces.RepositoryEstoquista;
@Repository
public class DaoEstoquista extends ImplementacaoCrud<Estoquista> implements RepositoryEstoquista{

	
	private static final long serialVersionUID = 1L;

}
