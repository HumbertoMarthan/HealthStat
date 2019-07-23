package br.com.dao.implementacao;

import org.springframework.stereotype.Repository;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.projeto.model.Estoquista;
import br.com.repository.interfaces.RepositoryEstoquista;
@Repository
public class DaoEstoquista extends ImplementacaoCrud<Estoquista> implements RepositoryEstoquista{

	
	private static final long serialVersionUID = 1L;

}
