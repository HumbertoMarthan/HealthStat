package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.financeiro.ContasReceber;
import br.com.clinica.repository.interfaces.RepositoryContasReceber;
@Repository
public class DaoContasReceber extends ImplementacaoCrud<ContasReceber> 
implements RepositoryContasReceber{
	
	private static final long serialVersionUID = 1L;

}
