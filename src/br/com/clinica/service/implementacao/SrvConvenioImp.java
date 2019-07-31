package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryConvenio;
import br.com.clinica.service.interfaces.SrvConvenio;

@Service
public class SrvConvenioImp implements SrvConvenio{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryConvenio repositoryConvenio;
}
