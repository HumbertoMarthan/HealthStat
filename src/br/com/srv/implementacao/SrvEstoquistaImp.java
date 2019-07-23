package br.com.srv.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.repository.interfaces.RepositoryEstoquista;
import br.com.srv.interfaces.SrvEstoquista;

@Service
public class SrvEstoquistaImp implements SrvEstoquista{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryEstoquista repositoryEstoquista;
}
