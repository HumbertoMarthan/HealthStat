package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.agendamento.Evento;
import br.com.clinica.repository.interfaces.RepositoryEvento;
@Repository
public class DaoEvento extends ImplementacaoCrud<Evento> implements RepositoryEvento{

	
	private static final long serialVersionUID = 1L;

}
