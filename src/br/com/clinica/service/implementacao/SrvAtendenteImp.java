package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryAtendente;
import br.com.clinica.service.interfaces.SrvAtendente;

@Service
public class SrvAtendenteImp implements SrvAtendente{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem inje��o de dependencia do repositorio
	
	
	@Resource 
	private RepositoryAtendente repositoryAtendente;
}
