package px.main.veterinaria.view;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import px.main.veterinaria.modelos.ProdutoAtendimento;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AtendimentoProdutosView {
	private Integer id;
	private List<ProdutoAtendimento> lista;

	public boolean contem(Integer idProdutoOriginal) {
		for (ProdutoAtendimento prod : this.lista) {
			if (prod.getIdProdutoOriginal().equals(idProdutoOriginal))
				return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "{id=" + id + ", lista=" + lista + "}";
	}
	
}
