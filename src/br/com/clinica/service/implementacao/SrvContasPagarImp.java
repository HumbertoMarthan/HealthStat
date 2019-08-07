package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryContasPagar;
import br.com.clinica.service.interfaces.SrvContasPagar;

@Service
public class SrvContasPagarImp implements SrvContasPagar{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	@Resource 
	private RepositoryContasPagar repositoryContasPagar; 
}
