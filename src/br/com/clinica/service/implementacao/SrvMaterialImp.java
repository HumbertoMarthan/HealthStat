package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryMaterial;
import br.com.clinica.service.interfaces.SrvMaterial;

@Service
public class SrvMaterialImp implements SrvMaterial{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryMaterial repositoryMaterial; 
}
