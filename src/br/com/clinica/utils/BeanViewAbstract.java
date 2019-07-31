package br.com.clinica.utils;

public abstract class BeanViewAbstract implements ActionViewPadrao {

	

	private static final long serialVersionUID = 1L;

	@Override
	public void limpaLista() throws Exception {

	}

	@Override
	public String save() throws Exception {
		return null;
	}
	

	@Override
	public void saveNotReturn() throws Exception {

	}

	@Override
	public void saveEdit() throws Exception {

	}

	@Override
	public void excluir() throws Exception {

	}

	@Override
	public String ativar() throws Exception {
		return null;
	}

	@Override
	public String novo() throws Exception {
		return null;
	}

	@Override
	public String editar() throws Exception {
		return null;
	}

	@Override
	public void setarVariaveisNulas() throws Exception {

	}

	@Override
	public void consultarEntidade() throws Exception {

	}

	@Override
	public void StatusOperation(EstatusPersistencia a) throws Exception {
			Mensagens.responseOperation(a);
	}

	@Override
	public String redirecionarNewEntidade() throws Exception {
		return null;

	}

	@Override
	public String redirecionarFindEntidade() throws Exception {
		return null;

	}
	
	protected void sucesso() throws Exception{
		StatusOperation((EstatusPersistencia.SUCESSO));
	}
	
	protected void error() throws Exception{
		StatusOperation((EstatusPersistencia.ERRO));
	}
	

	@Override
	public void addMsg(String msg) throws Exception {
		Mensagens.msg(msg);
	}

}
