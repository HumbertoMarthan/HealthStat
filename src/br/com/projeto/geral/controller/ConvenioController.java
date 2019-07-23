package br.com.projeto.geral.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.framework.inteface.crud.InterfaceCrud;
import br.com.projeto.model.Convenio;
import br.com.repository.interfaces.RepositoryConvenio;
import br.com.srv.interfaces.SrvConvenio;

@Controller
public class ConvenioController extends ImplementacaoCrud<Convenio> implements 
	InterfaceCrud<Convenio>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvConvenio srvConvenio;
	
	@Resource
	private RepositoryConvenio repositoryConvenio;
	
}
