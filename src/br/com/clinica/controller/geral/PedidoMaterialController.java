package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.estoque.PedidoMaterial;
import br.com.clinica.repository.interfaces.RepositoryPedidoMaterial;
import br.com.clinica.service.interfaces.SrvPedidoMaterial;

@Controller
public class PedidoMaterialController extends ImplementacaoCrud<PedidoMaterial> implements 
	InterfaceCrud<PedidoMaterial>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvPedidoMaterial srvPedidoMaterial ;
	
	@Resource
	private RepositoryPedidoMaterial repositoryPedidoMaterial;
}
