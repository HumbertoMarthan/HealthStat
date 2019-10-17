package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.outro.PrecoConsulta;
import br.com.clinica.repository.interfaces.RepositoryPrecoConsulta;
@Repository
public class DaoPrecoConsulta extends ImplementacaoCrud<PrecoConsulta> implements RepositoryPrecoConsulta{

	
	private static final long serialVersionUID = 1L;

}
