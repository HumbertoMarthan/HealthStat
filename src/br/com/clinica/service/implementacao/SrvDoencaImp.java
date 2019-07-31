package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryDoenca;
import br.com.clinica.service.interfaces.SrvDoenca;

@Service
public class SrvDoencaImp implements SrvDoenca{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryDoenca repositoryDoenca;
}
