package br.com.clinica.utils;

public abstract class BeanViewAbstract implements ActionViewPadrao {

	

	private static final long serialVersionUID = 1L;

	@Override
	public void limpaLista() {

	}

	@Override
	public String save() {
		return null;
	}
	

	@Override
	public void saveNotReturn() {

	}

	@Override
	public void saveEdit() {

	}

	@Override
	public void excluir() {

	}

	@Override
	public String ativar() {
		return null;
	}

	@Override
	public String novo() {
		return null;
	}

	@Override
	public String editar() {
		return null;
	}

	@Override
	public void setarVariaveisNulas() {

	}

	@Override
	public void consultarEntidade() {

	}

	@Override
	public void StatusOperation(EstatusPersistencia a) {
			Mensagens.responseOperation(a);
	}

	@Override
	public String redirecionarNewEntidade() {
		return null;

	}

	@Override
	public String redirecionarFindEntidade() {
		return null;

	}
	
	protected void sucesso(){
		try {
			StatusOperation((EstatusPersistencia.SUCESSO));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void error(){
		try {
		StatusOperation((EstatusPersistencia.ERRO));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public void addMsg(String msg) {
		try {
		Mensagens.msg(msg);
	}catch (Exception e) {
		e.printStackTrace();
		e.getMessage();
	}
	}

}
