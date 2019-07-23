package br.com.dao.implementacao;

import org.springframework.stereotype.Repository;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.projeto.model.Prontuario;
import br.com.repository.interfaces.RepositoryProntuario;
@Repository
public class DaoProntuario extends ImplementacaoCrud<Prontuario> implements RepositoryProntuario{

	
	private static final long serialVersionUID = 1L;

}
