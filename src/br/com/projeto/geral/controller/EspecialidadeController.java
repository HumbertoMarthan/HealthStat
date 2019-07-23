package br.com.projeto.geral.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.framework.inteface.crud.InterfaceCrud;
import br.com.projeto.model.Especialidade;
import br.com.repository.interfaces.RepositoryEspecialidade;
import br.com.srv.interfaces.SrvEspecialidade;

@Controller
public class EspecialidadeController extends ImplementacaoCrud<Especialidade> implements 
	InterfaceCrud<Especialidade>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvEspecialidade srvAcompanhante;
	
	@Resource
	private RepositoryEspecialidade repositoryEspecialidade;
	
}
