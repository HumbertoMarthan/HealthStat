package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.pessoa.Acompanhante;
import br.com.clinica.repository.interfaces.RepositoryAcompanhante;
@Repository
public class DaoAcompanhante 
extends ImplementacaoCrud<Acompanhante> implements RepositoryAcompanhante{
	private static final long serialVersionUID = 1L;
}
