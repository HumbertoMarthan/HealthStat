package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.pessoa.Estoquista;
import br.com.clinica.repository.interfaces.RepositoryEstoquista;
import br.com.clinica.service.interfaces.SrvEstoquista;

@Controller
public class EstoquistaController extends ImplementacaoCrud<Estoquista> implements 
	InterfaceCrud<Estoquista>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvEstoquista srvEstoquista   ;
	
	@Resource
	private RepositoryEstoquista repositoryEstoquista  ;
	
	
	
}
