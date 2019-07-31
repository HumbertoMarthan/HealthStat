package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.pessoa.Atendente;
import br.com.clinica.repository.interfaces.RepositoryAtendente;
import br.com.clinica.service.interfaces.SrvAtendente;

@Controller
public class AtendenteController extends ImplementacaoCrud<Atendente> implements 
	InterfaceCrud<Atendente>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvAtendente srvAtendente;
	
	@Resource
	private RepositoryAtendente repositoryAtendente;
	
	
	
}
