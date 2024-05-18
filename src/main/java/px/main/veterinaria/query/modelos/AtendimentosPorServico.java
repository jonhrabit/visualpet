package px.main.veterinaria.query.modelos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtendimentosPorServico {
	Long quantidade;
	String servico;
}
