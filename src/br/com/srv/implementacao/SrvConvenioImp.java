package br.com.srv.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.repository.interfaces.RepositoryConvenio;
import br.com.srv.interfaces.SrvConvenio;

@Service
public class SrvConvenioImp implements SrvConvenio{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryConvenio repositoryConvenio;
}
