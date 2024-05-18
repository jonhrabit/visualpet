package px.main.veterinaria.relatorio;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Linha {
	String nome, grupo;
	List<BigDecimal> valores; // 12 valores do ano/ um para cada mês

	public Linha(String nome, String grupo) {
		this.nome = nome;
		this.grupo = grupo;
	}

	public void addValor(BigDecimal[] valores) {
		for (BigDecimal v : valores) {
			this.valores.add(v);
		}

	}

}
