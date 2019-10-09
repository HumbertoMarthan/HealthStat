package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryPaciente;
import br.com.clinica.service.interfaces.SrvPagamentoEspecial;

@Service
public class SrvPagamentoEspecialImp implements SrvPagamentoEspecial{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryPaciente repositoryPaciente;
}
