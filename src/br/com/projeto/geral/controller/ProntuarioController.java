package br.com.projeto.geral.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.framework.inteface.crud.InterfaceCrud;
import br.com.projeto.model.Prontuario;
import br.com.repository.interfaces.RepositoryProntuario;
import br.com.srv.interfaces.SrvProntuario;

@Controller
public class ProntuarioController extends ImplementacaoCrud<Prontuario> implements 
	InterfaceCrud<Prontuario>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvProntuario srvProntuario;
	
	@Resource
	private RepositoryProntuario repositoryProntuario;
	
	
	
}
