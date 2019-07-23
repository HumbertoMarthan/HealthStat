package br.com.dao.implementacao;

import org.springframework.stereotype.Repository;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.projeto.model.Doenca;
import br.com.repository.interfaces.RepositoryDoenca;
@Repository
public class DaoDoenca extends ImplementacaoCrud<Doenca> 
implements RepositoryDoenca {

	
	private static final long serialVersionUID = 1L;

}
