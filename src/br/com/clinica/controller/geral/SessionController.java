package br.com.clinica.controller.geral;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.servlet.http.HttpSession;

@ApplicationScoped
public interface SessionController extends Serializable {
	
	void addSession(String keyLoginUser, HttpSession httpSession);
	
	void invalidateSession(String keyLoginUser);
	
}
