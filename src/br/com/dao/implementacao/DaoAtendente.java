package br.com.dao.implementacao;

import org.springframework.stereotype.Repository;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.projeto.model.Atendente;
import br.com.repository.interfaces.RepositoryAtendente;
@Repository
public class DaoAtendente extends ImplementacaoCrud<Atendente> implements RepositoryAtendente{

	
	private static final long serialVersionUID = 1L;

}
