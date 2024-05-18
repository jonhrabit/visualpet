package px.main.veterinaria.relatorio;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Relatorio {
	String nome;
	List<Linha> linhas;

	public Relatorio(String nome) {
		this.nome = nome;
		this.linhas = new ArrayList<Linha>();
	}

	public void addLinha(Linha[] linhas) {
		if (linhas != null)
			for (Linha l : linhas) {
				this.linhas.add(l);
			}
	}

	public void addLinha(Linha linha) {
		if (linha != null)
			this.linhas.add(linha);
	}

	public void addLinha(List<Linha> linhas) {
		if (linhas != null)
			linhas.stream().forEach(linha -> this.addLinha(linha));
	}

}
