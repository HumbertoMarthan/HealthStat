package br.com.srv.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.repository.interfaces.RepositoryAcompanhante;
import br.com.srv.interfaces.SrvAcompanhante;

@Service
public class SrvAcompanhanteImp implements SrvAcompanhante{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem inje��o de dependencia do repositorio
	
	
	@Resource 
	private RepositoryAcompanhante repositoryAcompanhante;
}
