package br.com.projeto.geral.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.framework.inteface.crud.InterfaceCrud;
import br.com.projeto.model.Doenca;
import br.com.repository.interfaces.RepositoryDoenca;
import br.com.srv.interfaces.SrvDoenca;

@Controller
public class DoencaController extends ImplementacaoCrud<Doenca> implements 
	InterfaceCrud<Doenca>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvDoenca srvDoenca;
	
	@Resource
	private RepositoryDoenca repositoryDoenca;
	
}
