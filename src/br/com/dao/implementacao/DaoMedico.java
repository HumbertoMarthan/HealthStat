package br.com.dao.implementacao;

import org.springframework.stereotype.Repository;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.projeto.model.Medico;
import br.com.repository.interfaces.RepositoryMedico;
@Repository
public class DaoMedico extends ImplementacaoCrud<Medico> implements RepositoryMedico{

	
	private static final long serialVersionUID = 1L;

}
