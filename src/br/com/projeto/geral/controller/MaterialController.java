package br.com.projeto.geral.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;


import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.framework.inteface.crud.InterfaceCrud;
import br.com.projeto.model.Material;
import br.com.repository.interfaces.RepositoryMaterial;
import br.com.srv.interfaces.SrvMaterial;

@Controller
public class MaterialController extends ImplementacaoCrud<Material> implements 
	InterfaceCrud<Material>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvMaterial srvMaterial;
	
	@Resource
	private RepositoryMaterial repositoryMaterial;
	
}
