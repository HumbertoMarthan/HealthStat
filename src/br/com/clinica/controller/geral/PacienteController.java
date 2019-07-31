package br.com.clinica.controller.geral;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.faces.model.SelectItem;

import org.springframework.stereotype.Controller;

import br.com.clinica.hibernate.ImplementacaoCrud;
import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.model.cadastro.pessoa.Paciente;
import br.com.clinica.repository.interfaces.RepositoryPaciente;
import br.com.clinica.service.interfaces.SrvPaciente;

@Controller
public class PacienteController extends ImplementacaoCrud<Paciente> implements 
	InterfaceCrud<Paciente>{

	private static final long serialVersionUID = 1L;
	
	@Resource
	private SrvPaciente srvPaciente ;
	
	@Resource
	private RepositoryPaciente repositoryPaciente;
	
	public List<SelectItem> getListPacientes() throws Exception {
		List<SelectItem> list = new ArrayList<SelectItem>();

		List<Paciente> pacientes = super.findListByQueryDinamica(" from Paciente");

		for (Paciente paciente : pacientes) {
			list.add(new SelectItem(paciente, paciente.getPessoa().getPessoaNome()));
		}
		return list;
	}
	
	public List<Paciente>  completePaciente(String q) throws Exception {
		return super.
				findListByQueryDinamica(" from Paciente where pessoa.pessoaNome like '%" + q.toUpperCase()  + "%' order by pessoa.pessoaNome ASC");
	
	}
	
	public List<Paciente> listaPacientes() throws Exception{
		return super.findListByQueryDinamica(" from Paciente");
	}
	
	
}
