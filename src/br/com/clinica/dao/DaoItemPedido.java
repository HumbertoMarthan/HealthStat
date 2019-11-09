package br.com.clinica.dao;

import org.springframework.stereotype.Repository;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.model.cadastro.estoque.ItemPedido;
import br.com.clinica.repository.interfaces.RepositoryItemPedido;
@Repository
public class DaoItemPedido extends ImplementacaoCrud<ItemPedido> implements RepositoryItemPedido{

	
	private static final long serialVersionUID = 1L;

}
