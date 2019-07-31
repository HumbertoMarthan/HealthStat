package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryAcompanhante;
import br.com.clinica.service.interfaces.SrvAcompanhante;

@Service
public class SrvAcompanhanteImp implements SrvAcompanhante{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryAcompanhante repositoryAcompanhante;
}
