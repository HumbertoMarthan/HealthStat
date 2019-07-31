package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.pessoa.Medico;
import br.com.clinica.repository.interfaces.RepositoryMedico;
@Repository
public class DaoMedico extends ImplementacaoCrud<Medico> implements RepositoryMedico{

	
	private static final long serialVersionUID = 1L;

}
