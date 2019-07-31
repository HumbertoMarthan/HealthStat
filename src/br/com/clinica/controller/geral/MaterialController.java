package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.estoque.Material;
import br.com.clinica.repository.interfaces.RepositoryMaterial;
import br.com.clinica.service.interfaces.SrvMaterial;

@Controller
public class MaterialController extends ImplementacaoCrud<Material> implements 
	InterfaceCrud<Material>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvMaterial srvMaterial;
	
	@Resource
	private RepositoryMaterial repositoryMaterial;
	
}
