package br.com.clinica.bean.geral;

import org.springframework.stereotype.Component;

import br.com.clinica.hibernate.InterfaceCrud;
import br.com.clinica.relatorio.BeanReportView;

@Component
public abstract class BeanManagedViewAbstract extends BeanReportView {

	private static final long serialVersionUID = 1L;

	protected abstract Class<?> getClassImp();

	protected abstract InterfaceCrud<?> getController();

}