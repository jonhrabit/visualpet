package px.main.veterinaria.modelos.enumeracoes;

public enum SituacaoAtendimento {
	AGENDA(0, "Agenda"),
	ATENDIMENTO(1, "Em Andamento"),
	FINALIZADO(2, "Finalizado"),
	CANCELADO(3, "Cancelado"),
	PENDENTE(4, "Pendente");
	
private String descricao;
private Integer id;
	
	SituacaoAtendimento(Integer id, String descricao) {
		this.id=id;
		this.descricao=descricao;
	}
	public String getDescricao() {
		return this.descricao;
	}
	public Integer getId() {
		return id;
	}
	public SituacaoAtendimento get(Integer id) {
		return this;
	}
	
}
