package br.com.dao.implementacao;

import org.springframework.stereotype.Repository;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.projeto.model.Acompanhante;
import br.com.repository.interfaces.RepositoryAcompanhante;
@Repository
public class DaoAcompanhante 
extends ImplementacaoCrud<Acompanhante> implements RepositoryAcompanhante{
	private static final long serialVersionUID = 1L;
}
