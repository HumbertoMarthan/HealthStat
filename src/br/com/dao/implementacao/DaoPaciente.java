package br.com.dao.implementacao;

import org.springframework.stereotype.Repository;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.projeto.model.Paciente;
import br.com.repository.interfaces.RepositoryPaciente;
@Repository
public class DaoPaciente extends ImplementacaoCrud<Paciente> implements RepositoryPaciente{

	
	private static final long serialVersionUID = 1L;

}
