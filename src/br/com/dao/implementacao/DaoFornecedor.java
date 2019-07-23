package br.com.dao.implementacao;

import org.springframework.stereotype.Repository;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.projeto.model.Fornecedor;
import br.com.repository.interfaces.RepositoryFornecedor;
@Repository
public class DaoFornecedor extends ImplementacaoCrud<Fornecedor> implements RepositoryFornecedor{

	
	private static final long serialVersionUID = 1L;

}
