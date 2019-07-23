package br.com.dao.implementacao;

import org.springframework.stereotype.Repository;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.projeto.model.Pessoa;
import br.com.repository.interfaces.RepositoryPessoa;
@Repository
public class DaoPessoa extends ImplementacaoCrud<Pessoa> implements RepositoryPessoa{

	
	private static final long serialVersionUID = 1L;

}
