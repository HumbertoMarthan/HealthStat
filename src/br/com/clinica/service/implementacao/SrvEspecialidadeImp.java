package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryEspecialidade;
import br.com.clinica.service.interfaces.SrvEspecialidade;

@Service
public class SrvEspecialidadeImp implements SrvEspecialidade{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem inje��o de dependencia do repositorio
	
	
	@Resource 
	private RepositoryEspecialidade repositoryEspecialidade;
}
