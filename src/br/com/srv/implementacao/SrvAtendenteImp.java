package br.com.srv.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.repository.interfaces.RepositoryAtendente;
import br.com.srv.interfaces.SrvAtendente;

@Service
public class SrvAtendenteImp implements SrvAtendente{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryAtendente repositoryAtendente;
}
