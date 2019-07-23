package br.com.srv.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.repository.interfaces.RepositoryProntuario;
import br.com.srv.interfaces.SrvProntuario;

@Service
public class SrvProntuarioImp implements SrvProntuario{ 

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryProntuario repositoryProntuario;
}
