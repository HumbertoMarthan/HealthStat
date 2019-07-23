package br.com.projeto.geral.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.framework.implementacao.crud.ImplementacaoCrud;
import br.com.framework.inteface.crud.InterfaceCrud;
import br.com.projeto.model.Pessoa;
import br.com.repository.interfaces.RepositoryPessoa;
import br.com.srv.interfaces.SrvPessoa;

@Controller
public class PessoaController extends ImplementacaoCrud<Pessoa> implements 
	InterfaceCrud<Pessoa>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvPessoa srvPessoa ;
	
	@Resource
	private RepositoryPessoa repositoryPessoa;
	
}
