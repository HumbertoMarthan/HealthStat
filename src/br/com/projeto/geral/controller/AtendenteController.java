package br.com.projeto.geral.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.framework.inteface.crud.InterfaceCrud;
import br.com.projeto.model.Atendente;
import br.com.repository.interfaces.RepositoryAtendente;
import br.com.srv.interfaces.SrvAtendente;

@Controller
public class AtendenteController extends ImplementacaoCrud<Atendente> implements 
	InterfaceCrud<Atendente>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvAtendente srvAtendente;
	
	@Resource
	private RepositoryAtendente repositoryAtendente;
	
	
	
}
