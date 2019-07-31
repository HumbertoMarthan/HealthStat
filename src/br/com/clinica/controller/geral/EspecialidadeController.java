package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.outro.Especialidade;
import br.com.clinica.repository.interfaces.RepositoryEspecialidade;
import br.com.clinica.service.interfaces.SrvEspecialidade;

@Controller
public class EspecialidadeController extends ImplementacaoCrud<Especialidade> implements 
	InterfaceCrud<Especialidade>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvEspecialidade srvAcompanhante;
	
	@Resource
	private RepositoryEspecialidade repositoryEspecialidade;
	
}
