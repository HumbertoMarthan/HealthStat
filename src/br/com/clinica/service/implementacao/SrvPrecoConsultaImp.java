package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryPrecoConsulta;
import br.com.clinica.service.interfaces.SrvPrecoConsulta;

@Service
public class SrvPrecoConsultaImp implements SrvPrecoConsulta{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryPrecoConsulta repositoryPrecoConsulta; 
}
