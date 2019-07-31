package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.estoque.Fornecedor;
import br.com.clinica.repository.interfaces.RepositoryFornecedor;
@Repository
public class DaoFornecedor extends ImplementacaoCrud<Fornecedor> implements RepositoryFornecedor{

	
	private static final long serialVersionUID = 1L;

}
