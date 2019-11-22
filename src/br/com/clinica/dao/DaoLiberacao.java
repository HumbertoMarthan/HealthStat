package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.usuario.Liberacao;
import br.com.clinica.repository.interfaces.RepositoryLiberacao;
@Repository
public class DaoLiberacao extends ImplementacaoCrud<Liberacao> implements RepositoryLiberacao{
	
	private static final long serialVersionUID = 1L;

}
