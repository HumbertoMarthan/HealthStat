package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.outro.Convenio;
import br.com.clinica.repository.interfaces.RepositoryConvenio;
@Repository
public class DaoConvenio extends ImplementacaoCrud<Convenio> 
implements RepositoryConvenio {

	
	private static final long serialVersionUID = 1L;

}
