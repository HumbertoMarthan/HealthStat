package br.com.srv.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.repository.interfaces.RepositoryFornecedor;
import br.com.srv.interfaces.SrvFornecedor;

@Service
public class SrvFornecedorImp implements SrvFornecedor{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryFornecedor repositoryFornecedor ;
}
