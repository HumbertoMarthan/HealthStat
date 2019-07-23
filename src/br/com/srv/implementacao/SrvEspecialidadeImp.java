package br.com.srv.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.repository.interfaces.RepositoryEspecialidade;
import br.com.srv.interfaces.SrvEspecialidade;

@Service
public class SrvEspecialidadeImp implements SrvEspecialidade{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryEspecialidade repositoryEspecialidade;
}
