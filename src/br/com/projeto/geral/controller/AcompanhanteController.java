package br.com.projeto.geral.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.framework.inteface.crud.InterfaceCrud;
import br.com.projeto.model.Acompanhante;
import br.com.repository.interfaces.RepositoryAcompanhante;
import br.com.srv.interfaces.SrvAcompanhante;

@Controller
public class AcompanhanteController extends ImplementacaoCrud<Acompanhante> implements 
	InterfaceCrud<Acompanhante>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvAcompanhante srvAcompanhante;
	
	@Resource
	private RepositoryAcompanhante repositoryAcompanhante;
	
}
