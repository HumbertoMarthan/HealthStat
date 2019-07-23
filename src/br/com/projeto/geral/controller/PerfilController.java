package br.com.projeto.geral.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.framework.inteface.crud.InterfaceCrud;
import br.com.projeto.model.Perfil;
import br.com.repository.interfaces.RepositoryPerfil;
import br.com.srv.interfaces.SrvPerfil;

@Controller
public class PerfilController extends ImplementacaoCrud<Perfil> implements 
	InterfaceCrud<Perfil>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvPerfil srvPerfil;
	
	@Resource
	private RepositoryPerfil repositoryPerfil;
	
}
