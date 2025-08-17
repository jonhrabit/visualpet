package px.main.veterinaria.view;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PacoteView {

	BigDecimal valor;
	Integer cliente_id, tamanho;
	String descricao;
	List<AtendimentoView> atendimentos;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	private class AtendimentoView {
		@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
		private Date agenda;
		private Integer servico, pet;
	}

}
