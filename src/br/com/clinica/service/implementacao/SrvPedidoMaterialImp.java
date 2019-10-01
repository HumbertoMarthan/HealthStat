package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryPedidoMaterial;
import br.com.clinica.service.interfaces.SrvPedidoMaterial;

@Service
public class SrvPedidoMaterialImp implements SrvPedidoMaterial{

	private static final long serialVersionUID = 1L;
	//tdo Imp tem injeção de dependencia do repositorio
	
	
	@Resource 
	private RepositoryPedidoMaterial repositoryPedidoMaterial;
}
