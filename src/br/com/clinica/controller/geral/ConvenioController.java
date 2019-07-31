package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.outro.Convenio;
import br.com.clinica.repository.interfaces.RepositoryConvenio;
import br.com.clinica.service.interfaces.SrvConvenio;

@Controller
public class ConvenioController extends ImplementacaoCrud<Convenio> implements 
	InterfaceCrud<Convenio>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvConvenio srvConvenio;
	
	@Resource
	private RepositoryConvenio repositoryConvenio;
	
}
