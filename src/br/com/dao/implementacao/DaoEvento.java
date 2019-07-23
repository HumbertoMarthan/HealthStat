package br.com.dao.implementacao;

import org.springframework.stereotype.Repository;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.projeto.model.Evento;
import br.com.repository.interfaces.RepositoryEvento;
@Repository
public class DaoEvento extends ImplementacaoCrud<Evento> implements RepositoryEvento{

	
	private static final long serialVersionUID = 1L;

}
