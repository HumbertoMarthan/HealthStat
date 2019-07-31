package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.outro.Doenca;
import br.com.clinica.repository.interfaces.RepositoryDoenca;
import br.com.clinica.service.interfaces.SrvDoenca;

@Controller
public class DoencaController extends ImplementacaoCrud<Doenca> implements 
	InterfaceCrud<Doenca>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvDoenca srvDoenca;
	
	@Resource
	private RepositoryDoenca repositoryDoenca;
	
}
