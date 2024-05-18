package px.main.veterinaria.modelos.enumeracoes;

public enum ServicoTipo {
	
	ESTETICA(0, "Estética"), 
	CLINICA(1, "Clínica"), 
	CUIDADOR(2, "Cuidador"), 
	HOSPEDAGEM(3, "Hospetagem");

	private String descricao;
	private Integer id;

	ServicoTipo(Integer id, String descricao) {
		this.id = id;
		this.descricao = descricao;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public Integer getId() {
		return id;
	}

	public static ServicoTipo get(Integer id) {
		for (ServicoTipo servicoTipo : ServicoTipo.values()) {
			if (servicoTipo.id == id)
				return servicoTipo;
		}
		return ServicoTipo.ESTETICA;
	}
}
