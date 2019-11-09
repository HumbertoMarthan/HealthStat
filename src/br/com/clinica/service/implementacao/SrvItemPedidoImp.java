package br.com.clinica.service.implementacao;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import br.com.clinica.repository.interfaces.RepositoryItemPedido;
import br.com.clinica.service.interfaces.SrvItemPedido;

@Service
public class SrvItemPedidoImp implements SrvItemPedido{

	private static final long serialVersionUID = 1L;
	
	@Resource 
	private RepositoryItemPedido repositoryItemPedido;
}
