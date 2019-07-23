package br.com.srv.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.repository.interfaces.RepositoryUnidadeDeMedida;
import br.com.srv.interfaces.SrvUnidadeDeMedida;

@Service
public class SrvUnidadeDeMedidaImp implements SrvUnidadeDeMedida{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryUnidadeDeMedida repositoryUnidadeDeMedida;
}
