package br.com.srv.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.repository.interfaces.RepositoryTipoConsulta;
import br.com.srv.interfaces.SrvTipoConsulta;

@Service
public class SrvTipoConsultaImp implements SrvTipoConsulta{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryTipoConsulta repositoryTipoConsulta;
}
