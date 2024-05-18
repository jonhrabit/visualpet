package px.main.veterinaria.view;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import px.main.veterinaria.modelos.Servico;
import px.main.veterinaria.modelos.enumeracoes.ServicoTipo;

@Data
public class AtendimentoListaView {
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private Date data;
	private String pet, tutor, celular;
	private Servico servico;
	private Integer idTutor,idTipo;
	private BigDecimal valor;
	private ServicoTipo[] servicoTipo;

	public AtendimentoListaView() {
		this.data = new Date();
		this.valor = new BigDecimal(0);
		this.idTutor=0;
		this.idTipo=99;
		servicoTipo=ServicoTipo.values();
	}


}
