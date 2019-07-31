package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.prontuario.Prontuario;
import br.com.clinica.repository.interfaces.RepositoryProntuario;
@Repository
public class DaoProntuario extends ImplementacaoCrud<Prontuario> implements RepositoryProntuario{

	
	private static final long serialVersionUID = 1L;

}
