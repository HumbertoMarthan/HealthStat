package br.com.clinica.controller.geral;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.agendamento.EventoPaciente;
import br.com.clinica.repository.interfaces.RepositoryEventoPaciente;
import br.com.clinica.service.interfaces.SrvEventoPaciente;

@Controller
public class EventoPacienteController extends ImplementacaoCrud<EventoPaciente> implements 
InterfaceCrud<EventoPaciente> {
	
	private static final long serialVersionUID = 1L;

	@Resource
	private SrvEventoPaciente srvEventoPaciente;
	
	@Resource
	private RepositoryEventoPaciente repositoryEventoPaciente;
	
	//Buscando no Banco de dados a lista de especialidades
	
	public List<EventoPaciente> listarEventos() throws Exception{
		return super.findListByQueryDinamica(" from EventoPaciente ");
	}
	
}
