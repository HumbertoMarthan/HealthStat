package br.com.projeto.geral.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.framework.inteface.crud.InterfaceCrud;
import br.com.projeto.model.Estoquista;
import br.com.repository.interfaces.RepositoryEstoquista;
import br.com.srv.interfaces.SrvEstoquista;

@Controller
public class EstoquistaController extends ImplementacaoCrud<Estoquista> implements 
	InterfaceCrud<Estoquista>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvEstoquista srvEstoquista   ;
	
	@Resource
	private RepositoryEstoquista repositoryEstoquista  ;
	
	
	
}
