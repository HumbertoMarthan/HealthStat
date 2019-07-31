package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryProntuario;
import br.com.clinica.service.interfaces.SrvProntuario;

@Service
public class SrvProntuarioImp implements SrvProntuario{ 

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryProntuario repositoryProntuario;
}
