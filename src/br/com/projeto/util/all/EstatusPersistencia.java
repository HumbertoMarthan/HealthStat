package br.com.projeto.util.all;

public enum EstatusPersistencia {
	
	
	ERRO("Erro"), SUCESSO("Sucesso"),
	OBJETO_REFERENCIADO("Esse objeto não pode ser apagado por possuir referencias ao mesmo tempo");
	
	
	private String name;
	
	private EstatusPersistencia(String s) {
		this.name = s;
	}
	
	@Override
	public String toString() {
		
		return this.name;
	}
}
