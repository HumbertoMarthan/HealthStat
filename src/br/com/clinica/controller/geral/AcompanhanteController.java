package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.pessoa.Acompanhante;
import br.com.clinica.repository.interfaces.RepositoryAcompanhante;
import br.com.clinica.service.interfaces.SrvAcompanhante;

@Controller
public class AcompanhanteController extends ImplementacaoCrud<Acompanhante> implements 
	InterfaceCrud<Acompanhante>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvAcompanhante srvAcompanhante;
	
	@Resource
	private RepositoryAcompanhante repositoryAcompanhante;
	
}
