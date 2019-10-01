package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryEstoque;
import br.com.clinica.service.interfaces.SrvEstoque;

@Service
public class SrvEstoqueImp implements SrvEstoque{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryEstoque repositoryEstoque;
}
