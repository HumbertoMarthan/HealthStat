package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.outro.Doenca;
import br.com.clinica.repository.interfaces.RepositoryDoenca;
@Repository
public class DaoDoenca extends ImplementacaoCrud<Doenca> 
implements RepositoryDoenca {

	
	private static final long serialVersionUID = 1L;

}
