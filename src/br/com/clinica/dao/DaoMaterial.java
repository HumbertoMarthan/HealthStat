package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import com.sun.prism.Material;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.repository.interfaces.RepositoryMaterial;
@Repository
public class DaoMaterial extends ImplementacaoCrud<Material> implements RepositoryMaterial{
	
	private static final long serialVersionUID = 1L;

}
