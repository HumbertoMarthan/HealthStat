package br.com.clinica.model.cadastro.agendamento;


	public enum TipoEvento {
	    /* TIPO DE EVENTO - CSS */
	    CONSULTA("Consulta", "consulta"),
	    RETORNO("Retorno", "retorno"),
	    CONFIRMA_CONSULTA("Confirmar", "confirmaConsulta");
	    

	    private final String descricao;
	    private final String css;

	    private TipoEvento(String descricao, String css) {
	        this.css = css;
	        this.descricao = descricao;
	    }

	    public String getCss() {
	        return css;
	    }

	    public String getDescricao() {
	        return descricao;
	    }
	    public String setCss(String css) {
	    	
	        return css;
	    }

	}

