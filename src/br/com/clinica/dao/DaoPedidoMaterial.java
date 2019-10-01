package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.estoque.PedidoMaterial;
import br.com.clinica.repository.interfaces.RepositoryPedidoMaterial;
@Repository
public class DaoPedidoMaterial extends ImplementacaoCrud<PedidoMaterial> implements RepositoryPedidoMaterial{

	
	private static final long serialVersionUID = 1L;

}
