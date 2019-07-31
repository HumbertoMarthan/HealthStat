package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.pessoa.Pessoa;
import br.com.clinica.repository.interfaces.RepositoryPessoa;
@Repository
public class DaoPessoa extends ImplementacaoCrud<Pessoa> implements RepositoryPessoa{

	
	private static final long serialVersionUID = 1L;

}
