package br.com.projeto.bean.geral;

import java.io.Serializable;

public class LoginAtualizaSenha implements Serializable {

	private static final long serialVersionUID = 1L;
	private String senhaAtual;
	private String novaSenha;
	private String confirmaSenha;
	
	//GETTERS E SETTERS ---------------------------------
	public String getSenhaAtual() {
		return senhaAtual;
	}
	public void setSenhaAtual(String senhaAtual) {
		this.senhaAtual = senhaAtual;
	}
	public String getNovaSenha() {
		return novaSenha;
	}
	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}
	public String getConfirmaSenha() {
		return confirmaSenha;
	}
	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}
	
	
}
