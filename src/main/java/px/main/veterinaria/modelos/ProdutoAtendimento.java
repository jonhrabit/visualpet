package px.main.veterinaria.modelos;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "produtosatendimento")
public class ProdutoAtendimento {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String nome;

	private Integer idProdutoOriginal, quantidade, removido;
	private BigDecimal preco;

	@ManyToOne
	@JoinColumn(name = "atendimento_id")
	@JsonIgnore
	private Atendimento atendimento;

	public ProdutoAtendimento() {
		this.id = 0;
		this.quantidade = 1;
		this.removido=0;
		this.preco = new BigDecimal(0);
	}
	
	public void QuantidadeUp(Integer limite) {
		this.quantidade++;
		this.quantidade = limite;
	}

	public void QuantidadeUp(Integer v, Integer limite) {
		this.quantidade += v;
		if (this.quantidade > limite)
			this.quantidade = limite;
	}

	public void QuantidadeDown(Integer v) {
		this.quantidade -= v;
		if (this.quantidade < 0)
			this.quantidade = 0;
	}

	public void QuantidadeDown() {
		this.quantidade--;
		if (this.quantidade < 0)
			this.quantidade = 0;
	}
	public void setRemovido() {
		if(this.removido!=this.quantidade)
		this.removido=this.quantidade-this.removido;
	}

	@Override
	public String toString() {
		return "{id=" + id + ", nome=" + nome + ", idProdutoOriginal=" + idProdutoOriginal
				+ ", quantidade=" + quantidade + ", preco=" + preco + ", removido=" + removido + "}";
	}
}
