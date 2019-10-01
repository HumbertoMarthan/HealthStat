package br.com.clinica.converters;

import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "entityConverter")
public class EntityConverter implements Converter {

	public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
		if (value != null) {
			return this.getAttributesFrom(component).get(value);
		}
		return null;
	}

	public String getAsString(FacesContext ctx, UIComponent component, Object value) {

		if (value != null
				&& !"".equals(value)) {

			EntityBase entity = (EntityBase) value;
			try {
			// adiciona item como atributo do componente
			this.addAttribute(component, entity);
			}catch (Exception e) {
				System.out.println("erro ao converter");
			}

			Long codigo = entity.getId();
			if (codigo != null) {
				return String.valueOf(codigo);
			}
		}

		return (String) value;
	}

	protected void addAttribute(UIComponent component, EntityBase o) {
		try {
		String key = o.getId().toString(); // codigo da empresa como chave neste caso
		this.getAttributesFrom(component).put(key, o);
		}catch (Exception e) {
			System.out.println("Erro ao converter add Atribute");
		}
		}

	protected Map<String, Object> getAttributesFrom(UIComponent component) {
		return component.getAttributes();
	}

}