package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.pessoa.Atendente;
import br.com.clinica.repository.interfaces.RepositoryAtendente;
@Repository
public class DaoAtendente extends ImplementacaoCrud<Atendente> implements RepositoryAtendente{

	
	private static final long serialVersionUID = 1L;

}
