package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryPessoa;
import br.com.clinica.service.interfaces.SrvPessoa;

@Service
public class SrvPessoaImp implements SrvPessoa{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryPessoa repositoryPessoa;
}
