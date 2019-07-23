package br.com.dao.implementacao;

import org.springframework.stereotype.Repository;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.projeto.model.Perfil;
import br.com.repository.interfaces.RepositoryPerfil;
@Repository
public class DaoPerfil extends ImplementacaoCrud<Perfil> implements RepositoryPerfil{

	
	private static final long serialVersionUID = 1L;

}
