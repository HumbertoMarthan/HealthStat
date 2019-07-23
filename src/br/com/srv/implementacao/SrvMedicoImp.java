package br.com.srv.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.repository.interfaces.RepositoryMedico;
import br.com.srv.interfaces.SrvMedico;

@Service
public class SrvMedicoImp implements SrvMedico{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryMedico repositoryMedico;
}
