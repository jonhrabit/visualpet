package px.main.veterinaria.query.modelos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class AtendimentosPorCliente {
	String cliente, pet;
	Long atendimentos;
}
