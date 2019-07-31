package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.pessoa.Paciente;
import br.com.clinica.repository.interfaces.RepositoryPaciente;
@Repository
public class DaoPaciente extends ImplementacaoCrud<Paciente> implements RepositoryPaciente{

	
	private static final long serialVersionUID = 1L;

}
