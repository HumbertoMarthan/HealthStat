package br.com.dao.implementacao;

import org.springframework.stereotype.Repository;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.projeto.model.Especialidade;
import br.com.repository.interfaces.RepositoryEspecialidade;
@Repository
public class DaoEspecialidade 
extends ImplementacaoCrud<Especialidade> implements RepositoryEspecialidade{
	private static final long serialVersionUID = 1L;
}
