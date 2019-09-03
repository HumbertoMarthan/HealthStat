package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.agendamento.EventoPaciente;
import br.com.clinica.repository.interfaces.RepositoryEventoPaciente;
@Repository
public class DaoEventoPaciente extends ImplementacaoCrud<EventoPaciente> implements RepositoryEventoPaciente{

	
	private static final long serialVersionUID = 1L;

}
