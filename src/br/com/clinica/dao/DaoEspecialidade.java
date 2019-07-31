package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.outro.Especialidade;
import br.com.clinica.repository.interfaces.RepositoryEspecialidade;
@Repository
public class DaoEspecialidade 
extends ImplementacaoCrud<Especialidade> implements RepositoryEspecialidade{
	private static final long serialVersionUID = 1L;
}
