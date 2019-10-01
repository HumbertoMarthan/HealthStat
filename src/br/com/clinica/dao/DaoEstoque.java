package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.estoque.Estoque;
import br.com.clinica.repository.interfaces.RepositoryEstoque;
@Repository
public class DaoEstoque extends ImplementacaoCrud<Estoque> implements RepositoryEstoque{

	
	private static final long serialVersionUID = 1L;

}
