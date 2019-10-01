package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryPedido;
import br.com.clinica.service.interfaces.SrvPedido;

@Service
public class SrvPedidoImp implements SrvPedido{

	private static final long serialVersionUID = 1L;
	
	@Resource 
	private RepositoryPedido repositoryPedido;
}
