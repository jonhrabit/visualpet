package px.main.veterinaria.modelos;

import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
@Table(name = "cestas")
public class Cesta {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	private String tipo, nf;

	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "cesta", orphanRemoval = true)
	private List<ProdutoCesta> produtos;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cesta")
	private List<Caixa> caixas;

	public Cesta() {
		this.cliente = new Cliente();
		this.tipo = "comum";
		this.data = new Date();
		this.produtos = new ArrayList<ProdutoCesta>();
		this.caixas = new ArrayList<Caixa>();
	}

	@Override
	public String toString() {
		return "Cesta [id=" + id + ", cliente=" + cliente + ", tipo=" + tipo + ", data=" + data + ", produtos="
				+ produtos + "]";
	}

	public BigDecimal getTotal() {
		BigDecimal total = new BigDecimal(0);
		for (ProdutoCesta produtoCesta : this.produtos) {
			total = total.add(produtoCesta.getPrecoVenda().multiply(new BigDecimal(produtoCesta.getQuantidade())));
		}
		return total;
	}

	public boolean contemProduto(Integer id) {
		for (ProdutoCesta produtoCesta : this.getProdutos()) {
			if (produtoCesta.compareId(id))
				return true;
		}
		return false;
	}

	public boolean produtoDown(Integer id, Integer quantidade) {
		for (ProdutoCesta produtoCesta : this.getProdutos()) {
			if (produtoCesta.compareId(id)) {
				produtoCesta.QuantidadeDown(quantidade);
				return true;
			}
		}
		return false;
	}

	public boolean produtoUp(Integer id, Integer quantidade, Integer limite) {
		for (ProdutoCesta produtoCesta : this.getProdutos()) {
			if (produtoCesta.compareId(id)) {
				produtoCesta.QuantidadeUp(quantidade, limite);
				return true;
			}
		}
		return false;
	}

	public BigDecimal getTotalCaixa() {
		BigDecimal total = BigDecimal.ZERO;
		for (Caixa caixa : caixas) {
			total = total.add(caixa.getValorBruto());
		}
		return total;
	}

	public BigDecimal getSaldo() {
		BigDecimal saldo = BigDecimal.ZERO;
		saldo = this.getTotal().subtract(this.getTotalCaixa());
		return saldo;
	}

	public boolean isQuitado() {
		if (this.getTotalCaixa().compareTo(this.getTotal()) >= 0)
			return true;
		return false;
	}

	public void clearCaixas() {
		this.caixas.clear();
	}

	public Integer getMes() {
		Calendar c = Calendar.getInstance();
		c.setTime(this.data);
		return c.get(Calendar.MONTH);
	}

}