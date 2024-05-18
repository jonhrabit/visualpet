package px.main.veterinaria.query.modelos;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class RelatorioAtendimentosPorServico {
	List<String> titulo;
	List<List<AtendimentosPorServico>> lista;
	
	public RelatorioAtendimentosPorServico(){
		this.titulo = new ArrayList<String>();
		this.lista = new ArrayList<List<AtendimentosPorServico>>();
		
	}

}
