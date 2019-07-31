package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.pessoa.Pessoa;
import br.com.clinica.repository.interfaces.RepositoryPessoa;
import br.com.clinica.service.interfaces.SrvPessoa;

@Controller
public class PessoaController extends ImplementacaoCrud<Pessoa> implements 
	InterfaceCrud<Pessoa>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvPessoa srvPessoa ;
	
	@Resource
	private RepositoryPessoa repositoryPessoa;
	
}
