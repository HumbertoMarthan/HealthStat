package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.estoque.Pedido;
import br.com.clinica.repository.interfaces.RepositoryPedido;
@Repository
public class DaoPedido extends ImplementacaoCrud<Pedido> implements RepositoryPedido{

	
	private static final long serialVersionUID = 1L;

}
