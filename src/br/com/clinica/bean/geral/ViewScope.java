package br.com.clinica.bean.geral;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.context.request.FacesRequestAttributes;

public class ViewScope implements Scope, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String VIEW_SCOPE_CALLBACKS = "viewscope.callbacks";
	
	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		Object instance = getViewMap().get(name);
		if (instance == null) {
			instance = objectFactory.getObject();
			getViewMap().put(name, instance);
		
		}
		
		return instance;
	}

	@Override
	public String getConversationId() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);
		return facesRequestAttributes.getSessionId()+ " - "+ facesContext.getViewRoot().getViewId();
	}

	@Override
	public void registerDestructionCallback(String name, Runnable runnable) {

		@SuppressWarnings("unchecked")
		Map<String, Runnable> callBacks = (Map<String, Runnable>) getViewMap().get(VIEW_SCOPE_CALLBACKS);

			if(callBacks != null){
				callBacks.put(VIEW_SCOPE_CALLBACKS , runnable);
			}	
		
	}

	@Override
	public Object remove(String name) {
		Object instance = getViewMap().remove(name);
		if(instance != null) {
		
			@SuppressWarnings("unchecked")
			Map<String, Runnable> callBacks = (Map<String, Runnable>) getViewMap().get(VIEW_SCOPE_CALLBACKS);
			if(callBacks != null){
				callBacks.remove(name);
			}
		}
		return instance;
	}

	@Override
	public Object resolveContextualObject(String name) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);
		return facesRequestAttributes.resolveReference(name);
	}
	// Uma lista onde tem um nome e um valor , retorna os dados em forma de lista
	private Map<String, Object> getViewMap(){
		return FacesContext.getCurrentInstance() != null ? 
				FacesContext.getCurrentInstance().getViewRoot().getViewMap() : new HashMap<String, Object>();
	}

}
