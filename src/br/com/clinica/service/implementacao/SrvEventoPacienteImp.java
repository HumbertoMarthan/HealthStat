package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryEventoPaciente;
import br.com.clinica.service.interfaces.SrvEventoPaciente;

@Service
public class SrvEventoPacienteImp implements SrvEventoPaciente{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryEventoPaciente repositoryEventoPaciente;
}
