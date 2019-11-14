package br.com.clinica.controller.geral;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.prontuario.Medicamento;
import br.com.clinica.repository.interfaces.RepositoryMedicamento;
import br.com.clinica.service.interfaces.SrvMedicamento;

@Controller
public class MedicamentoController extends ImplementacaoCrud<Medicamento> implements 
	InterfaceCrud<Medicamento>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvMedicamento srvMedicamento;
	
	@Resource
	private RepositoryMedicamento repositoryMedicamento;
	
}
