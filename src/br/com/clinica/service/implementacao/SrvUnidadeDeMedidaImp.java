package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryUnidadeDeMedida;
import br.com.clinica.service.interfaces.SrvUnidadeDeMedida;

@Service
public class SrvUnidadeDeMedidaImp implements SrvUnidadeDeMedida{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem inje��o de dependencia do repositorio
	
	
	@Resource 
	private RepositoryUnidadeDeMedida repositoryUnidadeDeMedida;
}
