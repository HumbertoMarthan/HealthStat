package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.estoque.ItemPedido;
import br.com.clinica.repository.interfaces.RepositoryItemPedido;
import br.com.clinica.service.interfaces.SrvItemPedido;

@Controller
public class ItemPedidoController extends ImplementacaoCrud<ItemPedido> implements 
	InterfaceCrud<ItemPedido>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvItemPedido srvItemPedido;
	
	@Resource
	private RepositoryItemPedido repositoryItemPedido;
}
