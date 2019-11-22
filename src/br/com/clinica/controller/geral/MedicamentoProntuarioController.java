package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.prontuario.MedicamentoProntuario;
import br.com.clinica.repository.interfaces.RepositoryMedicamentoProntuario;
import br.com.clinica.service.interfaces.SrvMedicamentoProntuario;

@Controller
public class MedicamentoProntuarioController extends ImplementacaoCrud<MedicamentoProntuario> implements InterfaceCrud<MedicamentoProntuario>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvMedicamentoProntuario srvMedicamentoProntuario;
	
	@Resource
	private RepositoryMedicamentoProntuario repositoryMedicamentoProntuario;
	
}
