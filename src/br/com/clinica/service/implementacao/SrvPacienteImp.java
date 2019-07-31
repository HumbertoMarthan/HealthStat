package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryPaciente;
import br.com.clinica.service.interfaces.SrvPaciente;

@Service
public class SrvPacienteImp implements SrvPaciente{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryPaciente repositoryPaciente;
}
