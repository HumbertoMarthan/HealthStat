package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.estoque.Estoque;
import br.com.clinica.repository.interfaces.RepositoryEstoque;
import br.com.clinica.service.interfaces.SrvEstoque;

@Controller
public class EstoqueController extends ImplementacaoCrud<Estoque> implements 
	InterfaceCrud<Estoque>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvEstoque srvEstoque   ;
	
	@Resource
	private RepositoryEstoque repositoryEstoque  ;
	
	
	
}
