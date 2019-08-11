package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryContasReceber;
import br.com.clinica.service.interfaces.SrvContasReceber;

@Service
public class SrvContasReceberImp implements SrvContasReceber{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryContasReceber repositoryContasReceber;
}
