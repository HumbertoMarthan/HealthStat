package br.com.dao.implementacao;

import org.springframework.stereotype.Repository;

import com.sun.prism.Material;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.repository.interfaces.RepositoryMaterial;
@Repository
public class DaoMaterial extends ImplementacaoCrud<Material> implements RepositoryMaterial{
	
	private static final long serialVersionUID = 1L;

}
