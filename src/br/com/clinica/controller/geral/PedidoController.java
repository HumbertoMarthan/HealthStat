package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.estoque.Pedido;
import br.com.clinica.repository.interfaces.RepositoryPedido;
import br.com.clinica.service.interfaces.SrvPedido;

@Controller
public class PedidoController extends ImplementacaoCrud<Pedido> implements InterfaceCrud<Pedido>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvPedido srvPedido ;
	
	@Resource
	private RepositoryPedido repositoryPedido;
}
