package br.com.dao.implementacao;

import org.springframework.stereotype.Repository;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.projeto.model.Convenio;
import br.com.repository.interfaces.RepositoryConvenio;
@Repository
public class DaoConvenio extends ImplementacaoCrud<Convenio> 
implements RepositoryConvenio {

	
	private static final long serialVersionUID = 1L;

}
